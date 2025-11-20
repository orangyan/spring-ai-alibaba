# Spring AI Alibaba Starters 模块详解

> Starters 模块通过 Spring Boot 自动配置机制，将 Alibaba 中间件能力（主要是 Nacos）无缝集成到 AI 应用中，提供服务发现、配置管理和多智能体协作能力。

---

## 一、模块定位与价值

### 1.1 什么是 Spring Boot Starter？

**Starter** 是 Spring Boot 的核心特性之一，它通过**自动配置**（Auto-Configuration）简化依赖管理和配置：

```xml
<!-- 传统做法：手动配置 -->
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-client</artifactId>
</dependency>
<!-- 还需要写大量配置类 -->

<!-- Starter 做法：开箱即用 -->
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-a2a-nacos</artifactId>
</dependency>
<!-- 自动配置完成，只需在 application.yml 中配置 -->
```

### 1.2 为什么需要 Starters？

| 问题场景 | 传统做法 | Starters 方案 |
|---------|---------|--------------|
| **多智能体部署** | 手动维护服务列表 | Nacos 自动发现 |
| **Prompt 调整** | 重启应用 | 配置中心热更新 |
| **模型切换** | 修改代码 | 动态配置 |
| **工具管理** | 硬编码 | MCP 动态挂载 |

### 1.3 核心价值

1. **云原生架构**：基于 Nacos 的服务发现和配置管理
2. **零侵入集成**：无需修改业务代码，添加依赖即可
3. **动态热更新**：Prompt、模型、工具可运行时调整
4. **分布式协作**：支持跨服务的智能体调用链

---

## 二、模块架构概览

Spring AI Alibaba 提供 **两个核心 Starter**：

```
spring-boot-starters/
├── spring-ai-alibaba-starter-a2a-nacos/        # A2A 服务发现
│   ├── autoconfigure/                          # 自动配置类
│   ├── registry/nacos/                        # Nacos 注册与发现
│   └── core/server/                           # A2A 服务端
└── spring-ai-alibaba-starter-config-nacos/    # 配置管理
    ├── agent/nacos/                           # Agent 配置注入
    └── tools/                                 # MCP 工具管理
```

---

## 三、A2A Nacos Starter 详解

### 3.1 模块定位

**spring-ai-alibaba-starter-a2a-nacos** 解决的核心问题：

> 在分布式环境下，如何让智能体之间互相发现并通信？

**A2A (Agent-to-Agent)** 协议定义了智能体间的标准通信方式。

### 3.2 核心组件

#### 3.2.1 AgentCard (智能体名片)

**AgentCard** 是智能体的元数据描述：

```java
public class AgentCard {
    private String name;                    // 智能体名称
    private String description;             // 功能描述
    private Map<String, Object> inputSchema;  // 输入格式（JSON Schema）
    private Map<String, Object> outputSchema; // 输出格式
    private Capabilities capabilities;      // 能力标识
    private List<String> tags;             // 标签
}

public class Capabilities {
    private boolean streaming;    // 是否支持流式输出
    private boolean memory;       // 是否支持长期记忆
    private boolean tools;        // 是否支持工具调用
}
```

#### 3.2.2 服务注册流程

```java
// 1. 自动配置类启动
@Configuration
@EnableConfigurationProperties(NacosA2aProperties.class)
public class NacosA2aRegistryAutoConfiguration {
    
    @Bean
    public NacosAgentRegistry nacosAgentRegistry() {
        return new NacosAgentRegistry(a2aOperationService, properties);
    }
    
    @Bean
    @ConditionalOnBean(ReactAgent.class)
    public AgentCardRegistrar agentCardRegistrar(
            List<ReactAgent> agents,
            NacosAgentRegistry registry) {
        
        // 应用启动时自动注册
        agents.forEach(agent -> {
            AgentCard card = buildAgentCard(agent);
            registry.register(card);
        });
        
        return new AgentCardRegistrar();
    }
}
```

**Nacos 注册结构**：
```
服务名：saa.agent.{agentName}
实例信息：
  - IP: 192.168.1.100
  - Port: 8080
  - Metadata:
      {
        "name": "翻译助手",
        "description": "提供中英互译服务",
        "inputSchema": {...},
        "capabilities": {"streaming": true}
      }
```

#### 3.2.3 服务发现流程

```java
// NacosAgentCardProvider 实现
public class NacosAgentCardProvider implements AgentCardProvider {
    
    private final A2aService a2aService;
    
    @Override
    public AgentCardWrapper getAgentCard(String agentName) {
        // 1. 从 Nacos 获取智能体元数据
        AgentCard nacosAgentCard = a2aService.getAgentCard(agentName);
        
        // 2. 订阅变更通知
        a2aService.subscribeAgentCard(agentName, new AbstractNacosAgentCardListener() {
            @Override
            public void onEvent(NacosAgentCardEvent event) {
                // 3. 智能体元数据变更时自动更新
                AgentCard newCard = event.getAgentCard();
                agentCard.setAgentCard(convertToA2aAgentCard(newCard));
            }
        });
        
        return new NacosAgentCardWrapper(convertToA2aAgentCard(nacosAgentCard));
    }
}
```

