# Spring AI Alibaba Studio 深度解析

## 一、模块定位

`spring-ai-alibaba-studio` 是 Spring AI Alibaba 项目的**可视化开发平台**,提供了一个完整的 Web UI 界面,帮助开发者:

- 🎨 **可视化交互**: 通过 Chat UI 与 Agent 实时对话
- 🐛 **调试工具**: 查看消息流、状态变化、Token 使用情况
- 📊 **会话管理**: Thread 管理、历史记录查看
- 🔧 **人机协同**: Human-in-the-Loop 工具调用确认
- 🚀 **快速集成**: 嵌入式模式 & 独立模式双支持

---

## 二、整体架构

Studio 采用 **前后端分离架构**,后端基于 Spring Boot,前端基于 Next.js:

```
┌─────────────────────────────────────────────────────┐
│              Spring AI Alibaba Studio               │
├───────────────────────┬─────────────────────────────┤
│     后端 (Spring Boot) │      前端 (Next.js)          │
│                       │                             │
│  ┌─────────────────┐  │  ┌────────────────────────┐ │
│  │  AgentController│  │  │   StreamProvider       │ │
│  │  - listApps()   │  │  │   - sendMessage()      │ │
│  └────────┬────────┘  │  │   - resumeFeedback()   │ │
│           │           │  └──────────┬─────────────┘ │
│  ┌────────▼─────────┐ │             │               │
│  │ExecutionController│ │  ┌──────────▼────────────┐ │
│  │  - /run_sse      │ │  │  spring-ai-api.ts     │ │
│  │  - /resume_sse   │◄─┼──┤  - runAgentStream()   │ │
│  └────────┬─────────┘ │  │  - resumeAgentStream()│ │
│           │           │  └───────────────────────┘ │
│  ┌────────▼─────────┐ │                            │
│  │  ThreadController│ │  ┌────────────────────────┐ │
│  │  - listThreads() │◄─┼──┤   ThreadProvider      │ │
│  │  - createThread()│  │  │   - createThread()    │ │
│  └────────┬─────────┘ │  │   - listThreads()     │ │
│           │           │  └───────────────────────┘ │
│  ┌────────▼─────────┐ │                            │
│  │   AgentLoader   │  │  ┌────────────────────────┐ │
│  │  - loadAgent()  │  │  │  React Components      │ │
│  │  - listAgents() │  │  │  - ChatInterface       │ │
│  └─────────────────┘  │  │  - MessageList         │ │
│                       │  │  - ToolConfirm         │ │
└───────────────────────┴─────────────────────────────┘
          │                          │
          └──────────SSE Stream──────┘
                (Server-Sent Events)
```

---

## 三、后端核心组件

### 3.1 AgentLoader - Agent 加载机制

`AgentLoader` 是 Studio 的核心抽象接口,负责加载和管理 Agent 实例:

#### **接口定义**

```java
public interface AgentLoader {
    @Nonnull
    List<String> listAgents();  // 列出所有可用的 Agent
    
    BaseAgent loadAgent(String name);  // 根据名称加载 Agent
}
```

#### **实现方式**

**1. AgentStaticLoader - 静态加载器**

适用于硬编码 Agent 的场景:

```java
// 创建并注册多个 Agent
AgentStaticLoader loader = new AgentStaticLoader(
    chatBotAgent,
    researchAgent,
    codeAssistant
);

// Spring Boot 中作为 Bean 注册
@Bean
public AgentLoader agentLoader() {
    return new AgentStaticLoader(myAgent1, myAgent2);
}
```

**特点**:
- ✅ 简单直接,适合快速原型
- ✅ 线程安全 (ConcurrentHashMap)
- ❌ 不支持热更新

**2. ConfigAgentWatcher - 配置文件监听器**

监听 YAML 配置文件变化,支持动态加载:

```java
// 监听配置目录
ConfigAgentWatcher watcher = new ConfigAgentWatcher();
watcher.watch(agentDirPath, (path) -> {
    // 配置文件变化时重新加载 Agent
    BaseAgent updatedAgent = loadFromConfig(path);
});
watcher.start();
```

**核心特性**:
- 🔍 **轮询检测**: 每 2 秒扫描一次 YAML 文件
- 📝 **跟踪修改时间**: 通过 `lastModifiedTime` 判断变化
- 🔄 **支持新增/修改/删除**: 全面的文件系统事件支持
- 🌍 **跨平台兼容**: 不依赖原生文件系统事件

