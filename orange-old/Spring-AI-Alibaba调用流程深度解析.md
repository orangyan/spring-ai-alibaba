# Spring AI Alibaba 调用流程深度解析

## 📋 目录

- [概述](#概述)
- [场景一：简单聊天调用](#场景一简单聊天调用)
- [场景二：流式调用](#场景二流式调用)
- [场景三：Function Calling 调用](#场景三function-calling-调用)
- [场景四：RAG 检索增强调用](#场景四rag-检索增强调用)
- [场景五：Graph 工作流调用](#场景五graph-工作流调用)
- [底层实现详解](#底层实现详解)
- [性能优化和最佳实践](#性能优化和最佳实践)

---

## 概述

Spring AI Alibaba 的调用流程可以从多个层面理解：

1. **用户层面**：简单的 API 调用
2. **框架层面**：ChatClient → ChatModel → API
3. **网络层面**：HTTP 请求 → DashScope 服务
4. **增强层面**：Advisor 拦截 → 功能增强

本文将通过 **5 个典型场景**，深入剖析每个场景的完整调用流程。

---

## 场景一：简单聊天调用

### 1.1 用户代码

```java
@RestController
@RequestMapping("/chat")
public class ChatController {
    
    private final ChatClient chatClient;
    
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    @GetMapping("/simple")
    public String simpleChat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
}
```

### 1.2 完整调用流程

```
用户请求
    ↓
【1. HTTP 请求进入】
GET /chat/simple?message=你好
    ↓
【2. ChatController.simpleChat()】
    ↓
【3. ChatClient.prompt()】
创建 PromptSpec 对象
    ↓
【4. PromptSpec.user("你好")】
添加用户消息到 Prompt
    ↓
【5. PromptSpec.call()】
    ↓
【6. DefaultChatClient.call()】
    ├─ 构建 Prompt 对象
    ├─ 应用 defaultOptions (model, temperature 等)
    ├─ 执行 Advisor 链（如果有）
    └─ 调用 chatModel.call(prompt)
         ↓
【7. DashScopeChatModel.call(Prompt)】
    ├─ 构建 ChatCompletionRequest
    ├─ 设置 model, messages, options
    ├─ 应用 RetryTemplate（重试机制）
    └─ 调用 dashScopeApi.chatCompletionEntity()
         ↓
【8. DashScopeApi.chatCompletionEntity()】
    ├─ 构建 HTTP 请求
    ├─ 设置 Headers (Authorization, Content-Type)
    ├─ 序列化请求体
    └─ 发送 POST 请求到 DashScope
         ↓
【9. DashScope 服务处理】
    ├─ 模型推理
    ├─ 生成回复
    └─ 返回 ChatCompletion 响应
         ↓
【10. DashScopeApi 接收响应】
    ├─ 解析 JSON 响应
    └─ 构建 ResponseEntity<ChatCompletion>
         ↓
【11. DashScopeChatModel 处理响应】
    ├─ 提取 ChatCompletion
    ├─ 转换为 ChatResponse
    ├─ 提取 Generation（包含 AssistantMessage）
    └─ 记录 Token 使用情况
         ↓
【12. DefaultChatClient 后处理】
    ├─ 执行 Advisor 后处理（如果有）
    └─ 返回 ChatResponse
         ↓
【13. PromptSpec.content()】
提取 response.getResult().getOutput().getContent()
    ↓
【14. ChatController 返回】
返回字符串给用户
    ↓
HTTP 响应返回
```

### 1.3 关键对象和数据结构

#### Prompt 对象
```java
public class Prompt {
    private List<Message> instructions;  // 消息列表
    private ChatOptions options;         // 模型选项
    
    // 示例
    new Prompt(
        List.of(new UserMessage("你好")),
        DashScopeChatOptions.builder()
            .withModel("qwen-plus")
            .withTemperature(0.7)
            .build()
    )
}
```

#### ChatCompletionRequest（发送到 DashScope）
```json
{
  "model": "qwen-plus",
  "input": {
    "messages": [
      {
        "role": "user",
        "content": "你好"
      }
    ]
  },
  "parameters": {
    "result_format": "message",
    "temperature": 0.7,
    "top_p": 0.8
  }
}
```

#### ChatCompletion（DashScope 返回）
```json
{
  "output": {
    "choices": [
      {
        "finish_reason": "stop",
        "message": {
          "role": "assistant",
          "content": "你好！有什么我可以帮助你的吗？"
        }
      }
    ]
  },
  "usage": {
    "input_tokens": 3,
    "output_tokens": 10,
    "total_tokens": 13
  },
  "request_id": "uuid-xxx"
}
```

#### ChatResponse（Spring AI 返回）
```java
public class ChatResponse {
    private List<Generation> results;    // 生成结果列表
    private ChatResponseMetadata metadata; // 元数据
    
    // results[0]
    Generation {
        AssistantMessage output;  // "你好！有什么我可以帮助你的吗？"
        ChatGenerationMetadata metadata; // finish_reason, tokens
    }
}
```

### 1.4 核心源码详解

#### DashScopeChatModel.call() 核心实现

```java
@Override
public ChatResponse call(Prompt prompt) {
    // 1. 构建请求
    ChatCompletionRequest request = createRequest(prompt, false);
    
    // 2. 使用 RetryTemplate 执行（支持重试）
    ResponseEntity<ChatCompletion> completionEntity = this.retryTemplate.execute(ctx -> {
        
        // 3. 记录观测数据（OpenTelemetry）
        if (this.observationRegistry != null) {
            // 记录 Span
            observationRegistry.getCurrentObservation().event(...);
        }
        
        // 4. 调用 DashScope API
        return this.dashScopeApi.chatCompletionEntity(
            request,
            DashScopeApi.DEFAULT_CHAT_MODEL
        );
    });
    
    // 5. 提取响应
    ChatCompletion chatCompletion = completionEntity.getBody();
    
    // 6. 转换为 ChatResponse
    List<Generation> generations = chatCompletion.output().choices()
        .stream()
        .map(choice -> {
            return new Generation(
                new AssistantMessage(choice.message().content()),
                ChatGenerationMetadata.from(choice.finishReason(), null)
            );
        })
        .toList();
    
    // 7. 构建元数据
    ChatResponseMetadata metadata = ChatResponseMetadata.builder()
        .withUsage(new DefaultUsage(
            chatCompletion.usage().inputTokens(),
            chatCompletion.usage().outputTokens()
        ))
        .withId(chatCompletion.requestId())
        .build();
    
    // 8. 返回
    return new ChatResponse(generations, metadata);
}
```

#### DashScopeApi.chatCompletionEntity() 核心实现

```java
public ResponseEntity<ChatCompletion> chatCompletionEntity(
        ChatCompletionRequest request, 
        String model) {
    
    // 1. 构建请求 URL
    String url = String.format("%s/services/aigc/text-generation/generation", baseUrl);
    
    // 2. 使用 RestClient 发送请求
    return this.restClient.post()
        .uri(url)
        .header("Authorization", "Bearer " + apiKey)
        .header("Content-Type", "application/json")
        .body(request)
        .retrieve()
        .toEntity(ChatCompletion.class);
}
```

### 1.5 时序图

```
用户    ChatController    ChatClient    DashScopeChatModel    DashScopeApi    DashScope服务
 |            |                |                |                  |                |
 |--请求----->|                |                |                  |                |
 |            |--prompt()----->|                |                  |                |
 |            |                |--user("你好")-->|                  |                |
 |            |                |--call()-------->|                  |                |
 |            |                |                |--call(prompt)--->|                |
 |            |                |                |                  |--HTTP POST---->|
 |            |                |                |                  |                |--推理-->
 |            |                |                |                  |                |<--结果--
 |            |                |                |                  |<--响应---------  |
 |            |                |                |<--ChatResponse---|                |
 |            |                |<--ChatResponse-|                  |                |
 |            |<--content()--- |                |                  |                |
 |<--响应----- |                |                |                  |                |
```

### 1.6 性能数据

| 阶段 | 耗时 | 说明 |
|------|------|------|
| HTTP 请求接收 | <1ms | Spring MVC |
| ChatClient 构建 | <1ms | 对象创建 |
| DashScopeChatModel 处理 | 1-2ms | 请求构建 |
| HTTP 发送 | 5-10ms | 网络延迟 |
| DashScope 推理 | 200-1000ms | 模型推理 |
| HTTP 接收 | 5-10ms | 网络延迟 |
| 响应解析 | 1-2ms | JSON 解析 |
| **总计** | **~220-1030ms** | **主要耗时在模型推理** |

---

## 场景二：流式调用

### 2.1 用户代码

```java
@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> streamChat(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .stream()
        .content();
}
```

### 2.2 完整调用流程

```
用户请求
    ↓
【1. HTTP 请求进入（SSE 连接）】
GET /chat/stream?message=介绍Spring
    ↓
【2. ChatController.streamChat()】
    ↓
【3. ChatClient.prompt().user().stream()】
    ↓
【4. DefaultChatClient.stream()】
    ├─ 构建 Prompt
    ├─ 应用 Advisor（如果有）
    └─ 调用 chatModel.stream(prompt)
         ↓
【5. DashScopeChatModel.stream(Prompt)】
    ├─ 构建 ChatCompletionRequest（stream=true）
    ├─ 设置 enableIncrementalOutput=true
    └─ 调用 dashScopeApi.chatCompletionStream()
         ↓
【6. DashScopeApi.chatCompletionStream()】
    ├─ 构建 SSE 请求
    ├─ 使用 WebClient（支持流式）
    └─ 返回 Flux<ChatCompletionChunk>
         ↓
【7. DashScope 服务流式返回】
    ├─ 开始推理
    ├─ 每生成一个 token 就发送一次
    └─ 持续发送直到完成
         ↓
【8. WebClient 接收 SSE 流】
data: {"output":{"choices":[{"message":{"content":"Spring"}}]}}
data: {"output":{"choices":[{"message":{"content":" 是"}}]}}
data: {"output":{"choices":[{"message":{"content":"一个"}}]}}
...
data: [DONE]
    ↓
【9. DashScopeChatModel 转换流】
    ├─ 解析每个 ChatCompletionChunk
    ├─ 提取增量内容
    ├─ 转换为 ChatResponse
    └─ 发射到 Flux<ChatResponse>
         ↓
【10. DefaultChatClient 后处理】
    ├─ 执行 Advisor 后处理
    └─ 返回 Flux<ChatResponse>
         ↓
【11. PromptSpec.content()】
    ├─ 提取每个 response 的 content
    └─ 转换为 Flux<String>
         ↓
【12. ChatController 返回】
    ├─ Spring WebFlux 处理 Flux
    └─ 通过 SSE 发送给客户端
         ↓
【13. 客户端接收 SSE 流】
data: Spring
data:  是
data: 一个
...
```

### 2.3 核心实现

#### DashScopeChatModel.stream() 核心代码

```java
@Override
public Flux<ChatResponse> stream(Prompt prompt) {
    // 1. 构建流式请求
    ChatCompletionRequest request = createRequest(prompt, true);
    
    // 2. 调用流式 API
    Flux<ChatCompletionChunk> completionChunks = this.retryTemplate.execute(ctx -> {
        return this.dashScopeApi.chatCompletionStream(
            request,
            DashScopeApi.DEFAULT_CHAT_MODEL
        );
    });
    
    // 3. 转换为 ChatResponse 流
    return completionChunks
        .switchMap(chunk -> {
            // 处理每个增量块
            if (chunk.output() == null || chunk.output().choices().isEmpty()) {
                return Flux.empty();
            }
            
            ChatCompletionChunk.Choice choice = chunk.output().choices().get(0);
            String content = choice.message().content();
            
            // 构建 ChatResponse
            Generation generation = new Generation(
                new AssistantMessage(content),
                ChatGenerationMetadata.from(choice.finishReason(), null)
            );
            
            return Flux.just(new ChatResponse(List.of(generation)));
        })
        .onErrorResume(throwable -> {
            // 错误处理
            return Flux.error(throwable);
        });
}
```

#### DashScopeApi.chatCompletionStream() 核心代码

```java
public Flux<ChatCompletionChunk> chatCompletionStream(
        ChatCompletionRequest request,
        String model) {
    
    // 1. 构建 URL
    String url = String.format("%s/services/aigc/text-generation/generation", baseUrl);
    
    // 2. 使用 WebClient 发送流式请求
    return this.webClient.post()
        .uri(url)
        .header("Authorization", "Bearer " + apiKey)
        .header("Content-Type", "application/json")
        .header("Accept", "text/event-stream")
        .bodyValue(request)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
        .map(sse -> {
            // 解析 SSE 数据
            String data = sse.data();
            if (data.equals("[DONE]")) {
                return null;
            }
            // 解析 JSON
            return objectMapper.readValue(data, ChatCompletionChunk.class);
        })
        .filter(Objects::nonNull);
}
```

### 2.4 流式调用的优势

1. **更快的首字响应**：首个 token ~200ms（vs 完整响应 ~1000ms）
2. **更好的用户体验**：逐字显示，类似打字效果
3. **降低内存占用**：不需要等待完整响应
4. **支持长文本**：避免超时问题

---

## 场景三：Function Calling 调用

### 3.1 用户代码

```java
@Service
public class WeatherService {
    
    @Tool(description = "获取指定城市的天气信息")
    public String getWeather(
        @ToolParam(description = "城市名称") String city) {
        // 模拟调用天气 API
        return city + "的天气是晴天，温度25度";
    }
}

@RestController
public class ChatController {
    
    private final ChatClient chatClient;
    
    public ChatController(
            ChatClient.Builder builder,
            WeatherService weatherService) {
        
        this.chatClient = builder
            .defaultFunctions(weatherService)  // 注册工具
            .build();
    }
    
    @GetMapping("/function")
    public String functionChat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
}
```

### 3.2 完整调用流程

```
用户请求: "北京的天气怎么样？"
    ↓
【第一轮调用：LLM 决策】
    ↓
【1-6】同简单调用流程
    ↓
【7. DashScopeChatModel.call()】
    ├─ 发送请求（带 tools 定义）
    └─ 接收响应
         ↓
【8. DashScope 返回 Tool Call】
{
  "output": {
    "choices": [{
      "finish_reason": "tool_calls",
      "message": {
        "role": "assistant",
        "tool_calls": [{
          "id": "call_xxx",
          "type": "function",
          "function": {
            "name": "getWeather",
            "arguments": "{\"city\":\"北京\"}"
          }
        }]
      }
    }]
  }
}
    ↓
【9. DashScopeChatModel 检测到 Tool Call】
    ├─ finish_reason = "tool_calls"
    ├─ 提取 function name 和 arguments
    └─ 调用 ToolCallingManager.executeToolCall()
         ↓
【10. ToolCallingManager.executeToolCall()】
    ├─ 查找注册的 Function（getWeather）
    ├─ 解析参数：city = "北京"
    ├─ 反射调用 weatherService.getWeather("北京")
    └─ 获取结果："北京的天气是晴天，温度25度"
         ↓
【11. 构建 Tool Result Message】
{
  "role": "tool",
  "name": "getWeather",
  "content": "北京的天气是晴天，温度25度"
}
    ↓
【第二轮调用：LLM 总结】
    ↓
【12. DashScopeChatModel 发起第二次调用】
messages = [
  {"role": "user", "content": "北京的天气怎么样？"},
  {"role": "assistant", "tool_calls": [...]},
  {"role": "tool", "name": "getWeather", "content": "..."}
]
    ↓
【13. DashScope 推理】
    ├─ 接收工具调用结果
    ├─ 基于结果生成回复
    └─ 返回最终答案
         ↓
【14. 最终响应】
{
  "output": {
    "choices": [{
      "finish_reason": "stop",
      "message": {
        "role": "assistant",
        "content": "根据查询结果，北京今天天气晴朗，温度为25度。"
      }
    }]
  }
}
    ↓
【15. 返回给用户】
"根据查询结果，北京今天天气晴朗，温度为25度。"
```

### 3.3 核心实现

#### Function 注册过程

```java
// 1. Spring Boot 自动扫描 @Tool 注解
@Configuration
public class FunctionAutoConfiguration {
    
    @Bean
    public FunctionCallbackContext functionCallbackContext(
            ApplicationContext applicationContext) {
        
        FunctionCallbackContext context = new FunctionCallbackContext();
        
        // 2. 查找所有带 @Tool 注解的方法
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Service.class);
        
        for (Object bean : beans.values()) {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Tool.class)) {
                    // 3. 包装为 FunctionCallback
                    FunctionCallback callback = new MethodInvokingFunctionCallback(
                        method,
                        bean,
                        method.getAnnotation(Tool.class).description()
                    );
                    
                    // 4. 注册到 Context
                    context.register(method.getName(), callback);
                }
            }
        }
        
        return context;
    }
}
```

#### DashScopeChatModel 处理 Tool Calls

```java
@Override
public ChatResponse call(Prompt prompt) {
    // ... 前面的代码 ...
    
    ChatResponse response = callDashScope(prompt);
    
    // 检查是否有 Tool Calls
    Generation generation = response.getResult();
    AssistantMessage assistantMessage = generation.getOutput();
    
    if (assistantMessage.getToolCalls() != null && 
        !assistantMessage.getToolCalls().isEmpty()) {
        
        // 执行工具调用
        List<Message> toolMessages = new ArrayList<>();
        toolMessages.add(new UserMessage(prompt.getInstructions().get(0).getContent()));
        toolMessages.add(assistantMessage);
        
        for (AssistantMessage.ToolCall toolCall : assistantMessage.getToolCalls()) {
            // 执行工具
            String result = toolCallingManager.executeToolCall(toolCall);
            
            // 添加工具结果消息
            toolMessages.add(new ToolResponseMessage(
                result,
                toolCall.id(),
                toolCall.name()
            ));
        }
        
        // 发起第二次调用
        Prompt secondPrompt = new Prompt(toolMessages, prompt.getOptions());
        return call(secondPrompt);
    }
    
    return response;
}
```

### 3.4 Tool Definition 结构

发送到 DashScope 的 tools 定义：

```json
{
  "model": "qwen-plus",
  "input": {
    "messages": [
      {"role": "user", "content": "北京的天气怎么样？"}
    ]
  },
  "parameters": {
    "tools": [
      {
        "type": "function",
        "function": {
          "name": "getWeather",
          "description": "获取指定城市的天气信息",
          "parameters": {
            "type": "object",
            "properties": {
              "city": {
                "type": "string",
                "description": "城市名称"
              }
            },
            "required": ["city"]
          }
        }
      }
    ]
  }
}
```

---

## 场景四：RAG 检索增强调用

### 4.1 用户代码

```java
@Configuration
public class RagConfig {
    
    @Bean
    public ChatClient ragChatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore) {
        
        return builder
            .defaultAdvisors(
                new DocumentRetrievalAdvisor(vectorStore)
            )
            .build();
    }
}

@RestController
public class RagController {
    
    private final ChatClient ragChatClient;
    
    @GetMapping("/rag")
    public String ragChat(@RequestParam String question) {
        return ragChatClient.prompt()
            .user(question)
            .call()
            .content();
    }
}
```

### 4.2 完整调用流程

```
用户请求: "Spring AI Alibaba 是什么？"
    ↓
【1. HTTP 请求进入】
    ↓
【2-4】同简单调用
    ↓
【5. DefaultChatClient.call()】
    ├─ 构建 Prompt
    ├─ 应用 defaultOptions
    └─ 执行 Advisor 链
         ↓
【6. DocumentRetrievalAdvisor.adviseRequest()】
    ↓
【7. 向量检索】
    ├─ 提取用户问题："Spring AI Alibaba 是什么？"
    ├─ 向量化问题（Embedding）
    ├─ 在 VectorStore 中相似度搜索
    └─ 返回 Top-K 文档
         ↓
【8. VectorStore.similaritySearch()】
    ├─ 使用 EmbeddingModel 向量化查询
    ├─ 在向量数据库中搜索（如 Redis/Elasticsearch）
    ├─ 计算余弦相似度
    └─ 返回相似文档列表
         ↓
【9. 检索结果】
[
  {
    "content": "Spring AI Alibaba 是阿里云通义千问的 Java AI 框架...",
    "score": 0.92,
    "metadata": {"source": "doc1.pdf"}
  },
  {
    "content": "Spring AI Alibaba 提供 ChatModel、ImageModel...",
    "score": 0.88,
    "metadata": {"source": "doc2.pdf"}
  }
]
    ↓
【10. DocumentRetrievalAdvisor 增强 Prompt】
    ├─ 构建系统消息
    ├─ 将检索到的文档注入到上下文
    └─ 修改 Prompt
         ↓
【11. 增强后的 Prompt】
messages = [
  {
    "role": "system",
    "content": "请基于以下上下文回答用户问题：\n\n【文档1】\nSpring AI Alibaba 是阿里云通义千问的 Java AI 框架...\n\n【文档2】\nSpring AI Alibaba 提供 ChatModel、ImageModel..."
  },
  {
    "role": "user",
    "content": "Spring AI Alibaba 是什么？"
  }
]
    ↓
【12. 调用 ChatModel】
    ├─ 使用增强后的 Prompt
    └─ 调用 DashScopeChatModel.call()
         ↓
【13. DashScope 推理】
    ├─ 基于注入的上下文
    ├─ 结合用户问题
    └─ 生成准确回答
         ↓
【14. 返回响应】
"Spring AI Alibaba 是阿里云通义千问的 Java AI 应用框架，它提供了 ChatModel、ImageModel、EmbeddingModel 等接口，帮助开发者快速构建 AI 应用..."
```

### 4.3 核心实现

#### DocumentRetrievalAdvisor 核心代码

```java
public class DocumentRetrievalAdvisor implements RequestResponseAdvisor {
    
    private final VectorStore vectorStore;
    private final int topK;
    
    @Override
    public AdvisedRequest adviseRequest(
            AdvisedRequest request, 
            Map<String, Object> context) {
        
        // 1. 提取用户问题
        String userMessage = request.userText();
        
        // 2. 向量检索
        List<Document> documents = vectorStore.similaritySearch(
            SearchRequest.query(userMessage).withTopK(topK)
        );
        
        // 3. 构建上下文
        String contextText = documents.stream()
            .map(doc -> String.format("【文档】\n%s", doc.getContent()))
            .collect(Collectors.joining("\n\n"));
        
        // 4. 构建系统消息
        String systemMessage = String.format(
            "请基于以下上下文回答用户问题：\n\n%s",
            contextText
        );
        
        // 5. 修改 Prompt
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemMessage));
        messages.addAll(request.messages());
        
        // 6. 返回增强后的请求
        return AdvisedRequest.from(request)
            .withMessages(messages)
            .build();
    }
}
```

#### VectorStore.similaritySearch() 实现

```java
@Override
public List<Document> similaritySearch(SearchRequest request) {
    // 1. 向量化查询
    String query = request.getQuery();
    List<Double> queryEmbedding = embeddingModel.embed(query);
    
    // 2. 在 Redis 中搜索
    String indexName = "spring-ai-index";
    Query redisQuery = new Query(indexName)
        .returnFields("content", "metadata")
        .setSortBy("__vector_score")
        .dialect(2);
    
    redisQuery.addParam("query_vector", 
        toByteArray(queryEmbedding));
    
    // 3. 执行搜索
    SearchResult result = jedis.ftSearch(
        indexName,
        redisQuery,
        SearchOptions.builder()
            .limit(0, request.getTopK())
            .build()
    );
    
    // 4. 解析结果
    return result.getDocuments().stream()
        .map(doc -> {
            Document document = new Document(
                doc.getString("content")
            );
            document.setMetadata(
                parseMetadata(doc.getString("metadata"))
            );
            document.setScore(
                doc.getScore()
            );
            return document;
        })
        .collect(Collectors.toList());
}
```

### 4.4 RAG 调用的关键点

1. **Embedding 模型**：查询和文档使用相同的模型向量化
2. **Top-K 选择**：通常选择 3-10 个文档
3. **相似度阈值**：过滤低相关文档（如 0.7）
4. **上下文长度**：注意不要超过模型上下文窗口

---

## 场景五：Graph 工作流调用

### 5.1 用户代码

```java
@Configuration
public class GraphConfig {
    
    @Bean
    public CompiledGraph weatherWorkflow(
            ChatClient chatClient,
            WeatherService weatherService) {
        
        // 1. 创建状态工厂
        StateFactory<WeatherState> stateFactory = StateFactory.builder(WeatherState.class)
            .addKeyStrategy("messages", Appender.class)
            .build();
        
        // 2. 定义节点
        NodeAction<WeatherState> extractCityNode = (state, config) -> {
            // 从消息中提取城市名
            String lastMessage = state.getMessages().get(state.getMessages().size() - 1);
            String city = extractCity(lastMessage);
            state.setCity(city);
            return state;
        };
        
        NodeAction<WeatherState> getWeatherNode = (state, config) -> {
            // 调用天气服务
            String weather = weatherService.getWeather(state.getCity());
            state.setWeather(weather);
            return state;
        };
        
        NodeAction<WeatherState> generateResponseNode = (state, config) -> {
            // 使用 LLM 生成回复
            String prompt = String.format(
                "用户问：%s\n天气信息：%s\n请生成友好的回复。",
                state.getMessages().get(0),
                state.getWeather()
            );
            
            String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
            
            state.addMessage(response);
            return state;
        };
        
        // 3. 构建 Graph
        StateGraph<WeatherState> graph = new StateGraph<>(
            "weather-workflow",
            stateFactory
        );
        
        graph.addNode("extract_city", extractCityNode)
            .addNode("get_weather", getWeatherNode)
            .addNode("generate_response", generateResponseNode)
            .addEdge(START, "extract_city")
            .addEdge("extract_city", "get_weather")
            .addEdge("get_weather", "generate_response")
            .addEdge("generate_response", END);
        
        // 4. 编译
        return graph.compile();
    }
}

@RestController
public class GraphController {
    
    private final CompiledGraph weatherWorkflow;
    
    @GetMapping("/graph")
    public String graphChat(@RequestParam String message) {
        // 创建初始状态
        WeatherState initialState = new WeatherState();
        initialState.addMessage(message);
        
        // 执行 Graph
        WeatherState finalState = weatherWorkflow.invoke(initialState);
        
        // 返回最后一条消息
        List<String> messages = finalState.getMessages();
        return messages.get(messages.size() - 1);
    }
}
```

### 5.2 完整调用流程

```
用户请求: "北京今天天气怎么样？"
    ↓
【1. HTTP 请求进入】
    ↓
【2. GraphController.graphChat()】
    ├─ 创建初始状态
    └─ 调用 weatherWorkflow.invoke(initialState)
         ↓
【3. CompiledGraph.invoke()】
    ├─ 初始化执行上下文
    ├─ 设置起始节点 START
    └─ 启动 GraphExecutor
         ↓
【4. GraphExecutor 执行流程】
    ↓
【节点1: extract_city】
    ├─ 接收状态: {messages: ["北京今天天气怎么样？"]}
    ├─ 执行节点逻辑: 提取城市名
    ├─ 更新状态: {messages: [...], city: "北京"}
    └─ 查找下一个节点: get_weather
         ↓
【节点2: get_weather】
    ├─ 接收状态: {messages: [...], city: "北京"}
    ├─ 执行节点逻辑: 调用 weatherService.getWeather("北京")
    ├─ 获取天气: "北京的天气是晴天，温度25度"
    ├─ 更新状态: {messages: [...], city: "北京", weather: "..."}
    └─ 查找下一个节点: generate_response
         ↓
【节点3: generate_response】
    ├─ 接收状态: {messages: [...], city: "北京", weather: "..."}
    ├─ 执行节点逻辑:
    │   ├─ 构建 Prompt
    │   └─ 调用 chatClient.prompt().user(...).call()
    │        ↓
    │   【嵌套：LLM 调用】
    │        ├─ DashScopeChatModel.call()
    │        ├─ DashScope 推理
    │        └─ 返回: "今天北京天气不错，晴朗温暖，温度25度，适合外出。"
    │        ↓
    ├─ 更新状态: {messages: [..., "今天北京天气不错..."], ...}
    └─ 查找下一个节点: END
         ↓
【5. GraphExecutor 完成】
    ├─ 到达 END 节点
    ├─ 返回最终状态
    └─ finalState = {messages: ["北京今天天气怎么样？", "今天北京天气不错..."], ...}
         ↓
【6. GraphController 返回】
提取最后一条消息: "今天北京天气不错，晴朗温暖，温度25度，适合外出。"
    ↓
HTTP 响应返回
```

### 5.3 核心实现

#### CompiledGraph.invoke() 核心代码

```java
@Override
public <S extends OverAllState> S invoke(S initialState) {
    // 1. 创建执行上下文
    GraphExecutionContext<S> context = new GraphExecutionContext<>(
        this.graphDefinition,
        initialState
    );
    
    // 2. 设置起始节点
    context.setCurrentNode(START);
    
    // 3. 执行 Graph
    while (!context.isFinished()) {
        String currentNodeId = context.getCurrentNode();
        
        if (currentNodeId.equals(END)) {
            break;
        }
        
        // 4. 获取节点定义
        NodeDefinition<S> nodeDef = graphDefinition.getNode(currentNodeId);
        
        // 5. 执行节点
        S newState = nodeDef.getAction().execute(
            context.getState(),
            nodeDef.getConfig()
        );
        
        // 6. 更新状态
        context.setState(newState);
        
        // 7. 查找下一个节点
        String nextNode = findNextNode(currentNodeId, newState);
        context.setCurrentNode(nextNode);
    }
    
    // 8. 返回最终状态
    return context.getState();
}
```

#### NodeAction 执行示例

```java
NodeAction<WeatherState> getWeatherNode = (state, config) -> {
    // 1. 获取输入
    String city = state.getCity();
    
    // 2. 执行业务逻辑
    String weather = weatherService.getWeather(city);
    
    // 3. 更新状态
    state.setWeather(weather);
    
    // 4. 返回新状态
    return state;
};
```

### 5.4 Graph 调用的特点

1. **状态驱动**：所有数据通过 State 传递
2. **节点解耦**：每个节点独立执行
3. **可观测**：每个节点的输入输出都可追踪
4. **可恢复**：支持 Checkpoint，可中断恢复

---

## 底层实现详解

### 6.1 Spring Boot 自动配置

#### DashScopeChatModel 如何被创建？

```java
@Configuration
@ConditionalOnClass(DashScopeChatModel.class)
@EnableConfigurationProperties(DashScopeProperties.class)
public class DashScopeAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public DashScopeApi dashScopeApi(DashScopeProperties properties) {
        return new DashScopeApi(
            properties.getBaseUrl(),
            properties.getApiKey(),
            properties.getWorkspaceId()
        );
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DashScopeChatModel dashScopeChatModel(
            DashScopeApi dashScopeApi,
            DashScopeProperties properties) {
        
        return DashScopeChatModel.builder()
            .dashScopeApi(dashScopeApi)
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .withModel(properties.getChat().getOptions().getModel())
                    .withTemperature(properties.getChat().getOptions().getTemperature())
                    .build()
            )
            .build();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ChatClient.Builder chatClientBuilder(ChatModel chatModel) {
        return ChatClient.builder(chatModel);
    }
}
```

#### 配置文件

```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      base-url: https://dashscope.aliyuncs.com/api/v1
      chat:
        options:
          model: qwen-plus
          temperature: 0.7
          top-p: 0.8
          max-tokens: 2000
```

### 6.2 HTTP 客户端实现

#### RestClient（同步调用）

```java
public class DashScopeApi {
    
    private final RestClient restClient;
    
    public DashScopeApi(String baseUrl, String apiKey) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
    
    public ResponseEntity<ChatCompletion> chatCompletionEntity(
            ChatCompletionRequest request) {
        
        return restClient.post()
            .uri("/services/aigc/text-generation/generation")
            .body(request)
            .retrieve()
            .toEntity(ChatCompletion.class);
    }
}
```

#### WebClient（流式调用）

```java
public class DashScopeApi {
    
    private final WebClient webClient;
    
    public DashScopeApi(String baseUrl, String apiKey) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
    
    public Flux<ChatCompletionChunk> chatCompletionStream(
            ChatCompletionRequest request) {
        
        return webClient.post()
            .uri("/services/aigc/text-generation/generation")
            .bodyValue(request)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
            .mapNotNull(sse -> {
                String data = sse.data();
                if ("[DONE]".equals(data)) {
                    return null;
                }
                return parseChunk(data);
            });
    }
}
```

### 6.3 重试机制

```java
@Bean
public RetryTemplate retryTemplate() {
    return RetryTemplate.builder()
        .maxAttempts(3)
        .fixedBackoff(1000)
        .retryOn(IOException.class)
        .retryOn(TimeoutException.class)
        .build();
}

// 使用
public ChatResponse call(Prompt prompt) {
    return retryTemplate.execute(context -> {
        return dashScopeApi.chatCompletionEntity(request);
    });
}
```

### 6.4 可观测性集成

```java
@Bean
public ChatModel observedChatModel(
        ChatModel chatModel,
        ObservationRegistry observationRegistry) {
    
    return new ObservationChatModelWrapper(
        chatModel,
        observationRegistry
    );
}

public class ObservationChatModelWrapper implements ChatModel {
    
    @Override
    public ChatResponse call(Prompt prompt) {
        return Observation.createNotStarted("chat.call", observationRegistry)
            .lowCardinalityKeyValue("model", "qwen-plus")
            .observe(() -> {
                // 记录输入
                Span span = tracer.currentSpan();
                span.tag("input", prompt.getContents());
                
                // 执行调用
                ChatResponse response = chatModel.call(prompt);
                
                // 记录输出
                span.tag("output", response.getResult().getOutput().getContent());
                span.tag("tokens", String.valueOf(response.getMetadata().getUsage().getTotalTokens()));
                
                return response;
            });
    }
}
```

---

## 性能优化和最佳实践

### 7.1 性能瓶颈分析

| 阶段 | 平均耗时 | 优化建议 |
|------|----------|----------|
| 请求构建 | 1-2ms | ✅ 已优化 |
| 网络传输 | 10-20ms | 使用 CDN，就近部署 |
| **模型推理** | **200-1000ms** | **选择合适的模型** |
| 响应解析 | 1-2ms | ✅ 已优化 |
| Function Calling | +500ms | 并行执行，缓存结果 |
| RAG 检索 | +50-100ms | 优化向量索引 |

### 7.2 最佳实践

#### 1. 使用流式调用

```java
// ❌ 不推荐：同步等待
String response = chatClient.prompt()
    .user(longText)
    .call()  // 等待 1000ms
    .content();

// ✅ 推荐：流式返回
Flux<String> responseStream = chatClient.prompt()
    .user(longText)
    .stream()  // 200ms 首字响应
    .content();
```

#### 2. 缓存常见查询

```java
@Cacheable("chat-responses")
public String cachedChat(String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
}
```

#### 3. 异步处理

```java
@Async
public CompletableFuture<String> asyncChat(String message) {
    String response = chatClient.prompt()
        .user(message)
        .call()
        .content();
    return CompletableFuture.completedFuture(response);
}
```

#### 4. 批量处理

```java
public List<String> batchChat(List<String> messages) {
    return messages.parallelStream()
        .map(msg -> chatClient.prompt().user(msg).call().content())
        .collect(Collectors.toList());
}
```

#### 5. 合理设置超时

```java
@Bean
public DashScopeChatModel chatModel(DashScopeApi api) {
    return DashScopeChatModel.builder()
        .dashScopeApi(api)
        .defaultOptions(
            DashScopeChatOptions.builder()
                .withTimeout(Duration.ofSeconds(30))
                .build()
        )
        .build();
}
```

### 7.3 成本优化

#### 1. 选择合适的模型

| 模型 | 输入价格 | 输出价格 | 适用场景 |
|------|---------|---------|---------|
| qwen-turbo | 0.3元/百万tokens | 0.6元/百万tokens | 简单对话 |
| qwen-plus | 0.8元/百万tokens | 2.0元/百万tokens | 复杂推理 |
| qwen-max | 20元/百万tokens | 60元/百万tokens | 高精度任务 |

#### 2. 减少 Token 消耗

```java
// ❌ 冗长的 Prompt
String prompt = "请你非常详细地、尽可能完整地、全面地回答我的问题...";

// ✅ 简洁的 Prompt
String prompt = "请简要回答：";
```

#### 3. 使用 RAG 替代长上下文

```java
// ❌ 直接发送大量文档（消耗大量 tokens）
String context = loadAllDocuments(); // 10000+ tokens
chatClient.prompt()
    .system(context)
    .user(question)
    .call();

// ✅ 使用 RAG 只检索相关文档（仅消耗必要 tokens）
chatClient.prompt()
    .advisors(new DocumentRetrievalAdvisor(vectorStore))
    .user(question)
    .call();
```

---

## 总结

### 调用流程总结

1. **简单调用**: 用户 → ChatClient → ChatModel → DashScope API → 返回
2. **流式调用**: 增加 SSE 连接，逐 token 返回
3. **Function Calling**: 两轮调用，第一轮获取工具调用，第二轮生成回复
4. **RAG 调用**: 先检索相关文档，注入到上下文，再调用 LLM
5. **Graph 调用**: 节点编排，状态驱动，可包含多次 LLM 调用

### 关键组件

- **ChatClient**: 高层 API，提供流式接口
- **ChatModel**: 模型抽象，封装 DashScope 调用
- **DashScopeApi**: HTTP 客户端，处理网络通信
- **Advisor**: 请求/响应拦截器，实现功能增强
- **RetryTemplate**: 重试机制，提高可靠性
- **ObservationRegistry**: 可观测性，记录调用链路

### 性能关键点

- **模型推理**是主要耗时（200-1000ms）
- **流式调用**可提升用户体验（首字 200ms）
- **Function Calling**增加一轮调用（+500ms）
- **RAG 检索**增加 50-100ms
- **网络延迟**10-20ms

### 优化建议

1. 使用流式调用提升体验
2. 缓存常见查询减少成本
3. 异步处理提高吞吐量
4. 选择合适模型平衡性能和成本
5. 使用 RAG 替代长上下文

---

**文档版本**: v1.0.0  
**最后更新**: 2025-10-05  
**作者**: Spring AI Alibaba Team