### 3.3 A2A 通信协议

#### 3.3.1 JSON-RPC 2.0

A2A 默认采用 **JSON-RPC 2.0** 协议进行通信：

**请求格式**：
```json
{
  "jsonrpc": "2.0",
  "method": "sendMessage",
  "params": {
    "message": {
      "role": "user",
      "content": "翻译：Hello"
    },
    "configuration": {
      "temperature": 0.7
    }
  },
  "id": "req-123"
}
```

**响应格式**：
```json
{
  "jsonrpc": "2.0",
  "result": {
    "message": {
      "role": "assistant",
      "content": "你好"
    },
    "metadata": {
      "model": "qwen-max",
      "usage": {"inputTokens": 10, "outputTokens": 5}
    }
  },
  "id": "req-123"
}
```

#### 3.3.2 JsonRpcA2aRequestHandler

**服务端请求处理器**：

```java
public class JsonRpcA2aRequestHandler implements A2aRequestHandler {
    
    private final JSONRPCHandler jsonRpcHandler;
    
    @Override
    public Object onHandler(String body, ServerRequest.Headers headers) {
        // 1. 判断是否为流式请求
        boolean streaming = isStreamingRequest(body);
        
        // 2. 路由到不同的处理方法
        if (streaming) {
            return handleStreamRequest(body);
        } else {
            return handleNonStreamRequest(body);
        }
    }
    
    private Flux<?> handleStreamRequest(String body) {
        SendStreamingMessageRequest req = parseRequest(body);
        
        // 调用 Graph Agent 执行
        Flow.Publisher<JSONRPCResponse> publisher = 
            jsonRpcHandler.onMessageSendStream(req);
        
        // 返回 Reactor Flux
        return Flux.from(FlowAdapters.toPublisher(publisher))
            .delaySubscription(Duration.ofMillis(10));
    }
}
```

### 3.4 完整调用链路

```
服务 A (调用方)                       Nacos                    服务 B (被调用方)
     │                                 │                              │
     │ 1. 查询"翻译助手"                │                              │
     ├────────────────────────────────>│                              │
     │                                 │                              │
     │ 2. 返回 AgentCard + 服务地址     │                              │
     │<────────────────────────────────┤                              │
     │                                 │                              │
     │ 3. HTTP POST (JSON-RPC)                                       │
     ├───────────────────────────────────────────────────────────────>│
     │   Body: {"method": "sendMessage", "params": {...}}            │
     │                                 │                              │
     │                                 │   4. GraphAgentExecutor 执行│
     │                                 │                    ┌─────────┤
     │                                 │                    │ LlmNode │
     │                                 │                    │ ToolNode│
     │                                 │                    └─────────┤
     │                                 │                              │
     │ 5. 返回结果                                                     │
     │<───────────────────────────────────────────────────────────────┤
     │   Body: {"result": {"message": {...}}}                        │
```

### 3.5 使用示例

#### 3.5.1 服务提供方（智能体服务）

```java
// 1. 添加依赖
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-a2a-nacos</artifactId>
</dependency>

// 2. 配置 Nacos
spring:
  application:
    name: translator-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848

// 3. 定义 Agent（会自动注册到 Nacos）
@Bean
public ReactAgent translatorAgent() {
    return ReactAgent.builder()
        .name("翻译助手")
        .description("中英互译服务")
        .chatClient(chatClient)
        .build();
}
```

#### 3.5.2 服务消费方（调用远程智能体）

```java
// 1. 添加依赖（同上）

// 2. 配置 Nacos（同上）

// 3. 构建远程 Agent
@Bean
public A2aRemoteAgent remoteTranslator(NacosAgentCardProvider provider) {
    return A2aRemoteAgent.builder()
        .name("翻译助手")
        .agentCardProvider(provider)  // 从 Nacos 获取
        .build();
}

// 4. 在主 Agent 中使用
@Bean
public ReactAgent mainAgent(A2aRemoteAgent remoteTranslator) {
    return ReactAgent.builder()
        .name("主助手")
        .chatClient(chatClient)
        .tools(List.of(
            weatherTool,
            remoteTranslator.asTool()  // 作为工具使用
        ))
        .build();
}
```

---

## 四、Config Nacos Starter 详解

### 4.1 模块定位

**spring-ai-alibaba-starter-config-nacos** 解决的核心问题：

> 如何在不重启应用的情况下，动态调整 Agent 的行为（Prompt、模型、工具）？