**使用场景**:
```yaml
# agent_config/research_agent.yaml
name: research_agent
description: "深度研究助手"
systemPrompt: "你是一个专业的研究助手..."
tools:
  - web_search
  - document_reader
```

当 YAML 文件修改后,Watcher 自动触发回调重新加载 Agent。

---

### 3.2 Controller 层 - API 端点

#### **AgentController - Agent 管理**

提供 Agent 列表查询:

```java
@RestController
public class AgentController {
    @GetMapping("/list-apps")
    public List<String> listApps() {
        return agentProvider.listAgents()
            .stream()
            .sorted()
            .collect(toList());
    }
}
```

**API 示例**:
```http
GET /list-apps
Response: ["chat_bot", "research_agent", "code_assistant"]
```

---

#### **ExecutionController - Agent 执行**

提供流式和非流式执行接口:

**核心方法**:

**1. `/run_sse` - 启动 Agent 流式执行**

```java
@PostMapping(value = "/run_sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<ServerSentEvent<String>> agentRunSse(@RequestBody AgentRunRequest request) {
    BaseAgent agent = agentLoader.loadAgent(request.appName);
    
    RunnableConfig config = RunnableConfig.builder()
        .threadId(request.threadId)
        .addMetadata("user_id", request.userId)
        .build();
    
    return executeAgent(request.newMessage.toUserMessage(), agent, config);
}
```

**请求格式**:
```json
{
  "appName": "research_agent",
  "userId": "user-001",
  "threadId": "thread-123",
  "newMessage": {
    "messageType": "user",
    "content": "介绍一下 Spring AI",
    "metadata": {}
  }
}
```

**响应格式 (SSE)**:
```
data: {"node":"llm_node","agent":"research_agent","message":{"messageType":"assistant","content":"Spring AI 是..."},"chunk":"Spring","tokenUsage":null}

data: {"node":"llm_node","agent":"research_agent","message":null,"chunk":" AI","tokenUsage":null}

data: {"node":"llm_node","agent":"research_agent","message":null,"chunk":" 是...","tokenUsage":null}
```

**2. `/resume_sse` - 恢复执行 (Human-in-the-Loop)**

当 Agent 需要人工确认工具调用时使用:

```java
@PostMapping(value = "/resume_sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<ServerSentEvent<String>> agentResumeSse(@RequestBody AgentResumeRequest request) {
    BaseAgent agent = agentLoader.loadAgent(request.appName);
    
    // 构建 InterruptionMetadata
    InterruptionMetadata.Builder metadataBuilder = InterruptionMetadata.builder();
    for (ToolFeedback feedback : request.toolFeedbacks) {
        metadataBuilder.addToolFeedback(
            new InterruptionMetadata.ToolFeedback(
                feedback.getId(),
                feedback.getName(),
                feedback.getArguments(),
                feedback.getResult(),  // APPROVED/REJECTED/EDITED
                feedback.getDescription()
            )
        );
    }
    
    RunnableConfig config = RunnableConfig.builder()
        .threadId(request.threadId)
        .addHumanFeedback(metadataBuilder.build())
        .build();
    
    return executeAgent(null, agent, config);
}
```

**请求格式**:
```json
{
  "appName": "research_agent",
  "threadId": "thread-123",
  "toolFeedbacks": [
    {
      "id": "call_123",
      "name": "web_search",
      "arguments": "{\"query\":\"Spring AI\"}",
      "result": "APPROVED",
      "description": "用户已确认"
    }
  ]
}
```

**执行流程**:

```
用户输入消息
     ↓
Agent 请求工具调用
     ↓
前端显示确认 UI ← InterruptionMetadata
     ↓
用户审核 (批准/拒绝/修改)
     ↓
调用 /resume_sse 继续执行
     ↓
Agent 执行工具 & 返回结果
```

---

#### **ThreadController - 会话管理**

提供 Thread (会话) 的 CRUD 操作:

