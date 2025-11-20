# Spring AI Alibaba 特有设计详解

## 📋 目录

- [概述](#概述)
- [DashScopeChatOptions - 配置选项类](#dashscopechatoptions---配置选项类)
- [DashScopeApi - HTTP 客户端封装](#dashscopeapi---http-客户端封装)
- [DashScopeChatModel - ChatModel 实现](#dashscopechatmodel---chatmodel-实现)
- [设计对比：Spring AI vs Spring AI Alibaba](#设计对比spring-ai-vs-spring-ai-alibaba)
- [完整使用示例](#完整使用示例)
- [最佳实践](#最佳实践)

---

## 概述

### 1.1 Spring AI 标准接口

Spring AI 提供了一套标准接口，让开发者可以用统一的方式调用不同的 AI 服务：

```
┌─────────────────────────────────────────────────────────┐
│             Spring AI 标准接口层                        │
│                                                          │
│  ChatModel  ImageModel  EmbeddingModel  ...             │
│     ↑           ↑            ↑                           │
└─────┼───────────┼────────────┼───────────────────────────┘
      │           │            │
      │           │            │
┌─────┼───────────┼────────────┼───────────────────────────┐
│     │           │            │                           │
│ OpenAI     Azure     Google      Alibaba                 │
│  Impl      Impl       Impl         Impl                  │
│                                                           │
│              各厂商具体实现层                             │
└───────────────────────────────────────────────────────────┘
```

### 1.2 Spring AI Alibaba 的设计

Spring AI Alibaba 提供了阿里云百炼（DashScope）服务的完整实现：

```
┌─────────────────────────────────────────────────────────┐
│                   Spring AI Alibaba                      │
│                                                          │
│  ┌────────────────┐  ┌────────────────┐                │
│  │ DashScopeChatModel                  │                │
│  │  implements ChatModel                                │
│  └────────────────┘  └────────────────┘                │
│          ↓                   ↓                           │
│  ┌────────────────┐  ┌────────────────┐                │
│  │ DashScopeApi   │  │DashScopeChatOptions              │
│  │ (HTTP Client)  │  │(Configuration)  │                │
│  └────────────────┘  └────────────────┘                │
└─────────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────────┐
│            阿里云百炼 (DashScope) 服务                   │
│                                                          │
│  qwen-plus | qwen-max | qwen-turbo | ...                │
└─────────────────────────────────────────────────────────┘
```

### 1.3 核心类关系图

```
DashScopeChatModel
    ├── 持有 DashScopeApi（HTTP 客户端）
    ├── 持有 DashScopeChatOptions（默认配置）
    ├── 持有 RetryTemplate（重试机制）
    └── 持有 ObservationRegistry（可观测性）
         ↓
    调用 DashScopeApi
         ↓
    发送 HTTP 请求到 DashScope 服务
```

---

## DashScopeChatOptions - 配置选项类

### 2.1 设计目的

`DashScopeChatOptions` 是对 Spring AI 标准接口 `ChatOptions` 的具体实现，专门为阿里云百炼（DashScope）服务设计，包含了所有 DashScope API 支持的配置参数。

### 2.2 类定义

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashScopeChatOptions implements ToolCallingChatOptions {
    // 实现了两个接口：
    // 1. ChatOptions - Spring AI 标准接口
    // 2. ToolCallingChatOptions - Function Calling 扩展接口
}
```

### 2.3 核心配置参数

#### 2.3.1 基础参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `model` | String | qwen-plus | 模型名称 |
| `temperature` | Double | 0.85 | 随机性控制，范围 [0, 2) |
| `topP` | Double | 0.8 | 核采样阈值，范围 (0, 1.0) |
| `topK` | Integer | null | 采样候选池大小 |
| `maxTokens` | Integer | - | 最大生成 Token 数 |
| `seed` | Integer | - | 随机种子，用于复现 |

**详细说明**:

```java
/**
 * temperature - 控制随机性和多样性
 * 
 * 原理：通过平滑概率分布来控制输出
 * - 更高的值：降低分布峰值，允许更多低概率 token，输出更多样
 * - 更低的值：增加分布峰值，高概率 token 更容易被选中，输出更确定
 * 
 * 范围：[0, 2)，系统默认 0.85
 * 注意：不推荐设置为 0
 * 
 * 使用建议：
 * - 创意写作：0.9-1.2（更随机）
 * - 日常对话：0.7-0.9（平衡）
 * - 精确任务：0.1-0.3（更确定）
 */
private Double temperature;

/**
 * topP - 核采样（Nucleus Sampling）
 * 
 * 原理：只保留累积概率达到阈值的候选 token
 * 例如：top_p = 0.8，只有累积概率达到 80% 的 token 才会被保留
 * 
 * 范围：(0, 1.0)，默认 0.8
 * 注意：不要设置 >= 1.0
 * 
 * 使用建议：
 * - 更高的值：增加随机性
 * - 更低的值：增加确定性
 */
private Double topP;

/**
 * topK - Top-K 采样
 * 
 * 原理：只保留得分最高的 K 个 token
 * 例如：top_k = 50，只有得分最高的 50 个 token 会被考虑
 * 
 * 默认：null（禁用）
 * 注意：如果 top_k 为 null 或 > 100，则禁用，只使用 top_p
 * 
 * 使用建议：
 * - 较大的值：增加随机性
 * - 较小的值：增加确定性
 */
private Integer topK;

/**
 * seed - 随机种子
 * 
 * 用途：控制生成的可复现性
 * 类型：无符号 64 位整数
 * 
 * 注意：提供 seed 后，模型会尝试生成相同或相似的结果，
 *      但无法保证完全的可复现性
 */
private Integer seed;
```

#### 2.3.2 DashScope 特有参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `enableSearch` | Boolean | false | 是否启用联网搜索 |
| `searchOptions` | SearchOptions | - | 联网搜索策略配置 |
| `repetitionPenalty` | Double | 1.1 | 重复惩罚，控制重复度 |
| `incrementalOutput` | Boolean | true | 流式输出是否增量 |
| `responseFormat` | ResponseFormat | - | 返回格式（text/json_object） |
| `enableThinking` | Boolean | false | 是否启用思维过程 |
| `thinkingBudget` | Integer | - | 思维过程最大长度 |

**详细说明**:

```java
/**
 * enableSearch - 联网搜索
 * 
 * 用途：模型内置的联网搜索服务
 * 
 * 可选值：
 * - true：启用联网搜索，模型会参考搜索结果
 * - false（默认）：禁用联网搜索
 * 
 * 注意：即使启用，模型也会"自主决定"是否使用搜索结果
 */
@JsonProperty("enable_search")
private Boolean enableSearch = false;

/**
 * searchOptions - 搜索策略配置
 * 
 * 仅在 enableSearch = true 时生效
 * 
 * 配置项：
 * - searchStrategy: 搜索策略
 * - timeRange: 时间范围
 * - region: 地理区域
 */
@JsonProperty("search_options")
private DashScopeApi.SearchOptions searchOptions;

/**
 * repetitionPenalty - 重复惩罚
 * 
 * 用途：控制模型生成内容的重复度
 * 
 * 范围：增加该值可以减少重复
 * 默认：1.1
 * 1.0：表示不惩罚
 */
@JsonProperty("repetition_penalty")
private Double repetitionPenalty;

/**
 * incrementalOutput - 增量输出
 * 
 * 用途：控制流式输出模式下，后续输出是否包含之前的内容
 * 
 * - true（默认）：增量模式，后续输出不包含之前内容，需要自己拼接
 * - false：后续输出包含之前内容
 * 
 * 示例：
 * true:  "Spring" → " AI" → " Alibaba"
 * false: "Spring" → "Spring AI" → "Spring AI Alibaba"
 */
@JsonProperty("incremental_output")
private Boolean incrementalOutput = true;

/**
 * responseFormat - 返回格式
 * 
 * 用途：指定模型返回内容的格式
 * 
 * 可选值：
 * - {"type": "text"}：文本格式（默认）
 * - {"type": "json_object"}：JSON 对象格式
 * 
 * 使用场景：
 * - 结构化输出：使用 json_object
 * - 自由文本：使用 text
 */
@JsonProperty("response_format")
private DashScopeResponseFormat responseFormat;

/**
 * enableThinking - 启用思维过程
 * 
 * 用途：让模型展示其思考过程
 * 默认：false
 * 
 * 适用模型：Qwen3 全系模型
 */
@JsonProperty("enable_thinking")
private Boolean enableThinking = false;

/**
 * thinkingBudget - 思维过程最大长度
 * 
 * 用途：限制思维过程的长度
 * 仅在 enableThinking = true 时生效
 */
@JsonProperty("thinking_budget")
private Integer thinkingBudget;
```

#### 2.3.3 Function Calling 参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `tools` | List<FunctionTool> | - | 可调用的工具列表 |
| `toolChoice` | Object | auto | 工具选择策略 |
| `parallelToolCalls` | Boolean | - | 是否启用并行工具调用 |
| `toolCallbacks` | List<ToolCallback> | [] | 工具回调列表 |
| `toolNames` | Set<String> | [] | 工具名称集合 |
| `internalToolExecutionEnabled` | Boolean | - | 是否启用内部工具执行 |

**详细说明**:

```java
/**
 * tools - 工具列表
 * 
 * 用途：定义模型可以调用的函数
 * 
 * 注意：即使提供多个函数，模型也只会选择一个生成结果
 * 
 * 示例：
 * [
 *   {
 *     "type": "function",
 *     "function": {
 *       "name": "getWeather",
 *       "description": "获取天气信息",
 *       "parameters": {...}
 *     }
 *   }
 * ]
 */
@JsonProperty("tools")
private List<DashScopeApi.FunctionTool> tools;

/**
 * toolChoice - 工具选择策略
 * 
 * 可选值：
 * - "none"：不调用任何工具（tools 为空时的默认值）
 * - "auto"：模型自主决定是否调用工具（tools 不为空时的默认值）
 * - {"type": "function", "function": {"name": "user_function"}}：
 *   指定调用特定工具
 */
@JsonProperty("tool_choice")
private Object toolChoice;

/**
 * parallelToolCalls - 并行工具调用
 * 
 * 用途：是否启用并行调用多个工具
 * 默认：false（顺序调用）
 * 
 * 使用场景：
 * - 多个独立的工具调用：启用并行
 * - 有依赖关系的调用：禁用并行
 */
@JsonProperty("parallel_tool_calls")
private Boolean parallelToolCalls;

/**
 * toolCallbacks - 工具回调
 * 
 * Spring AI 扩展：用于注册 Java 方法作为工具
 * 
 * 注意：这是 Spring AI 框架的扩展，不会发送到 DashScope API
 */
@JsonIgnore
private List<ToolCallback> toolCallbacks = new ArrayList<>();

/**
 * toolNames - 工具名称
 * 
 * Spring AI 扩展：运行时解析的工具名称
 */
@JsonIgnore
private Set<String> toolNames = new HashSet<>();

/**
 * internalToolExecutionEnabled - 内部工具执行
 * 
 * Spring AI 扩展：是否在 ChatModel 内部执行工具生命周期
 * 
 * - true：ChatModel 自动执行工具并发起第二轮调用
 * - false：需要手动处理工具调用结果
 */
@JsonIgnore
private Boolean internalToolExecutionEnabled;
```

#### 2.3.4 多模态参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `vlHighResolutionImages` | Boolean | - | 是否启用高分辨率图像 |
| `multiModel` | Boolean | false | 是否涉及多模型 |

**详细说明**:

```java
/**
 * vlHighResolutionImages - 高分辨率图像
 * 
 * 用途：将 token 限制更改为 16384（仅适用于视觉语言模型）
 * 
 * 支持的模型：
 * - qwen-vl-max
 * - qwen-vl-max-0809
 * - qwen-vl-plus-0809
 */
@JsonProperty("vl_high_resolution_images")
private Boolean vlHighResolutionImages;

/**
 * multiModel - 多模型请求
 * 
 * 用途：指示请求是否涉及多个模型
 * 默认：false
 */
@JsonProperty("multi_model")
private Boolean multiModel = false;
```

#### 2.3.5 停止序列

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `stop` | List<Object> | - | 停止序列（字符串或 token_ids） |

**详细说明**:

```java
/**
 * stop - 停止参数
 * 
 * 用途：精确控制内容生成过程，自动停止生成
 * 
 * 支持两种类型：
 * 1. 字符串数组：["Hello", "World"]
 * 2. token_ids 数组：[37763, 367]
 * 
 * 注意：在 list 模式下，字符串和 token_ids 不能混用
 * 
 * 示例：
 * - stop = ["Hello"]：生成即将包含 "Hello" 时停止
 * - stop = [37763, 367]：生成即将包含 "Observation" 时停止
 */
@JsonProperty("stop")
private List<Object> stop;
```

### 2.4 Builder 模式

DashScopeChatOptions 提供了流式的 Builder API：

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withTemperature(0.7)
    .withTopP(0.8)
    .withTopK(50)
    .withMaxToken(2000)
    .withEnableSearch(true)
    .withRepetitionPenalty(1.2)
    .withResponseFormat(new DashScopeResponseFormat("json_object"))
    .withEnableThinking(true)
    .withThinkingBudget(500)
    .withParallelToolCalls(true)
    .build();
```

### 2.5 配置层级

```
应用配置
    ↓
application.yml
    ↓
DashScopeAutoConfiguration（自动配置）
    ↓
defaultOptions（全局默认配置）
    ↓
ChatClient 或 ChatModel 调用时的 options（请求级配置）
    ↓
合并后的最终配置
```

**配置优先级**（从高到低）：
1. 请求级配置（call 方法传入的 options）
2. ChatClient 的 defaultOptions
3. Spring Boot 配置文件（application.yml）
4. DashScopeChatOptions 的默认值

**示例**:

```java
// 1. 全局配置（application.yml）
spring:
  ai:
    dashscope:
      chat:
        options:
          model: qwen-plus
          temperature: 0.7

// 2. ChatClient 默认配置
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultOptions(
        DashScopeChatOptions.builder()
            .withTemperature(0.8)  // 覆盖全局配置
            .build()
    )
    .build();

// 3. 请求级配置
chatClient.prompt()
    .user("你好")
    .options(
        DashScopeChatOptions.builder()
            .withTemperature(0.9)  // 覆盖 ChatClient 配置
            .withMaxToken(1000)
            .build()
    )
    .call();
```

### 2.6 完整配置示例

#### 2.6.1 基础对话配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withTemperature(0.7)
    .withTopP(0.8)
    .withMaxToken(2000)
    .build();

ChatResponse response = chatModel.call(
    new Prompt("介绍 Spring AI Alibaba", options)
);
```

#### 2.6.2 创意写作配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withTemperature(1.2)      // 更高的随机性
    .withTopP(0.95)            // 更宽的采样范围
    .withRepetitionPenalty(1.3)  // 减少重复
    .build();

ChatResponse response = chatModel.call(
    new Prompt("写一首关于春天的诗", options)
);
```

#### 2.6.3 精确任务配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-max")
    .withTemperature(0.1)      // 更低的随机性
    .withTopP(0.7)            // 更窄的采样范围
    .withSeed(42)             // 固定随机种子
    .build();

ChatResponse response = chatModel.call(
    new Prompt("计算 123 + 456", options)
);
```

#### 2.6.4 联网搜索配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withEnableSearch(true)
    .withSearchOptions(
        new DashScopeApi.SearchOptions()
            .setSearchStrategy("time_priority")  // 时间优先
            .setTimeRange("week")                 // 最近一周
    )
    .build();

ChatResponse response = chatModel.call(
    new Prompt("今天的新闻有哪些？", options)
);
```

#### 2.6.5 Function Calling 配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withToolCallbacks(List.of(weatherToolCallback))
    .withInternalToolExecutionEnabled(true)  // 自动执行工具
    .withParallelToolCalls(false)           // 顺序执行
    .build();

ChatResponse response = chatModel.call(
    new Prompt("北京今天天气怎么样？", options)
);
```

#### 2.6.6 结构化输出配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withResponseFormat(new DashScopeResponseFormat("json_object"))
    .build();

ChatResponse response = chatModel.call(
    new Prompt(
        "请以 JSON 格式返回以下信息：姓名、年龄、职业", 
        options
    )
);
```

#### 2.6.7 思维链配置

```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withEnableThinking(true)
    .withThinkingBudget(1000)  // 限制思维过程长度
    .build();

ChatResponse response = chatModel.call(
    new Prompt("解释相对论的基本原理", options)
);
```

---

## DashScopeApi - HTTP 客户端封装

### 3.1 设计目的

`DashScopeApi` 是对阿里云百炼（DashScope）HTTP API 的封装，提供了：

1. **统一的 HTTP 客户端**：RestClient（同步）和 WebClient（流式）
2. **API 端点管理**：统一管理所有 API 路径
3. **请求/响应模型**：定义所有数据结构
4. **错误处理**：统一的错误处理机制
5. **认证管理**：API Key 和 Workspace ID 管理

### 3.2 类结构

```java
public class DashScopeApi {
    
    // HTTP 客户端
    private final RestClient restClient;
    private final WebClient webClient;
    
    // 配置信息
    private final String baseUrl;
    private final ApiKey apiKey;
    private final String completionsPath;
    private final String embeddingsPath;
    private final MultiValueMap<String, String> headers;
    private final ResponseErrorHandler responseErrorHandler;
}
```

### 3.3 核心功能

#### 3.3.1 聊天完成（同步）

```java
/**
 * 同步调用聊天完成 API
 * 
 * @param request 请求对象
 * @return 响应实体
 */
public ResponseEntity<ChatCompletion> chatCompletionEntity(
        ChatCompletionRequest request) {
    
    return this.restClient.post()
        .uri(this.completionsPath)
        .body(request)
        .retrieve()
        .toEntity(ChatCompletion.class);
}
```

**请求格式**:

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
    "top_p": 0.8,
    "max_tokens": 2000
  }
}
```

**响应格式**:

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
    "output_tokens": 12,
    "total_tokens": 15
  },
  "request_id": "uuid-xxx"
}
```

#### 3.3.2 聊天完成（流式）

```java
/**
 * 流式调用聊天完成 API
 * 
 * @param request 请求对象
 * @return 响应流
 */
public Flux<ChatCompletionChunk> chatCompletionStream(
        ChatCompletionRequest request) {
    
    return this.webClient.post()
        .uri(this.completionsPath)
        .bodyValue(request)
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<
            ServerSentEvent<String>>() {})
        .takeUntil(chunk -> 
            SSE_DONE_PREDICATE.test(chunk.data()))
        .filter(chunk -> 
            chunk.data() != null && 
            !SSE_DONE_PREDICATE.test(chunk.data()))
        .map(chunk -> 
            ModelOptionsUtils.jsonToObject(
                chunk.data(), 
                ChatCompletionChunk.class
            ));
}
```

**SSE 响应格式**:

```
data: {"output":{"choices":[{"message":{"content":"你"}}]}}

data: {"output":{"choices":[{"message":{"content":"好"}}]}}

data: {"output":{"choices":[{"message":{"content":"！"}}]}}

data: [DONE]
```

#### 3.3.3 Embedding（向量化）

```java
/**
 * 调用向量化 API
 * 
 * @param request 请求对象
 * @return 响应实体
 */
public ResponseEntity<EmbeddingResponse> embeddings(
        EmbeddingRequest request) {
    
    return this.restClient.post()
        .uri(this.embeddingsPath)
        .body(request)
        .retrieve()
        .toEntity(EmbeddingResponse.class);
}
```

### 3.4 Builder 模式

DashScopeApi 使用 Builder 模式进行构建：

```java
DashScopeApi api = DashScopeApi.builder()
    .baseUrl("https://dashscope.aliyuncs.com/api/v1")
    .apiKey("sk-xxxxx")
    .workSpaceId("workspace-123")
    .build();
```

### 3.5 多 HTTP 客户端设计

```
┌─────────────────────────────────────────────────────────┐
│                    DashScopeApi                          │
│                                                          │
│  ┌──────────────────┐       ┌──────────────────┐       │
│  │   RestClient     │       │   WebClient      │       │
│  │   (同步调用)      │       │   (流式调用)      │       │
│  └──────────────────┘       └──────────────────┘       │
│          ↓                          ↓                    │
│  ┌──────────────────┐       ┌──────────────────┐       │
│  │  HTTP POST       │       │  SSE Stream      │       │
│  │  阻塞等待响应     │       │  逐块接收数据     │       │
│  └──────────────────┘       └──────────────────┘       │
└─────────────────────────────────────────────────────────┘
```

**为什么需要两个客户端？**

1. **RestClient**：
   - 同步阻塞式调用
   - 适合简单的请求-响应场景
   - 返回完整响应

2. **WebClient**：
   - 非阻塞响应式调用
   - 适合流式场景（SSE）
   - 返回 Flux<> 流

### 3.6 请求/响应模型

#### 3.6.1 ChatCompletionRequest

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletionRequest(
    @JsonProperty("model") String model,
    @JsonProperty("input") Input input,
    @JsonProperty("parameters") Parameters parameters
) {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Input(
        @JsonProperty("messages") List<Message> messages
    ) {}
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Parameters(
        @JsonProperty("result_format") String resultFormat,
        @JsonProperty("temperature") Double temperature,
        @JsonProperty("top_p") Double topP,
        @JsonProperty("top_k") Integer topK,
        @JsonProperty("max_tokens") Integer maxTokens,
        @JsonProperty("stop") List<Object> stop,
        @JsonProperty("enable_search") Boolean enableSearch,
        @JsonProperty("tools") List<FunctionTool> tools,
        @JsonProperty("tool_choice") Object toolChoice,
        @JsonProperty("incremental_output") Boolean incrementalOutput
    ) {}
}
```

#### 3.6.2 ChatCompletion

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletion(
    @JsonProperty("output") Output output,
    @JsonProperty("usage") Usage usage,
    @JsonProperty("request_id") String requestId
) {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Output(
        @JsonProperty("choices") List<Choice> choices
    ) {}
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Choice(
        @JsonProperty("finish_reason") String finishReason,
        @JsonProperty("message") Message message
    ) {}
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(
        @JsonProperty("role") String role,
        @JsonProperty("content") String content,
        @JsonProperty("tool_calls") List<ToolCall> toolCalls
    ) {}
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Usage(
        @JsonProperty("input_tokens") Integer inputTokens,
        @JsonProperty("output_tokens") Integer outputTokens,
        @JsonProperty("total_tokens") Integer totalTokens
    ) {}
}
```

### 3.7 错误处理

```java
public class DashScopeApi {
    
    private final ResponseErrorHandler responseErrorHandler;
    
    public DashScopeApi(..., ResponseErrorHandler responseErrorHandler) {
        // 使用自定义错误处理器
        this.restClient = restClientBuilder
            .defaultStatusHandler(responseErrorHandler)
            .build();
    }
}
```

**默认错误处理器**:

```java
RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER
```

**自定义错误处理**:

```java
ResponseErrorHandler customErrorHandler = new ResponseErrorHandler() {
    
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }
    
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String responseBody = new String(
            response.getBody().readAllBytes(), 
            StandardCharsets.UTF_8
        );
        
        if (statusCode.value() == 401) {
            throw new DashScopeException("API Key 无效");
        } else if (statusCode.value() == 429) {
            throw new DashScopeException("请求频率超限");
        } else {
            throw new DashScopeException(
                "API 调用失败: " + responseBody
            );
        }
    }
};
```

### 3.8 认证机制

#### 3.8.1 API Key 认证

```java
Consumer<HttpHeaders> finalHeaders = h -> {
    if (!(apiKey instanceof NoopApiKey)) {
        h.setBearerAuth(apiKey.getValue());
    }
    h.setContentType(MediaType.APPLICATION_JSON);
    h.addAll(headers);
};
```

**HTTP 请求头**:

```
Authorization: Bearer sk-xxxxxxxxxxxxxxxx
Content-Type: application/json
```

#### 3.8.2 Workspace ID

```java
if (StringUtils.hasText(workSpaceId)) {
    this.headers.add(
        DashScopeApiConstants.HEADER_WORK_SPACE_ID, 
        workSpaceId
    );
}
```

**HTTP 请求头**:

```
X-DashScope-WorkSpace: workspace-123
```

### 3.9 完整使用示例

```java
// 1. 创建 API 客户端
DashScopeApi api = DashScopeApi.builder()
    .baseUrl("https://dashscope.aliyuncs.com/api/v1")
    .apiKey("sk-xxxxx")
    .workSpaceId("workspace-123")
    .build();

// 2. 构建请求
ChatCompletionRequest request = new ChatCompletionRequest(
    "qwen-plus",
    new ChatCompletionRequest.Input(
        List.of(new Message("user", "你好"))
    ),
    new ChatCompletionRequest.Parameters(
        "message",
        0.7,
        0.8,
        null,
        2000,
        null,
        false,
        null,
        null,
        true
    )
);

// 3. 发送请求
ResponseEntity<ChatCompletion> response = 
    api.chatCompletionEntity(request);

// 4. 处理响应
ChatCompletion completion = response.getBody();
String content = completion.output()
    .choices()
    .get(0)
    .message()
    .content();

System.out.println("回复: " + content);
```

---

## DashScopeChatModel - ChatModel 实现

### 4.1 设计目的

`DashScopeChatModel` 是 Spring AI 标准接口 `ChatModel` 的具体实现，将 Spring AI 的调用转换为 DashScope API 调用。

### 4.2 类定义

```java
public class DashScopeChatModel implements ChatModel, StreamingChatModel {
    
    // 核心依赖
    private final DashScopeApi dashScopeApi;
    private final DashScopeChatOptions defaultOptions;
    private final RetryTemplate retryTemplate;
    private final ObservationRegistry observationRegistry;
    
    // Function Calling 支持
    private final ToolCallingManager toolCallingManager;
}
```

### 4.3 核心方法

#### 4.3.1 call() - 同步调用

```java
@Override
public ChatResponse call(Prompt prompt) {
    // 1. 合并配置
    ChatCompletionRequest request = createRequest(prompt, false);
    
    // 2. 使用重试模板执行
    ResponseEntity<ChatCompletion> completionEntity = 
        this.retryTemplate.execute(ctx -> {
            return this.dashScopeApi.chatCompletionEntity(request);
        });
    
    // 3. 转换响应
    ChatCompletion chatCompletion = completionEntity.getBody();
    return toChatResponse(chatCompletion);
}
```

#### 4.3.2 stream() - 流式调用

```java
@Override
public Flux<ChatResponse> stream(Prompt prompt) {
    // 1. 构建流式请求
    ChatCompletionRequest request = createRequest(prompt, true);
    
    // 2. 调用流式 API
    return this.retryTemplate.execute(ctx -> {
        return this.dashScopeApi.chatCompletionStream(request);
    })
    // 3. 转换每个 chunk
    .map(chunk -> toChatResponse(chunk))
    // 4. 错误处理
    .onErrorResume(throwable -> {
        return Flux.error(new RuntimeException("Stream failed", throwable));
    });
}
```

### 4.4 Function Calling 支持

```java
@Override
public ChatResponse call(Prompt prompt) {
    ChatResponse response = callDashScope(prompt);
    
    // 检查是否有工具调用
    if (hasToolCalls(response)) {
        // 执行工具
        List<ToolResponseMessage> toolResults = 
            executeTools(response.getResult().getOutput().getToolCalls());
        
        // 构建新的 Prompt（包含工具结果）
        List<Message> newMessages = new ArrayList<>();
        newMessages.addAll(prompt.getInstructions());
        newMessages.add(response.getResult().getOutput());
        newMessages.addAll(toolResults);
        
        // 发起第二次调用
        Prompt secondPrompt = new Prompt(newMessages, prompt.getOptions());
        return call(secondPrompt);
    }
    
    return response;
}
```

### 4.5 重试机制

```java
private final RetryTemplate retryTemplate = RetryTemplate.builder()
    .maxAttempts(3)
    .fixedBackoff(1000)
    .retryOn(IOException.class)
    .retryOn(TimeoutException.class)
    .build();
```

### 4.6 可观测性

```java
@Override
public ChatResponse call(Prompt prompt) {
    return Observation.createNotStarted(
        "chat.call", 
        observationRegistry
    )
    .lowCardinalityKeyValue("model", "qwen-plus")
    .observe(() -> {
        // 记录输入
        Span span = tracer.currentSpan();
        span.tag("input", prompt.getContents());
        
        // 执行调用
        ChatResponse response = callDashScope(prompt);
        
        // 记录输出
        span.tag("output", response.getResult().getOutput().getContent());
        span.tag("tokens", String.valueOf(
            response.getMetadata().getUsage().getTotalTokens()
        ));
        
        return response;
    });
}
```

---

## 设计对比：Spring AI vs Spring AI Alibaba

### 5.1 接口层面对比

| 接口 | Spring AI | Spring AI Alibaba |
|------|-----------|-------------------|
| ChatModel | 标准接口 | DashScopeChatModel 实现 |
| ChatOptions | 通用配置 | DashScopeChatOptions 扩展 |
| API Client | 无（各厂商自行实现） | DashScopeApi 封装 |

### 5.2 配置参数对比

| 参数 | Spring AI | DashScope | 说明 |
|------|-----------|-----------|------|
| model | ✅ | ✅ | 模型名称 |
| temperature | ✅ | ✅ | 随机性控制 |
| topP | ✅ | ✅ | 核采样 |
| topK | ✅ | ✅ | Top-K 采样 |
| maxTokens | ✅ | ✅ | 最大 Token 数 |
| stop | ✅ | ✅ | 停止序列 |
| **enableSearch** | ❌ | ✅ | DashScope 特有 |
| **repetitionPenalty** | ❌ | ✅ | DashScope 特有 |
| **incrementalOutput** | ❌ | ✅ | DashScope 特有 |
| **enableThinking** | ❌ | ✅ | DashScope 特有 |

### 5.3 架构对比

**Spring AI 标准架构**:

```
Application Code
    ↓
ChatModel Interface
    ↓
Vendor Implementation
    ↓
HTTP Client (厂商自定义)
    ↓
AI Service API
```

**Spring AI Alibaba 架构**:

```
Application Code
    ↓
ChatModel Interface
    ↓
DashScopeChatModel
    ↓
DashScopeApi (统一封装)
    ├── RestClient (同步)
    └── WebClient (流式)
    ↓
DashScope API
```

---

## 完整使用示例

### 6.1 Spring Boot 自动配置

**application.yml**:

```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      base-url: https://dashscope.aliyuncs.com/api/v1
      workspace-id: ${WORKSPACE_ID}
      chat:
        options:
          model: qwen-plus
          temperature: 0.7
          top-p: 0.8
          max-tokens: 2000
          enable-search: false
```

**自动配置**:

```java
@Bean
@ConditionalOnMissingBean
public DashScopeApi dashScopeApi(DashScopeProperties properties) {
    return DashScopeApi.builder()
        .baseUrl(properties.getBaseUrl())
        .apiKey(properties.getApiKey())
        .workSpaceId(properties.getWorkspaceId())
        .build();
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
                .withTopP(properties.getChat().getOptions().getTopP())
                .withMaxToken(properties.getChat().getOptions().getMaxTokens())
                .build()
        )
        .build();
}
```

### 6.2 直接使用 ChatModel

```java
@Service
public class ChatService {
    
    private final ChatModel chatModel;
    
    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    
    public String simpleChat(String message) {
        // 使用默认配置
        ChatResponse response = chatModel.call(
            new Prompt(message)
        );
        
        return response.getResult().getOutput().getContent();
    }
    
    public String chatWithOptions(String message) {
        // 自定义配置
        DashScopeChatOptions options = DashScopeChatOptions.builder()
            .withModel("qwen-max")
            .withTemperature(0.9)
            .withEnableSearch(true)
            .build();
        
        ChatResponse response = chatModel.call(
            new Prompt(message, options)
        );
        
        return response.getResult().getOutput().getContent();
    }
    
    public Flux<String> streamChat(String message) {
        // 流式调用
        return chatModel.stream(new Prompt(message))
            .map(response -> response.getResult().getOutput().getContent());
    }
}
```

### 6.3 使用 ChatClient（推荐）

```java
@Configuration
public class ChatClientConfig {
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .withModel("qwen-plus")
                    .withTemperature(0.7)
                    .build()
            )
            .build();
    }
}

@Service
public class ChatService {
    
    private final ChatClient chatClient;
    
    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    public String chat(String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
    
    public String chatWithOptions(String message) {
        return chatClient.prompt()
            .user(message)
            .options(
                DashScopeChatOptions.builder()
                    .withModel("qwen-max")
                    .withTemperature(0.9)
                    .build()
            )
            .call()
            .content();
    }
    
    public Flux<String> streamChat(String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }
}
```

---

## 最佳实践

### 7.1 配置管理

#### ✅ 推荐：分层配置

```yaml
# 全局默认配置
spring:
  ai:
    dashscope:
      chat:
        options:
          model: qwen-plus
          temperature: 0.7
          top-p: 0.8
```

```java
// 创建不同用途的 ChatClient
@Configuration
public class ChatClientConfig {
    
    // 日常对话 Client
    @Bean
    public ChatClient casualChatClient(ChatClient.Builder builder) {
        return builder
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .withTemperature(0.8)
                    .build()
            )
            .build();
    }
    
    // 精确任务 Client
    @Bean
    public ChatClient preciseTaskClient(ChatClient.Builder builder) {
        return builder
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .withModel("qwen-max")
                    .withTemperature(0.1)
                    .withSeed(42)
                    .build()
            )
            .build();
    }
    
    // 创意写作 Client
    @Bean
    public ChatClient creativeWritingClient(ChatClient.Builder builder) {
        return builder
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .withTemperature(1.2)
                    .withRepetitionPenalty(1.3)
                    .build()
            )
            .build();
    }
}
```

#### ❌ 不推荐：请求级覆盖所有配置

```java
// 每次调用都传入完整配置（繁琐且易出错）
chatClient.prompt()
    .user(message)
    .options(
        DashScopeChatOptions.builder()
            .withModel("qwen-plus")
            .withTemperature(0.7)
            .withTopP(0.8)
            .withMaxToken(2000)
            // ... 很多配置
            .build()
    )
    .call();
```

### 7.2 性能优化

#### ✅ 推荐：使用流式调用

```java
// 对于长文本生成，使用流式调用
public Flux<String> generateLongText(String prompt) {
    return chatClient.prompt()
        .user(prompt)
        .stream()
        .content();
}
```

#### ✅ 推荐：合理设置 maxTokens

```java
// 根据实际需求设置合理的 maxTokens
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withMaxToken(500)  // 简短回答
    .build();
```

#### ✅ 推荐：缓存常见查询

```java
@Cacheable("chat-responses")
public String getCachedResponse(String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
}
```

### 7.3 错误处理

#### ✅ 推荐：优雅的错误处理

```java
public String safeChat(String message) {
    try {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    } catch (DashScopeException e) {
        if (e.getErrorCode() == ErrorCodeEnum.RATE_LIMIT) {
            // 处理频率限制
            return "请求过于频繁，请稍后再试";
        } else if (e.getErrorCode() == ErrorCodeEnum.INVALID_API_KEY) {
            // 处理 API Key 无效
            log.error("API Key 无效，请检查配置");
            return "系统配置错误，请联系管理员";
        } else {
            // 其他错误
            log.error("AI 调用失败", e);
            return "服务暂时不可用，请稍后再试";
        }
    }
}
```

### 7.4 可观测性

#### ✅ 推荐：启用 Observation

```yaml
spring:
  ai:
    alibaba:
      observation:
        enabled: true
```

#### ✅ 推荐：自定义指标

```java
@Component
public class ChatMetricsCollector {
    
    private final MeterRegistry meterRegistry;
    
    public ChatMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    public String chatWithMetrics(String message) {
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            String response = chatClient.prompt()
                .user(message)
                .call()
                .content();
            
            // 记录成功指标
            sample.stop(Timer.builder("chat.duration")
                .tag("status", "success")
                .register(meterRegistry));
            
            return response;
        } catch (Exception e) {
            // 记录失败指标
            sample.stop(Timer.builder("chat.duration")
                .tag("status", "failure")
                .register(meterRegistry));
            
            throw e;
        }
    }
}
```

---

## 总结

### 核心设计理念

1. **DashScopeChatOptions**：
   - 实现 Spring AI 标准接口
   - 扩展 DashScope 特有功能
   - 提供流式 Builder API
   - 支持配置分层管理

2. **DashScopeApi**：
   - 统一封装 HTTP 调用
   - 同时支持同步和流式
   - 规范请求/响应模型
   - 统一错误处理机制

3. **DashScopeChatModel**：
   - 实现 Spring AI ChatModel 接口
   - 内置重试和可观测性
   - 完整支持 Function Calling
   - 自动处理工具调用生命周期

### 设计优势

1. **标准化**：遵循 Spring AI 标准接口，易于切换不同的 AI 服务
2. **扩展性**：支持 DashScope 的所有特性
3. **易用性**：提供流式 Builder API，配置简单
4. **可靠性**：内置重试机制和错误处理
5. **可观测**：完整的 OpenTelemetry 集成

---

**文档版本**: v1.0.0  
**最后更新**: 2025-10-05  
**作者**: Spring AI Alibaba Team