### 4.2 核心组件

#### 4.2.1 配置结构

**Nacos 配置中心的数据组织**：

```
Group: ai-agent-{agentName}
├── agent-base.json                  # Agent 基础配置
├── Group: nacos-ai-meta
│   ├── prompt-system.json          # System Prompt
│   ├── prompt-user.json            # User Prompt 模板
│   └── mcp-servers.json            # MCP 工具配置
└── Group: nacos-ai-model
    └── model-config.json           # 模型配置
```

#### 4.2.2 AgentVO (Agent 配置对象)

```java
public class AgentVO {
    private String name;                  // Agent 名称
    private String description;           // 描述
    private PromptVO prompt;              // Prompt 配置
    private ModelVO model;                // 模型配置
    private MemoryVO memory;              // 记忆配置
    private PartnerAgentsVO partnerAgents; // 协作智能体
    private McpServersVO mcpServers;      // MCP 工具
}

public class PromptVO {
    private String systemPrompt;          // System Prompt
    private String userPromptTemplate;    // User Prompt 模板
    private Map<String, String> variables; // 变量
}

public class ModelVO {
    private String provider;              // 模型提供商（dashscope/openai）
    private String modelName;             // 模型名称（qwen-max）
    private Double temperature;           // 温度
    private Integer maxTokens;            // 最大 Token 数
}
```

#### 4.2.3 NacosAgentBuilderFactory (动态构建工厂)

```java
public class NacosAgentBuilderFactory implements AgentBuilderFactory {
    
    private NacosOptions nacosOptions;
    
    @Override
    public Builder builder() {
        // 1. 从 Nacos 加载配置
        AgentVO agentVO = NacosAgentInjector.loadAgentVO(nacosOptions);
        
        // 2. 注入 Prompt
        PromptVO promptVO = NacosPromptInjector.getPromptByKey(
            nacosOptions, agentVO.getPrompt().getPromptKey()
        );
        
        // 3. 注入 Model
        ChatClient chatClient = NacosModelInjector.buildChatClient(
            agentVO.getModel()
        );
        
        // 4. 注入 MCP 工具
        List<ToolCallback> tools = NacosMcpToolsInjector.loadTools(
            agentVO.getMcpServers()
        );
        
        // 5. 构建 ReactAgent
        return ReactAgent.builder()
            .name(agentVO.getName())
            .systemPrompt(promptVO.getSystemPrompt())
            .chatClient(chatClient)
            .tools(tools);
    }
}
```

### 4.3 热更新机制

#### 4.3.1 Nacos 配置监听

```java
@Component
public class NacosAgentConfigListener {
    
    @PostConstruct
    public void init() {
        // 监听 Agent 配置变更
        nacosConfigService.addListener(
            dataId: "agent-base.json",
            group: "ai-agent-customerService",
            new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    // 1. 解析新配置
                    AgentVO newConfig = JSON.parseObject(configInfo, AgentVO.class);
                    
                    // 2. 重建 Agent
                    ReactAgent newAgent = nacosAgentBuilderFactory
                        .builder()
                        .build();
                    
                    // 3. 替换旧实例
                    agentRegistry.replaceAgent(newAgent);
                    
                    logger.info("Agent 配置已更新: {}", newConfig.getName());
                }
            }
        );
    }
}
```

#### 4.3.2 Prompt 热更新流程

```
运营人员 → Nacos 控制台修改 Prompt
    ↓
Nacos Server 推送变更通知
    ↓
应用监听器接收通知
    ↓
NacosPromptInjector 加载新 Prompt
    ↓
更新 AgentLlmNode.systemPrompt
    ↓
下一次对话生效（无需重启）
```

### 4.4 配置示例

#### 4.4.1 agent-base.json

```json
{
  "name": "客服助手",
  "description": "提供订单查询、退款等服务",
  "prompt": {
    "promptKey": "customer-service-system"
  },
  "model": {
    "provider": "dashscope",
    "modelName": "qwen-max",
    "temperature": 0.7,
    "maxTokens": 2000
  },
  "mcpServers": {
    "servers": [
      {
        "name": "filesystem",
        "transport": "stdio",
        "command": "npx",
        "args": ["@modelcontextprotocol/server-filesystem", "/data"]
      }
    ]
  }
}
```

#### 4.4.2 prompt-system.json

```json
{
  "promptKey": "customer-service-system",
  "content": "你是一个专业的客服人员。\n请使用礼貌、专业的语气回答用户问题。\n如果需要查询订单，请使用 queryOrder 工具。",
  "variables": {
    "companyName": "阿里云",
    "serviceHours": "9:00-18:00"
  },
  "version": "v1.2"
}
```

#### 4.4.3 mcp-servers.json