```java
@RestController
public class ThreadController {
    // 列出用户的所有会话
    @GetMapping("/apps/{appName}/users/{userId}/threads")
    public ListThreadsResponse listThreads(
        @PathVariable String appName,
        @PathVariable String userId
    );
    
    // 创建新会话 (自动生成 ID)
    @PostMapping("/apps/{appName}/users/{userId}/threads")
    public Thread createThread(
        @PathVariable String appName,
        @PathVariable String userId,
        @RequestBody Map<String, Object> initialState
    );
    
    // 获取会话详情
    @GetMapping("/apps/{appName}/users/{userId}/threads/{threadId}")
    public Thread getThread(
        @PathVariable String appName,
        @PathVariable String userId,
        @PathVariable String threadId
    );
    
    // 删除会话
    @DeleteMapping("/apps/{appName}/users/{userId}/threads/{threadId}")
    public void deleteThread(
        @PathVariable String appName,
        @PathVariable String userId,
        @PathVariable String threadId
    );
}
```

**Thread 数据结构**:
```json
{
  "thread_id": "thread-123",
  "appName": "research_agent",
  "userId": "user-001",
  "values": {
    "messages": [
      {"messageType": "user", "content": "你好"},
      {"messageType": "assistant", "content": "你好!有什么可以帮助你的?"}
    ]
  },
  "metadata": {
    "createdAt": "2025-01-15T10:00:00Z"
  }
}
```

---

### 3.3 流式输出处理

Studio 的核心能力之一是**实时流式输出**,在 `executeAgent` 方法中实现:

#### **流式输出逻辑**

```java
private Flux<ServerSentEvent<String>> executeAgent(
    UserMessage userMessage, 
    BaseAgent agent, 
    RunnableConfig config
) {
    // 获取 Agent 流式输出
    Flux<NodeOutput> agentStream = agent.stream(userMessage, config);
    
    return agentStream.map(nodeOutput -> {
        String node = nodeOutput.node();
        String agentName = nodeOutput.agent();
        Usage tokenUsage = nodeOutput.tokenUsage();
        
        AgentRunResponse response;
        
        // 处理流式输出
        if (nodeOutput instanceof StreamingOutput<?> streamingOutput) {
            Message message = streamingOutput.message();
            
            if (message instanceof AssistantMessage assistantMessage) {
                if (assistantMessage.hasToolCalls()) {
                    // 工具调用消息
                    response = new AgentRunResponse(
                        node, agentName, assistantMessage, tokenUsage, ""
                    );
                } else {
                    // 普通文本消息,包含 chunk
                    response = new AgentRunResponse(
                        node, agentName, assistantMessage, tokenUsage, 
                        assistantMessage.getText()
                    );
                }
            } else {
                response = new AgentRunResponse(
                    node, agentName, message, tokenUsage, ""
                );
            }
        }
        // 处理人机交互中断
        else if (nodeOutput instanceof InterruptionMetadata interruptionMetadata) {
            ToolRequestConfirmMessageDTO toolRequestMessage = 
                MessageDTO.MessageDTOFactory.fromInterruptionMetadata(interruptionMetadata);
            response = new AgentRunResponse(
                node, agentName, toolRequestMessage, tokenUsage, ""
            );
        }
        
        // 序列化为 JSON
        String jsonData = mapper.writeValueAsString(response);
        return ServerSentEvent.<String>builder().data(jsonData).build();
    })
    .onErrorResume(error -> {
        // 错误处理
        String errorJson = String.format(
            "{\"error\":true,\"errorType\":\"%s\",\"errorMessage\":\"%s\"}",
            error.getClass().getSimpleName(),
            error.getMessage().replace("\"", "\\\"")
        );
        return Flux.just(
            ServerSentEvent.<String>builder()
                .event("error")
                .data(errorJson)
                .build()
        );
    });
}
```

**关键点**:
1. **StreamingOutput**: 包含流式消息和 chunk
2. **InterruptionMetadata**: 触发人机交互确认
3. **错误处理**: 转换为结构化错误响应
4. **SSE 格式**: `data: {...}\n\n`

---

## 四、前端核心组件

### 4.1 技术栈

- **框架**: Next.js 15.2 (React 19)
- **UI 库**: Radix UI + Tailwind CSS
- **状态管理**: React Context + Hooks
- **流式处理**: Server-Sent Events (SSE)
- **数学渲染**: KaTeX
- **代码高亮**: React Syntax Highlighter
- **图表**: Recharts

---

### 4.2 StreamProvider - 流式消息管理

`StreamProvider` 是前端的核心状态管理组件:

#### **状态定义**

```typescript
interface StreamContextType {
  messages: UIMessage[];           // 消息列表
  isStreaming: boolean;            // 是否正在流式输出
  sendMessage: (content: string) => Promise<void>;  // 发送消息
  resumeFeedback: (toolFeedbacks: ToolFeedbackDTO[]) => Promise<void>;  // 恢复执行
  clearMessages: () => void;       // 清空消息
}
```

#### **sendMessage 实现**

```typescript
const sendMessage = useCallback(async (content: string) => {
  // 1. 自动创建 Thread
  let activeThreadId = currentThreadId;
  if (!activeThreadId) {
    const newThread = await createThread();
    activeThreadId = newThread.thread_id;
  }
  
  // 2. 添加用户消息到 UI
  const userUIMessage: UIMessage = {
    id: `user-${Date.now()}`,
    message: {
      messageType: 'user',
      content: content.trim(),
      metadata: {}
    },
    timestamp: Date.now()
  };
  setMessages(prev => [...prev, userUIMessage]);
  setIsStreaming(true);
  
  // 3. 调用后端流式 API
  const apiClient = createApiClient();
  const stream = apiClient.runAgentStream(
    activeThreadId,
    { messageType: 'user', content: content.trim(), metadata: {} },
    abortControllerRef.current.signal
  );
  
  // 4. 处理流式响应
  let isFirstChunk = true;
  for await (const agentResponse of stream) {
    if (agentResponse.chunk) {
      // 流式文本 chunk
      if (isFirstChunk) {
        // 创建新的 Assistant 消息
        const newAssistantMessage: UIMessage = {
          id: `assistant-${Date.now()}`,
          message: {
            messageType: 'assistant',
            content: agentResponse.chunk,
            metadata: {},
            toolCalls: []
          },
          timestamp: Date.now()
        };
        setMessages(prev => [...prev, newAssistantMessage]);
        isFirstChunk = false;
      } else {
        // 追加到现有消息
        setMessages(prev => {
          const newMessages = [...prev];
          const lastMessage = newMessages[newMessages.length - 1];
          newMessages[newMessages.length - 1] = {
            ...lastMessage,
            message: {
              ...lastMessage.message,
              content: lastMessage.message.content + agentResponse.chunk
            }
          };
          return newMessages;
        });
      }
    } else if (agentResponse.message) {
      // 完整消息 (工具调用、工具响应等)
      const backendMessage = fromMessageDTO(agentResponse.message);
      const messageType = agentResponse.message.messageType;
      
      if (messageType === 'tool-confirm') {
        // 工具确认消息,添加为新消息
        const newMessage: UIMessage = {
          id: `${messageType}-${Date.now()}`,
          message: backendMessage,
          timestamp: Date.now()
        };
        setMessages(prev => [...prev, newMessage]);
        isFirstChunk = true;  // 重置 flag
      }
    }
  }
  
  setIsStreaming(false);
}, [currentThreadId, createThread]);
```

**流程图**:

```
用户输入 "介绍一下 Spring AI"
         ↓
[User Message] 添加到 messages
         ↓
调用 runAgentStream()
         ↓
SSE Stream 开始
         ↓
chunk: "Spring" ──→ 创建 [Assistant Message]
         ↓
chunk: " AI" ────→ 追加到现有消息
         ↓
chunk: " 是..." ─→ 追加到现有消息
         ↓
message: {messageType: "tool-request", ...} ──→ 添加 [Tool Request Message]
         ↓
用户确认工具调用
         ↓
调用 resumeFeedback()
         ↓
message: {messageType: "tool", ...} ──→ 添加 [Tool Response Message]
         ↓
chunk: "根据..." ─→ 创建新 [Assistant Message]
         ↓
Stream 结束
```

---

### 4.3 spring-ai-api.ts - API 客户端

封装了所有后端 API 调用:

#### **核心方法**

**1. runAgentStream - 流式执行**

```typescript
async *runAgentStream(
  threadId: string,
  message: UserMessage,
  signal?: AbortSignal
): AsyncGenerator<AgentRunResponse, void, unknown> {
  const request: AgentRunRequest = {
    appName: process.env.NEXT_PUBLIC_APP_NAME || 'research_agent',
    userId: process.env.NEXT_PUBLIC_USER_ID || 'user-001',
    threadId,
    newMessage: message,
    streaming: true,
  };
  
  const response = await fetch(`${this.baseUrl}/run_sse`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'text/event-stream',
    },
    body: JSON.stringify(request),
    signal,
  });
  
  yield* this._processSSEStream(response);
}
```