```json
{
  "servers": [
    {
      "name": "filesystem",
      "description": "文件系统工具",
      "transport": "stdio",
      "command": "npx",
      "args": [
        "@modelcontextprotocol/server-filesystem",
        "/Users/user/data"
      ]
    },
    {
      "name": "github",
      "description": "GitHub API 工具",
      "transport": "sse",
      "url": "http://localhost:3000/sse"
    }
  ]
}
```

### 4.5 使用示例

```java
// 1. 添加依赖
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-config-nacos</artifactId>
</dependency>

// 2. 配置 Nacos
spring:
  application:
    name: customer-service
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        namespace: dev
        
saa:
  agent:
    name: customerService
    enabled: true

// 3. 使用动态构建的 Agent
@RestController
public class ChatController {
    
    @Autowired
    private NacosAgentBuilderFactory factory;
    
    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        // 每次调用都会从 Nacos 获取最新配置
        ReactAgent agent = factory.builder().build();
        AssistantMessage response = agent.call(message);
        return response.getText();
    }
}
```

---

## 五、实战场景

### 5.1 场景一：多环境 Prompt 管理

**需求**：测试环境和生产环境使用不同的 Prompt

**解决方案**：
```yaml
# 测试环境
spring:
  cloud:
    nacos:
      config:
        namespace: test

# 生产环境
spring:
  cloud:
    nacos:
      config:
        namespace: prod
```

在 Nacos 中为不同 namespace 配置不同的 `prompt-system.json`。

### 5.2 场景二：A/B 测试

**需求**：对比两种 Prompt 的效果

**解决方案**：
```java
// 策略路由
@Bean
public ReactAgent agentA(NacosAgentBuilderFactory factory) {
    return factory.builder()
        .nacosOptions(options.withPromptKey("prompt-a"))
        .build();
}

@Bean
public ReactAgent agentB(NacosAgentBuilderFactory factory) {
    return factory.builder()
        .nacosOptions(options.withPromptKey("prompt-b"))
        .build();
}

// 按用户 ID 分流
public ReactAgent selectAgent(String userId) {
    return userId.hashCode() % 2 == 0 ? agentA : agentB;
}
```

### 5.3 场景三：模型成本优化

**需求**：高峰期使用便宜模型，低峰期使用高级模型

**解决方案**：
```java
// 定时任务
@Scheduled(cron = "0 0 9 * * ?")  // 每天 9 点
public void switchToQwenPlus() {
    updateNacosConfig("model-config.json", """
        {"modelName": "qwen-plus"}
    """);
}

@Scheduled(cron = "0 0 22 * * ?")  // 每天 22 点
public void switchToQwenMax() {
    updateNacosConfig("model-config.json", """
        {"modelName": "qwen-max"}
    """);
}
```

---

## 六、最佳实践

### 6.1 配置管理

1. **版本控制**：在 Prompt 配置中添加 `version` 字段
2. **灰度发布**：先在测试环境验证，再推送到生产
3. **配置备份**：定期导出 Nacos 配置到 Git

### 6.2 性能优化

1. **缓存 AgentCard**：避免每次调用都查询 Nacos
```java
@Cacheable(value = "agentCard", key = "#agentName")
public AgentCard getAgentCard(String agentName) {
    return nacosAgentCardProvider.getAgentCard(agentName);
}
```

2. **本地配置优先**：开发环境使用本地配置，生产环境使用 Nacos
```yaml
saa:
  agent:
    config-source: ${CONFIG_SOURCE:local}  # local / nacos
```

### 6.3 监控与告警

1. **配置变更通知**：
```java
@Bean
public NacosConfigListener configListener() {
    return new NacosConfigListener() {
        @Override
        public void onConfigChange(String dataId, String content) {
            // 发送钉钉/企微通知
            dingTalkClient.send(
                "配置变更：" + dataId + "\n内容：" + content
            );
        }
    };
}
```

2. **服务健康检查**：
```java
@HealthIndicator
public Health nacosHealth() {
    try {
        List<Instance> instances = namingService.getAllInstances("test-service");
        return instances.isEmpty() ? 
            Health.down().build() : 
            Health.up().build();
    } catch (Exception e) {
        return Health.down(e).build();
    }
}
```

---

## 七、总结

Starters 模块通过与 Nacos 的深度集成，实现了：

| 能力 | A2A Nacos Starter | Config Nacos Starter |
|------|-------------------|---------------------|
| **服务发现** | ✅ AgentCard 注册与订阅 | - |
| **配置管理** | - | ✅ Prompt/Model/Tools 热更新 |
| **通信协议** | ✅ JSON-RPC 2.0 | - |
| **流式输出** | ✅ SSE 支持 | - |
| **多环境** | ✅ Namespace 隔离 | ✅ Namespace 隔离 |

它们是 Spring AI Alibaba 框架**云原生化**的关键组件，使得 AI 应用能够像微服务一样灵活部署和管理。