**2. _processSSEStream - SSE 解析**

```typescript
private async *_processSSEStream(
  response: Response
): AsyncGenerator<AgentRunResponse, void, unknown> {
  const reader = response.body?.getReader();
  const decoder = new TextDecoder();
  let buffer = '';
  
  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    
    buffer += decoder.decode(value, { stream: true });
    const lines = buffer.split('\n');
    buffer = lines.pop() || '';
    
    for (const line of lines) {
      if (line.trim().startsWith('data:')) {
        const data = line.slice(5).trim();
        if (data) {
          const agentResponse: AgentRunResponse = JSON.parse(data);
          yield agentResponse;
        }
      }
    }
  }
}
```

**SSE 数据流示例**:

```
data: {"node":"llm_node","chunk":"Spring","message":null}

data: {"node":"llm_node","chunk":" AI","message":null}

data: {"node":"tool_node","chunk":null,"message":{"messageType":"tool-confirm","toolCalls":[...]}}
```

---

### 4.4 ThreadProvider - 会话管理

```typescript
export const ThreadProvider: React.FC = ({ children }) => {
  const [threads, setThreads] = useState<Thread[]>([]);
  const [currentThreadId, setCurrentThreadId] = useState<string | null>(null);
  
  // 创建新会话
  const createThread = async () => {
    const apiClient = createApiClient();
    const newThread = await apiClient.createSession(appName, userId);
    setThreads(prev => [newThread, ...prev]);
    setCurrentThreadId(newThread.thread_id);
    return newThread;
  };
  
  // 加载会话列表
  const loadThreads = async () => {
    const apiClient = createApiClient();
    const loadedThreads = await apiClient.listSessions(appName, userId);
    setThreads(loadedThreads);
  };
  
  // 删除会话
  const deleteThread = async (threadId: string) => {
    const apiClient = createApiClient();
    await apiClient.deleteSession(appName, userId, threadId);
    setThreads(prev => prev.filter(t => t.thread_id !== threadId));
    if (currentThreadId === threadId) {
      setCurrentThreadId(null);
    }
  };
  
  return (
    <ThreadContext.Provider value={{
      threads,
      currentThreadId,
      createThread,
      loadThreads,
      deleteThread,
      setCurrentThreadId
    }}>
      {children}
    </ThreadContext.Provider>
  );
};
```

---

### 4.5 消息组件

#### **消息类型定义**

```typescript
export interface UIMessage {
  id: string;
  message: Message;  // 实际消息内容
  timestamp: number;
}

export type Message = 
  | UserMessage
  | AssistantMessage
  | ToolRequestMessage
  | ToolRequestConfirmMessage
  | ToolResponseMessage;
```

#### **AssistantMessage 组件**

渲染 AI 回复:

```typescript
export function AssistantMessageComponent({ message }: { message: UIMessage }) {
  const assistantMsg = message.message as AssistantMessage;
  
  return (
    <div className="flex gap-4">
      <Avatar>
        <AvatarFallback>AI</AvatarFallback>
      </Avatar>
      <div className="flex-1">
        {/* Markdown 渲染 */}
        <MarkdownText content={assistantMsg.content} />
        
        {/* 工具调用展示 */}
        {assistantMsg.toolCalls?.map(toolCall => (
          <ToolCallCard key={toolCall.id} toolCall={toolCall} />
        ))}
        
        {/* Token 使用情况 */}
        {message.metadata?.tokenUsage && (
          <TokenUsage usage={message.metadata.tokenUsage} />
        )}
      </div>
    </div>
  );
}
```

#### **ToolRequestConfirmMessage 组件**

人机交互确认 UI:

```typescript
export function ToolFeedbackConfirm({ message }: { message: UIMessage }) {
  const { resumeFeedback } = useStream();
  const [feedbacks, setFeedbacks] = useState<ToolFeedbackDTO[]>([]);
  const toolConfirmMsg = message.message as ToolRequestConfirmMessage;
  
  // 初始化 feedbacks
  useEffect(() => {
    const initialFeedbacks = toolConfirmMsg.toolFeedback.map(tool => ({
      id: tool.id,
      name: tool.name,
      arguments: tool.arguments,
      result: 'APPROVED' as const,
      description: tool.description
    }));
    setFeedbacks(initialFeedbacks);
  }, []);
  
  // 修改工具参数
  const handleEditArguments = (id: string, newArguments: string) => {
    setFeedbacks(prev => prev.map(f => 
      f.id === id 
        ? { ...f, arguments: newArguments, result: 'EDITED' as const }
        : f
    ));
  };
  
  // 拒绝工具调用
  const handleReject = (id: string) => {
    setFeedbacks(prev => prev.map(f => 
      f.id === id 
        ? { ...f, result: 'REJECTED' as const }
        : f
    ));
  };
  
  // 提交反馈
  const handleSubmit = async () => {
    await resumeFeedback(feedbacks);
  };
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>🔧 Agent 请求调用以下工具</CardTitle>
        <CardDescription>请审核并确认</CardDescription>
      </CardHeader>
      <CardContent>
        {toolConfirmMsg.toolFeedback.map(tool => (
          <div key={tool.id} className="border p-4 rounded">
            <h4 className="font-semibold">{tool.name}</h4>
            <p className="text-sm text-muted-foreground">{tool.description}</p>
            
            {/* 参数编辑器 */}
            <JsonEditor 
              value={tool.arguments}
              onChange={newArgs => handleEditArguments(tool.id, newArgs)}
            />
            
            {/* 操作按钮 */}
            <div className="flex gap-2 mt-2">
              <Button 
                variant="outline" 
                onClick={() => handleReject(tool.id)}
              >
                ❌ 拒绝
              </Button>
            </div>
          </div>
        ))}
      </CardContent>
      <CardFooter>
        <Button onClick={handleSubmit}>
          ✅ 确认所有更改并继续
        </Button>
      </CardFooter>
    </Card>
  );
}
```

**效果展示**:

```
┌─────────────────────────────────────────────┐
│ 🔧 Agent 请求调用以下工具                     │
│ 请审核并确认                                 │
├─────────────────────────────────────────────┤
│ web_search                                  │
│ 在互联网上搜索信息                            │
│                                             │
│ {                                           │
│   "query": "Spring AI Alibaba"              │
│ }                                           │
│                                             │
│ [❌ 拒绝]                                    │
├─────────────────────────────────────────────┤
│ document_reader                             │
│ 读取文档内容                                 │
│                                             │
│ {                                           │
│   "path": "/docs/spring-ai.md"              │
│ }                                           │
│                                             │
│ [❌ 拒绝]                                    │
├─────────────────────────────────────────────┤
│                                             │
│           [✅ 确认所有更改并继续]              │
└─────────────────────────────────────────────┘
```

---

## 五、部署模式

### 5.1 嵌入式模式

**适用场景**: 将 UI 集成到现有 Spring Boot 应用中

**集成步骤**:

**1. 添加依赖**

```xml
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-studio</artifactId>
    <version>1.1.0.0-M4</version>
</dependency>
```

**2. 实现 AgentLoader**

```java
@Component
public class MyAgentLoader implements AgentLoader {
    @Override
    public List<String> listAgents() {
        return List.of("my_agent");
    }
    
    @Override
    public BaseAgent loadAgent(String name) {
        if ("my_agent".equals(name)) {
            return ReactAgent.builder()
                .name("my_agent")
                .chatClient(chatClient)
                .tools(myTools)
                .build();
        }
        throw new NoSuchElementException("Agent not found: " + name);
    }
}
```

**3. 启动应用**

```bash
mvn spring-boot:run
```

访问 `http://localhost:8080/chatui/index.html`

**优点**:
- ✅ 零配置,开箱即用
- ✅ 前端资源已打包到 JAR 中 (`/META-INF/resources/chatui/`)
- ✅ 适合快速原型和演示

**缺点**:
- ❌ 前端和后端耦合
- ❌ 不支持前端热更新

---

### 5.2 独立模式

**适用场景**: 前后端分离开发

**后端启动**:

```bash
cd spring-ai-alibaba-studio/src/test/java
# 运行 StudioApplication.java
```

**前端启动**:

```bash
cd spring-ai-alibaba-studio/agent-chat-ui
pnpm install
pnpm dev
```

访问 `http://localhost:3000`

**环境配置** (`.env.development`):

```properties
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME=research_agent
NEXT_PUBLIC_USER_ID=user-001
```

**优点**:
- ✅ 前后端完全解耦
- ✅ 支持前端热更新
- ✅ 便于开发调试

**缺点**:
- ❌ 需要分别启动前后端
- ❌ 需要配置 CORS

---

## 六、使用示例

### 6.1 基础对话

```typescript
// 用户
"介绍一下 Spring AI"

// Agent (流式输出)
"Spring" → " AI" → " 是一个..." → "用于构建 AI 应用的框架..."
```

### 6.2 工具调用

```typescript
// 用户
"搜索 Spring AI 的最新文档"

// Agent 请求工具调用
{
  messageType: "tool-confirm",
  toolFeedback: [{
    id: "call_123",
    name: "web_search",
    arguments: '{"query": "Spring AI latest documentation"}',
    description: "在互联网上搜索信息"
  }]
}

// 前端显示确认 UI
// 用户点击 "确认"

// 调用 resumeFeedback([{id: "call_123", result: "APPROVED", ...}])

// Agent 执行工具
{
  messageType: "tool",
  responses: [{
    id: "call_123",
    name: "web_search",
    responseData: "搜索结果: ..."
  }]
}

// Agent 继续生成回复
"根据搜索结果,Spring AI 的最新文档可以在..."
```

### 6.3 多轮对话

```typescript
// Thread 1
用户: "什么是 ReAct Agent?"
Agent: "ReAct Agent 是结合推理和行动的 Agent 架构..."

用户: "如何实现它?"
Agent: "实现 ReAct Agent 需要..."

// 保存到 Thread,下次加载时恢复
```

---

## 七、最佳实践

### 7.1 Agent 开发

**推荐模式**:

```java
@Component
public class ProductionAgentLoader implements AgentLoader {
    private final Map<String, Supplier<BaseAgent>> agentFactories;
    
    public ProductionAgentLoader(
        ChatClient chatClient,
        List<ToolCallback> tools
    ) {
        this.agentFactories = Map.of(
            "customer_support", () -> buildCustomerSupportAgent(chatClient, tools),
            "sales_assistant", () -> buildSalesAssistant(chatClient, tools)
        );
    }
    
    @Override
    public BaseAgent loadAgent(String name) {
        Supplier<BaseAgent> factory = agentFactories.get(name);
        if (factory == null) {
            throw new NoSuchElementException("Agent not found: " + name);
        }
        return factory.get();  // 每次调用都创建新实例,确保线程安全
    }
    
    private BaseAgent buildCustomerSupportAgent(ChatClient chatClient, List<ToolCallback> tools) {
        return ReactAgent.builder()
            .name("customer_support")
            .description("客户支持 Agent")
            .chatClient(chatClient)
            .tools(tools)
            .systemPrompt("你是一个专业的客户支持助手...")
            .build();
    }
}
```

---

### 7.2 流式输出优化

**问题**: 大量小 chunk 导致前端频繁渲染

**解决方案**: Batching

```typescript
// 前端批量更新
const BATCH_INTERVAL = 50; // ms
let batchedChunks: string[] = [];
let batchTimer: NodeJS.Timeout | null = null;

for await (const response of stream) {
  if (response.chunk) {
    batchedChunks.push(response.chunk);
    
    if (!batchTimer) {
      batchTimer = setTimeout(() => {
        const fullChunk = batchedChunks.join('');
        updateMessage(fullChunk);
        batchedChunks = [];
        batchTimer = null;
      }, BATCH_INTERVAL);
    }
  }
}
```

---

### 7.3 错误处理

**后端**:

```java
return agentStream
    .onErrorResume(error -> {
        log.error("Agent execution failed", error);
        String errorJson = formatErrorResponse(error);
        return Flux.just(
            ServerSentEvent.<String>builder()
                .event("error")
                .data(errorJson)
                .build()
        );
    });
```

**前端**:

```typescript
try {
  for await (const response of stream) {
    // 处理正常响应
  }
} catch (error) {
  if (error.name === "AbortError") {
    toast.info("请求已取消");
  } else {
    toast.error(`执行失败: ${error.message}`);
  }
}
```

---

### 7.4 性能优化

**1. Checkpoint 优化**

```java
CompileConfig config = CompileConfig.builder()
    .checkpointSaver(new RedisSaver(redissonClient))  // 使用 Redis
    .build();

CompiledGraph graph = stateGraph.compile(config);
```

**2. Thread 管理**

- 定期清理过期 Thread
- 限制每个用户的 Thread 数量
- 实现 Thread 归档机制

**3. 消息历史优化**

- 只加载最近 N 条消息
- 实现分页加载
- 压缩历史消息

---

## 八、扩展能力

### 8.1 自定义消息类型

**后端**:

```java
public class CustomMessage extends Message {
    private final Map<String, Object> customData;
    
    // 实现自定义序列化
}

// Controller 中处理
if (nodeOutput instanceof CustomOutput customOutput) {
    CustomMessageDTO dto = toCustomDTO(customOutput);
    response = new AgentRunResponse(node, agent, dto, tokenUsage, "");
}
```

**前端**:

```typescript
// 扩展消息类型
export interface CustomMessage extends Message {
  messageType: 'custom';
  customData: any;
}

// 渲染组件
export function CustomMessageComponent({ message }: { message: UIMessage }) {
  const customMsg = message.message as CustomMessage;
  return <CustomRenderer data={customMsg.customData} />;
}
```

---

### 8.2 插件系统

```java
public interface StudioPlugin {
    void onMessageSent(UserMessage message, RunnableConfig config);
    void onMessageReceived(AgentRunResponse response);
    void onError(Throwable error);
}

@Component
public class LoggingPlugin implements StudioPlugin {
    @Override
    public void onMessageSent(UserMessage message, RunnableConfig config) {
        log.info("User message: {}", message.getContent());
    }
}
```

---

### 8.3 多模态支持

**前端上传**:

```typescript
const sendImageMessage = async (imageFile: File) => {
  const base64 = await fileToBase64(imageFile);
  const message: UserMessage = {
    messageType: 'user',
    content: "请分析这张图片",
    media: [{
      mimeType: imageFile.type,
      data: base64
    }]
  };
  await sendMessage(message);
};
```

**后端处理**:

```java
if (userMessage.getMedia() != null) {
    for (Media media : userMessage.getMedia()) {
        // 处理图片/视频等
        processMedia(media);
    }
}
```

---

## 九、总结

### 核心能力

| 能力 | 说明 |
|------|------|
| 🎨 **可视化交互** | 美观的 Chat UI,支持 Markdown、代码高亮、数学公式 |
| 🔄 **实时流式输出** | SSE 流式传输,类似 ChatGPT 的打字机效果 |
| 🤝 **Human-in-the-Loop** | 工具调用前人工审核,支持修改参数或拒绝 |
| 💾 **会话管理** | Thread CRUD,历史记录持久化 |
| 🐛 **调试工具** | Token 使用统计、节点状态追踪 |
| 🚀 **灵活部署** | 嵌入式模式 & 独立模式,适配不同场景 |

### 适用场景

- ✅ **Agent 原型开发**: 快速验证想法
- ✅ **调试与测试**: 可视化 Agent 行为
- ✅ **演示与展示**: 给产品经理/客户演示
- ✅ **生产环境**: 嵌入到实际产品中

### 与其他方案对比

| 方案 | Studio | LangGraph Studio | AgentOps |
|------|--------|------------------|----------|
| 开源 | ✅ | ✅ | ❌ |
| Spring Boot 集成 | ✅ | ❌ | ❌ |
| 嵌入式部署 | ✅ | ❌ | ❌ |
| Human-in-the-Loop | ✅ | ✅ | ❌ |
| 流式输出 | ✅ | ✅ | ✅ |
| 多模态 | ✅ | ✅ | ❌ |

**Studio 的核心优势**:
1. **深度集成 Spring 生态**: 无缝衔接 Spring Boot 项目
2. **零依赖部署**: 前端资源打包到 JAR,无需额外部署前端
3. **灵活的 Agent 加载机制**: 支持静态加载和动态配置

---

## 十、参考资源

- **官方文档**: [Spring AI Alibaba Studio](https://github.com/alibaba/spring-ai-alibaba/tree/main/spring-ai-alibaba-studio)
- **示例项目**: `spring-ai-alibaba/examples/studio-demo`
- **前端仓库**: `agent-chat-ui` (基于 Next.js)
- **API 文档**: 查看 `ExecutionController.java` 的 REST 接口定义

---

🎉 **Happy Building with Spring AI Alibaba Studio!**

