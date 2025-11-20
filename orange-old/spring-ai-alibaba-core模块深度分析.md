# Spring AI Alibaba Core 模块深度分析

## 文档信息
- **模块名称**: spring-ai-alibaba-core  
- **版本**: 1.1.0.0-SNAPSHOT
- **创建日期**: 2025-10-02
- **文档类型**: 核心模块技术分析

---

## 目录
1. [模块概述](#模块概述)
2. [模块架构](#模块架构)
3. [核心组件详解](#核心组件详解)
4. [API客户端实现](#api客户端实现)
5. [模型实现详解](#模型实现详解)
6. [RAG支持](#rag支持)
7. [可观测性](#可观测性)
8. [配置与使用](#配置与使用)

---

## 模块概述

### 定位
spring-ai-alibaba-core 是整个 Spring AI Alibaba 框架的**核心基础模块**，提供了与阿里云百炼（DashScope）平台的深度集成，实现了 Spring AI 标准接口的完整适配。

### 主要功能
1. **多模型支持** - Chat、Image、Audio、Video、Embedding 等
2. **Agent 抽象** - 提供智能体基础能力
3. **RAG 支持** - 文档检索增强生成
4. **Tool Calling** - 函数调用能力
5. **Streaming** - 流式响应支持
6. **Observation** - 完整的可观测性
7. **Rerank** - 文档重排序

### 依赖关系

```xml
<dependencies>
    <!-- Spring AI 核心依赖 -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-commons</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-model</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-rag</artifactId>
    </dependency>
    
    <!-- HTTP 客户端 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
    </dependency>
    
    <!-- NLP 工具 -->
    <dependency>
        <groupId>org.apache.opennlp</groupId>
        <artifactId>opennlp-tools</artifactId>
    </dependency>
</dependencies>
```

---

## 模块架构

### 总体架构图

```
┌─────────────────────────────────────────────────────────┐
│              Spring AI Interfaces                        │
│  ChatModel │ ImageModel │ EmbeddingModel │ AudioModel   │
└─────────────────────────┬───────────────────────────────┘
                          │ implements
┌─────────────────────────┴───────────────────────────────┐
│         spring-ai-alibaba-core (Adapter Layer)          │
│                                                          │
│  ┌──────────────────────────────────────────────────┐  │
│  │  Model Layer (模型层)                             │  │
│  │  • DashScopeChatModel                            │  │
│  │  • DashScopeImageModel                           │  │
│  │  • DashScopeEmbeddingModel                       │  │
│  │  • DashScopeAudioModel                           │  │
│  │  • DashScopeRerankModel                          │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↓                               │
│  ┌──────────────────────────────────────────────────┐  │
│  │  API Layer (API层)                                │  │
│  │  • DashScopeApi (REST/WebFlux)                   │  │
│  │  • DashScopeAgentApi                             │  │
│  │  • DashScopeImageApi                             │  │
│  │  • DashScopeAudioApi                             │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↓                               │
│  ┌──────────────────────────────────────────────────┐  │
│  │  HTTP Client Layer (HTTP客户端层)                 │  │
│  │  • RestClient (同步)                              │  │
│  │  • WebClient (异步/流式)                          │  │
│  │  • OkHttpClient (文件上传)                        │  │
│  └──────────────────────────────────────────────────┘  │
│                                                          │
└──────────────────────────┬───────────────────────────────┘
                           │ HTTP/SSE
┌──────────────────────────┴───────────────────────────────┐
│              DashScope API Service                        │
│              (百炼模型服务)                                │
└───────────────────────────────────────────────────────────┘
```

### 包结构

```
com.alibaba.cloud.ai/
├── agent/                     # Agent 抽象
│   └── Agent.java            # 基础 Agent 接口
│
├── advisor/                   # RAG 增强器
│   ├── DocumentRetrievalAdvisor.java      # 文档检索增强
│   ├── RetrievalRerankAdvisor.java        # 检索+重排序
│   ├── CompositeDocumentRetriever.java    # 复合检索器
│   └── DashScopeDocumentAnalysisAdvisor.java
│
├── dashscope/                # DashScope 实现
│   ├── api/                  # API 客户端
│   │   ├── DashScopeApi.java              # 核心 API
│   │   ├── DashScopeAgentApi.java         # Agent API
│   │   ├── DashScopeImageApi.java         # 图像 API
│   │   ├── DashScopeAudioSpeechApi.java   # 语音合成 API
│   │   ├── DashScopeAudioTranscriptionApi.java # 语音识别 API
│   │   └── DashScopeVideoApi.java         # 视频 API
│   │
│   ├── chat/                 # 聊天模型
│   │   ├── DashScopeChatModel.java        # 聊天模型实现
│   │   ├── DashScopeChatOptions.java      # 配置选项
│   │   └── observation/                   # 可观测
│   │
│   ├── image/                # 图像模型
│   │   ├── DashScopeImageModel.java       # 图像生成模型
│   │   ├── DashScopeImageOptions.java     # 配置选项
│   │   └── observation/                   # 可观测
│   │
│   ├── embedding/            # 嵌入模型
│   │   ├── DashScopeEmbeddingModel.java   # 向量化模型
│   │   └── DashScopeEmbeddingOptions.java # 配置选项
│   │
│   ├── audio/                # 音频模型
│   │   ├── DashScopeAudioSpeechModel.java       # TTS
│   │   ├── DashScopeAudioTranscriptionModel.java # STT
│   │   └── synthesis/                           # 语音合成
│   │
│   ├── rerank/               # 重排序模型
│   │   ├── DashScopeRerankModel.java      # 重排序实现
│   │   └── DashScopeRerankOptions.java    # 配置选项
│   │
│   ├── rag/                  # RAG 支持
│   │   ├── DashScopeCloudStore.java           # 云端知识库
│   │   ├── DashScopeDocumentCloudReader.java  # 文档读取
│   │   ├── DashScopeDocumentRetriever.java    # 文档检索
│   │   └── DashScopeDocumentTransformer.java  # 文档转换
│   │
│   ├── agent/                # DashScope Agent
│   │   ├── DashScopeAgent.java
│   │   └── DashScopeAgentOptions.java
│   │
│   ├── video/                # 视频模型
│   │   ├── DashScopeVideoModel.java
│   │   └── VideoModel.java
│   │
│   ├── common/               # 通用组件
│   │   ├── DashScopeApiConstants.java   # 常量定义
│   │   ├── DashScopeException.java      # 异常类
│   │   └── ErrorCodeEnum.java           # 错误码
│   │
│   └── metadata/             # 元数据
│       └── DashScopeAiUsage.java        # Token 使用统计
│
├── document/                 # 文档处理
│   ├── DocumentParser.java              # 文档解析器接口
│   ├── TextDocumentParser.java          # 文本解析器
│   ├── JsonDocumentParser.java          # JSON 解析器
│   └── DocumentWithScore.java           # 带分数的文档
│
├── evaluation/               # 模型评估
│   ├── AnswerCorrectnessEvaluator.java  # 答案正确性
│   ├── AnswerFaithfulnessEvaluator.java # 答案忠实度
│   ├── AnswerRelevancyEvaluator.java    # 答案相关性
│   └── LaajEvaluator.java               # LAAJ 评估器
│
├── model/                    # 模型抽象
│   ├── RerankModel.java                 # 重排序接口
│   ├── RerankRequest.java
│   ├── RerankResponse.java
│   └── SpringAIAlibabaModels.java       # 模型枚举
│
├── tool/                     # 工具调用
│   ├── ObservableToolCallingManager.java
│   └── observation/                     # 工具调用可观测
│
└── transformer/              # 文本转换器
    └── splitter/             # 文本分割器
        ├── RecursiveCharacterTextSplitter.java
        └── SentenceSplitter.java
```

---

## 核心组件详解

### 1. DashScopeChatModel (聊天模型)

#### 类图

```
                 ┌─────────────┐
                 │  ChatModel  │ (Spring AI Interface)
                 └──────▲──────┘
                        │
           ┌────────────┴────────────┐
           │  DashScopeChatModel     │
           ├─────────────────────────┤
           │ - dashscopeApi          │
           │ - defaultOptions        │
           │ - retryTemplate         │
           │ - observationRegistry   │
           │ - toolCallingManager    │
           ├─────────────────────────┤
           │ + call(Prompt)          │
           │ + stream(Prompt)        │
           │ - createRequest()       │
           │ - toChatResponse()      │
           └─────────────────────────┘
```

#### 核心实现

```java
public class DashScopeChatModel implements ChatModel {
    
    // 依赖组件
    private final DashScopeApi dashscopeApi;
    private DashScopeChatOptions defaultOptions;
    private final RetryTemplate retryTemplate;
    private final ObservationRegistry observationRegistry;
    private final ToolCallingManager toolCallingManager;
    
    // 默认配置
    public static final String DEFAULT_MODEL_NAME = "qwen-plus";
    public static final Double DEFAULT_TEMPERATURE = 0.7;
    
    /**
     * 同步调用
     */
    @Override
    public ChatResponse call(Prompt prompt) {
        // 1. 验证输入
        Assert.notNull(prompt, "Prompt must not be null");
        Assert.isTrue(!CollectionUtils.isEmpty(prompt.getInstructions()), 
            "Prompt messages must not be empty");
        
        // 2. 构建请求
        Prompt requestPrompt = buildRequestPrompt(prompt);
        
        // 3. 内部调用（支持 Function Calling）
        return internalCall(requestPrompt, null);
    }
    
    /**
     * 内部调用实现（核心逻辑）
     */
    public ChatResponse internalCall(Prompt prompt, 
            ChatResponse previousChatResponse) {
        
        // 1. 创建 API 请求
        ChatCompletionRequest request = createRequest(prompt, false);
        
        // 2. 创建观测上下文
        ChatModelObservationContext observationContext = 
            ChatModelObservationContext.builder()
                .prompt(prompt)
                .provider(DashScopeApiConstants.PROVIDER_NAME)
                .build();
        
        // 3. 执行请求（带观测）
        ChatResponse response = ChatModelObservationDocumentation
            .CHAT_MODEL_OPERATION
            .observation(observationConvention, 
                DEFAULT_OBSERVATION_CONVENTION, 
                () -> observationContext,
                observationRegistry)
            .observe(() -> {
                // 使用重试模板调用 API
                ResponseEntity<ChatCompletion> completionEntity = 
                    retryTemplate.execute(ctx -> 
                        dashscopeApi.chatCompletionEntity(
                            request, 
                            getAdditionalHttpHeaders(prompt)
                        )
                    );
                
                // 转换响应
                ChatResponse chatResponse = toChatResponse(
                    completionEntity.getBody(), 
                    previousChatResponse, 
                    request, 
                    null
                );
                
                observationContext.setResponse(chatResponse);
                return chatResponse;
            });
        
        // 4. 处理 Function Calling
        if (toolExecutionEligibilityPredicate
                .isToolExecutionRequired(prompt.getOptions(), response)) {
            
            // 执行工具调用
            var toolExecutionResult = toolCallingManager
                .executeToolCalls(prompt, response);
            
            if (toolExecutionResult.returnDirect()) {
                // 直接返回工具执行结果
                return ChatResponse.builder()
                    .from(response)
                    .generations(ToolExecutionResult
                        .buildGenerations(toolExecutionResult))
                    .build();
            } else {
                // 将工具结果发回模型
                return internalCall(
                    new Prompt(
                        toolExecutionResult.conversationHistory(), 
                        prompt.getOptions()
                    ),
                    response
                );
            }
        }
        
        return response;
    }
    
    /**
     * 流式调用
     */
    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        // 验证
        Assert.notNull(prompt, "Prompt must not be null");
        Assert.isTrue(!CollectionUtils.isEmpty(prompt.getInstructions()), 
            "Prompt messages must not be empty");
        
        // 构建请求
        Prompt requestPrompt = buildRequestPrompt(prompt);
        
        // 内部流式调用
        return internalStream(requestPrompt, null);
    }
    
    /**
     * 内部流式调用
     */
    public Flux<ChatResponse> internalStream(Prompt prompt, 
            ChatResponse previousChatResponse) {
        
        return Flux.deferContextual(contextView -> {
            // 1. 创建请求
            ChatCompletionRequest request = createRequest(prompt, true);
            
            // 2. 调用流式 API
            Flux<ChatCompletionChunk> completionChunks = 
                retryTemplate.execute(ctx -> 
                    dashscopeApi.chatCompletionStream(
                        request, 
                        getAdditionalHttpHeaders(prompt)
                    )
                );
            
            // 3. 角色映射（流式响应第一个 chunk 包含 role）
            ConcurrentHashMap<String, String> roleMap = 
                new ConcurrentHashMap<>();
            
            // 4. 创建观测
            ChatModelObservationContext observationContext = 
                ChatModelObservationContext.builder()
                    .prompt(prompt)
                    .provider(DashScopeApiConstants.PROVIDER_NAME)
                    .build();
            
            Observation observation = 
                ChatModelObservationDocumentation.CHAT_MODEL_OPERATION
                    .observation(observationConvention, 
                        DEFAULT_OBSERVATION_CONVENTION, 
                        () -> observationContext,
                        observationRegistry);
            
            observation.parentObservation(
                contextView.getOrDefault(
                    ObservationThreadLocalAccessor.KEY, null)
            ).start();
            
            // 5. 转换流
            Flux<ChatResponse> chatResponse = completionChunks
                .map(this::chunkToChatCompletion)
                .switchMap(chatCompletion -> 
                    Mono.just(chatCompletion)
                        .map(cc -> toChatResponse(
                            cc, 
                            previousChatResponse, 
                            request, 
                            roleMap
                        ))
                );
            
            // 6. 处理流式 Function Calling
            Flux<ChatResponse> flux = chatResponse
                .flatMap(response -> {
                    if (toolExecutionEligibilityPredicate
                            .isToolExecutionRequired(
                                prompt.getOptions(), 
                                response
                            )) {
                        // 执行工具调用
                        var toolExecutionResult = 
                            toolCallingManager.executeToolCalls(
                                prompt, 
                                response
                            );
                        
                        if (toolExecutionResult.returnDirect()) {
                            return Flux.just(response);
                        } else {
                            // 递归调用
                            return internalStream(
                                new Prompt(
                                    toolExecutionResult
                                        .conversationHistory(), 
                                    prompt.getOptions()
                                ),
                                response
                            );
                        }
                    }
                    return Flux.just(response);
                })
                .doOnError(observation::error)
                .doFinally(s -> observation.stop())
                .contextWrite(ctx -> 
                    ctx.put(ObservationThreadLocalAccessor.KEY, observation)
                );
            
            return flux;
        });
    }
    
    /**
     * 创建 API 请求
     */
    private ChatCompletionRequest createRequest(Prompt prompt, boolean stream) {
        // 1. 合并选项（运行时 + 默认）
        ChatOptions options = prompt.getOptions() != null 
            ? ModelOptionsUtils.merge(
                prompt.getOptions(), 
                defaultOptions, 
                ChatOptions.class
              )
            : defaultOptions;
        
        // 2. 转换消息
        List<Message> messages = prompt.getInstructions()
            .stream()
            .map(this::toApiMessage)
            .toList();
        
        // 3. 处理 Function Definitions
        List<FunctionTool> functionTools = null;
        if (options.getFunctions() != null) {
            functionTools = options.getFunctions()
                .stream()
                .map(this::toFunctionTool)
                .toList();
        }
        
        // 4. 构建请求
        return ChatCompletionRequest.builder()
            .model(options.getModel())
            .messages(messages)
            .temperature(options.getTemperature())
            .topP(options.getTopP())
            .maxTokens(options.getMaxTokens())
            .stream(stream)
            .tools(functionTools)
            .build();
    }
}
```

#### 关键特性

**1. 重试机制**
```java
// 使用 RetryTemplate 实现自动重试
ResponseEntity<ChatCompletion> response = retryTemplate.execute(
    ctx -> dashscopeApi.chatCompletionEntity(request)
);
```

**2. Function Calling 支持**
```java
// 检测是否需要调用函数
if (toolExecutionEligibilityPredicate.isToolExecutionRequired(
        prompt.getOptions(), response)) {
    
    // 执行函数调用
    var result = toolCallingManager.executeToolCalls(prompt, response);
    
    // 将结果发回模型
    return internalCall(
        new Prompt(result.conversationHistory(), options),
        response
    );
}
```

**3. 可观测性集成**
```java
// 使用 Micrometer Observation
return ChatModelObservationDocumentation.CHAT_MODEL_OPERATION
    .observation(observationConvention, 
        DEFAULT_OBSERVATION_CONVENTION, 
        () -> observationContext,
        observationRegistry)
    .observe(() -> {
        // 执行实际调用
        return callApi();
    });
```

**4. 流式响应处理**
```java
// Flux 流式处理
Flux<ChatResponse> stream = completionChunks
    .map(this::chunkToChatCompletion)
    .switchMap(cc -> Mono.just(cc).map(this::toChatResponse))
    .flatMap(response -> handleFunctionCalling(response))
    .doOnError(observation::error)
    .doFinally(s -> observation.stop());
```

---

### 2. DashScopeImageModel (图像生成模型)

#### 实现特点

```java
public class DashScopeImageModel implements ImageModel {
    
    private final DashScopeImageApi dashScopeImageApi;
    private final DashScopeImageOptions defaultOptions;
    private final RetryTemplate retryTemplate;
    private final ObservationRegistry observationRegistry;
    
    /**
     * 异步任务模式
     * DashScope 图像生成采用异步任务模式：
     * 1. 提交任务 -> 获得 taskId
     * 2. 轮询任务状态 -> PENDING/RUNNING/SUCCEEDED/FAILED
     * 3. 获取结果
     */
    @Override
    public ImageResponse call(ImagePrompt request) {
        // 1. 提交图像生成任务
        String taskId = submitImageGenTask(request);
        if (taskId == null) {
            return new ImageResponse(List.of(), toMetadataEmpty());
        }
        
        // 2. 创建观测上下文
        ImageModelObservationContext observationContext = 
            ImageModelObservationContext.builder()
                .imagePrompt(request)
                .provider(DashScopeApiConstants.PROVIDER_NAME)
                .build();
        
        Observation observation = 
            ImageModelObservationDocumentation.IMAGE_MODEL_OPERATION
                .observation(observationConvention, 
                    new DefaultImageModelObservationConvention(), 
                    () -> observationContext,
                    observationRegistry);
        
        // 3. 轮询任务状态（带重试）
        return observation.observe(() -> 
            retryTemplate.execute(ctx -> {
                observation.lowCardinalityKeyValue(
                    "retry.attempt", 
                    String.valueOf(ctx.getRetryCount())
                );
                
                // 获取任务状态
                DashScopeImageAsyncResponse resp = 
                    getImageGenTask(taskId);
                
                if (resp != null) {
                    String status = resp.output().taskStatus();
                    observation.lowCardinalityKeyValue("task.status", status);
                    
                    switch (status) {
                        case "SUCCEEDED" -> {
                            return toImageResponse(resp);
                        }
                        case "FAILED", "UNKNOWN" -> {
                            return new ImageResponse(
                                List.of(), 
                                toMetadata(resp)
                            );
                        }
                    }
                }
                
                // 任务仍在进行，抛出临时异常触发重试
                throw new TransientAiException(
                    "Image generation still pending"
                );
            }, 
            // 超时回调
            context -> {
                observation.lowCardinalityKeyValue("timeout", "true");
                return new ImageResponse(
                    List.of(), 
                    toMetadataTimeout(taskId)
                );
            })
        );
    }
    
    /**
     * 提交图像生成任务
     */
    public String submitImageGenTask(ImagePrompt request) {
        // 1. 合并选项
        DashScopeImageOptions imageOptions = toImageOptions(
            request.getOptions()
        );
        
        // 2. 构建请求
        DashScopeImageRequest dashScopeImageRequest = 
            constructImageRequest(request, imageOptions);
        
        // 3. 提交任务
        ResponseEntity<DashScopeImageAsyncResponse> submitResponse = 
            dashScopeImageApi.submitImageGenTask(dashScopeImageRequest);
        
        if (submitResponse == null || submitResponse.getBody() == null) {
            logger.warn("Submit imageGen error, request: {}", request);
            return null;
        }
        
        return submitResponse.getBody().output().taskId();
    }
    
    /**
     * 获取任务结果
     */
    public DashScopeImageAsyncResponse getImageGenTask(String taskId) {
        ResponseEntity<DashScopeImageAsyncResponse> response = 
            dashScopeImageApi.getImageGenTaskResult(taskId);
        
        if (response == null || response.getBody() == null) {
            logger.warn("No image response returned for taskId: {}", taskId);
            return null;
        }
        
        return response.getBody();
    }
    
    /**
     * 构建图像请求
     */
    private DashScopeImageRequest constructImageRequest(
            ImagePrompt imagePrompt,
            DashScopeImageOptions options) {
        
        return new DashScopeImageRequest(
            options.getModel(),
            new DashScopeImageRequestInput(
                imagePrompt.getInstructions().get(0).getText(),
                options.getNegativePrompt(),
                options.getRefImg(),
                options.getFunction(),
                options.getBaseImageUrl(),
                options.getMaskImageUrl(),
                options.getSketchImageUrl()
            ),
            new DashScopeImageRequestParameter(
                options.getStyle(),
                options.getSize(),
                options.getN(),
                options.getSeed(),
                options.getRefStrength(),
                options.getRefMode(),
                options.getPromptExtend(),
                options.getWatermark(),
                options.getSketchWeight(),
                options.getSketchExtraction(),
                options.getSketchColor(),
                options.getMaskColor()
            )
        );
    }
}
```

#### 支持的图像功能

1. **文生图** (Text-to-Image)
2. **图生图** (Image-to-Image)  
3. **图像编辑** (Inpainting)
4. **草图生成** (Sketch-to-Image)
5. **风格迁移** (Style Transfer)
6. **图像放大** (Super Resolution)

#### 配置选项

```java
DashScopeImageOptions options = DashScopeImageOptions.builder()
    .withModel("wanx-v1")              // 模型
    .withSize("1024*1024")             // 尺寸
    .withN(1)                          // 生成数量
    .withStyle("auto")                 // 风格
    .withSeed(42)                      // 随机种子
    .withNegativePrompt("ugly, bad")   // 负面提示词
    .withWatermark(false)              // 水印
    .build();
```

---

### 3. DashScopeEmbeddingModel (向量嵌入模型)

#### 核心实现

```java
public class DashScopeEmbeddingModel extends AbstractEmbeddingModel {
    
    private final DashScopeApi dashScopeApi;
    private final DashScopeEmbeddingOptions defaultOptions;
    private final RetryTemplate retryTemplate;
    private final MetadataMode metadataMode;
    private final ObservationRegistry observationRegistry;
    
    /**
     * 单文本向量化
     */
    @Override
    public float[] embed(Document document) {
        Assert.notNull(document, "Document must not be null");
        return embed(document.getFormattedContent(metadataMode));
    }
    
    /**
     * 批量向量化
     */
    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        // 1. 构建请求
        EmbeddingRequest embeddingRequest = buildEmbeddingRequest(request);
        DashScopeApi.EmbeddingRequest apiRequest = 
            createRequest(embeddingRequest);
        
        // 2. 创建观测上下文
        var observationContext = EmbeddingModelObservationContext.builder()
            .embeddingRequest(embeddingRequest)
            .provider(DashScopeApiConstants.PROVIDER_NAME)
            .build();
        
        // 3. 执行请求（带观测和重试）
        return EmbeddingModelObservationDocumentation.EMBEDDING_MODEL_OPERATION
            .observation(observationConvention, 
                DEFAULT_OBSERVATION_CONVENTION, 
                () -> observationContext,
                observationRegistry)
            .observe(() -> {
                DashScopeApi.EmbeddingList apiEmbeddingResponse = 
                    retryTemplate.execute(ctx -> {
                        try {
                            return dashScopeApi.embeddings(apiRequest).getBody();
                        } catch (Exception e) {
                            logger.error("Error embedding request: {}", 
                                request.getInstructions(), e);
                            throw e;
                        }
                    });
                
                // 4. 校验响应
                if (apiEmbeddingResponse == null) {
                    logger.warn("No embeddings returned for request: {}", request);
                    return new EmbeddingResponse(List.of());
                }
                
                if (apiEmbeddingResponse.message() != null) {
                    logger.error("Error message returned for request: {}", 
                        apiEmbeddingResponse.message());
                    throw new RuntimeException(
                        "Embedding failed: error code:" + 
                        apiEmbeddingResponse.code() + 
                        ", message:" + 
                        apiEmbeddingResponse.message()
                    );
                }
                
                // 5. 提取使用统计
                DashScopeApi.EmbeddingUsage usage = 
                    apiEmbeddingResponse.usage();
                Usage embeddingUsage = usage != null 
                    ? getDefaultUsage(usage) 
                    : new EmptyUsage();
                
                // 6. 转换响应
                var metadata = generateResponseMetadata(
                    apiRequest.model(), 
                    embeddingUsage
                );
                
                List<Embedding> embeddings = apiEmbeddingResponse.output()
                    .embeddings()
                    .stream()
                    .map(e -> new Embedding(e.embedding(), e.textIndex()))
                    .toList();
                
                EmbeddingResponse embeddingResponse = 
                    new EmbeddingResponse(embeddings, metadata);
                
                observationContext.setResponse(embeddingResponse);
                
                return embeddingResponse;
            });
    }
    
    /**
     * 构建请求
     */
    private EmbeddingRequest buildEmbeddingRequest(EmbeddingRequest embeddingRequest) {
        // 1. 处理运行时选项
        DashScopeEmbeddingOptions runtimeOptions = null;
        if (embeddingRequest.getOptions() != null) {
            runtimeOptions = ModelOptionsUtils.copyToTarget(
                embeddingRequest.getOptions(), 
                EmbeddingOptions.class,
                DashScopeEmbeddingOptions.class
            );
        }
        
        // 2. 合并选项
        DashScopeEmbeddingOptions requestOptions = runtimeOptions == null 
            ? defaultOptions
            : DashScopeEmbeddingOptions.builder()
                .withModel(ModelOptionsUtils.mergeOption(
                    runtimeOptions.getModel(), 
                    defaultOptions.getModel()
                ))
                .withDimensions(ModelOptionsUtils.mergeOption(
                    runtimeOptions.getDimensions(),
                    defaultOptions.getDimensions()
                ))
                .withTextType(ModelOptionsUtils.mergeOption(
                    runtimeOptions.getTextType(), 
                    defaultOptions.getTextType()
                ))
                .build();
        
        return new EmbeddingRequest(
            embeddingRequest.getInstructions(), 
            requestOptions
        );
    }
    
    /**
     * 创建 API 请求
     */
    private DashScopeApi.EmbeddingRequest createRequest(EmbeddingRequest request) {
        DashScopeEmbeddingOptions requestOptions = 
            (DashScopeEmbeddingOptions) request.getOptions();
        
        return DashScopeApi.EmbeddingRequest.builder()
            .model(requestOptions.getModel())
            .texts(request.getInstructions())
            .textType(requestOptions.getTextType())
            .dimension(requestOptions.getDimensions())
            .build();
    }
}
```

#### 支持的 Embedding 模型

```java
public enum EmbeddingModel {
    EMBEDDING_V1("text-embedding-v1"),
    EMBEDDING_V2("text-embedding-v2"),     // 默认，性能最佳
    EMBEDDING_V3("text-embedding-v3"),
    EMBEDDING_ASYNC_V1("text-embedding-async-v1"),
    EMBEDDING_ASYNC_V2("text-embedding-async-v2"),
    EMBEDDING_ASYNC_V3("text-embedding-async-v3");
}
```

#### TextType 选项

```java
public enum EmbeddingTextType {
    QUERY("query"),      // 查询文本
    DOCUMENT("document") // 文档文本（默认）
}
```

#### 使用示例

```java
// 1. 基础用法
EmbeddingModel embeddingModel = new DashScopeEmbeddingModel(dashScopeApi);
List<Double> embedding = embeddingModel.embed("Spring AI Alibaba");

// 2. 批量向量化
EmbeddingResponse response = embeddingModel.call(
    new EmbeddingRequest(
        List.of("text1", "text2", "text3"),
        DashScopeEmbeddingOptions.builder()
            .withModel("text-embedding-v2")
            .withTextType("document")
            .withDimensions(1536)
            .build()
    )
);

// 3. 文档向量化
Document doc = new Document("Spring AI content");
float[] vector = embeddingModel.embed(doc);
```

---

### 4. DashScopeRerankModel (重排序模型)

#### 核心实现

```java
public class DashScopeRerankModel implements RerankModel {
    
    private final DashScopeApi dashscopeApi;
    private final RetryTemplate retryTemplate;
    private final DashScopeRerankOptions defaultOptions;
    
    /**
     * 重排序调用
     */
    @Override
    public RerankResponse call(RerankRequest request) {
        // 1. 验证
        Assert.notNull(request.getQuery(), "query must not be null");
        Assert.notNull(request.getInstructions(), 
            "documents must not be null");
        
        // 2. 合并选项
        DashScopeRerankOptions requestOptions = mergeOptions(
            request.getOptions(), 
            defaultOptions
        );
        
        // 3. 创建 API 请求
        DashScopeApi.RerankRequest rerankRequest = 
            createRequest(request, requestOptions);
        
        // 4. 执行请求（带重试）
        ResponseEntity<DashScopeApi.RerankResponse> responseEntity = 
            retryTemplate.execute(ctx -> 
                dashscopeApi.rerankEntity(rerankRequest)
            );
        
        var response = responseEntity.getBody();
        
        // 5. 校验响应
        if (response == null) {
            logger.warn("No rerank returned for query: {}", request.getQuery());
            return new RerankResponse(Collections.emptyList());
        }
        
        // 6. 转换结果（带分数）
        List<DocumentWithScore> documentWithScores = response.output()
            .results()
            .stream()
            .map(data -> DocumentWithScore.builder()
                .withScore(data.relevanceScore())
                .withDocument(request.getInstructions().get(data.index()))
                .build())
            .toList();
        
        // 7. 构建响应
        var metadata = new RerankResponseMetadata(
            DashScopeAiUsage.from(response.usage())
        );
        
        return new RerankResponse(documentWithScores, metadata);
    }
    
    /**
     * 创建 API 请求
     */
    private DashScopeApi.RerankRequest createRequest(
            RerankRequest request, 
            DashScopeRerankOptions requestOptions) {
        
        // 提取文档文本
        List<String> docs = request.getInstructions()
            .stream()
            .map(Document::getText)
            .toList();
        
        // 构建参数
        DashScopeApi.RerankRequestParameter parameter = 
            new DashScopeApi.RerankRequestParameter(
                requestOptions.getTopN(),
                requestOptions.getReturnDocuments()
            );
        
        // 构建输入
        var input = new DashScopeApi.RerankRequestInput(
            request.getQuery(), 
            docs
        );
        
        return new DashScopeApi.RerankRequest(
            requestOptions.getModel(), 
            input, 
            parameter
        );
    }
    
    /**
     * 合并选项
     */
    private DashScopeRerankOptions mergeOptions(
            @Nullable RerankOptions runtimeOptions,
            DashScopeRerankOptions defaultOptions) {
        
        var runtimeOptionsForProvider = ModelOptionsUtils.copyToTarget(
            runtimeOptions, 
            RerankOptions.class,
            DashScopeRerankOptions.class
        );
        
        if (runtimeOptionsForProvider == null) {
            return defaultOptions;
        }
        
        return DashScopeRerankOptions.builder()
            .withModel(ModelOptionsUtils.mergeOption(
                runtimeOptionsForProvider.getModel(), 
                defaultOptions.getModel()
            ))
            .withTopN(ModelOptionsUtils.mergeOption(
                runtimeOptionsForProvider.getTopN(), 
                defaultOptions.getTopN()
            ))
            .withReturnDocuments(ModelOptionsUtils.mergeOption(
                runtimeOptionsForProvider.getReturnDocuments(),
                defaultOptions.getReturnDocuments()
            ))
            .build();
    }
}
```

#### Rerank 使用场景

```java
// 典型的 RAG 流程优化
public List<Document> enhancedRetrieval(String query) {
    // 1. 向量检索（召回更多候选）
    List<Document> candidates = vectorStore.similaritySearch(
        SearchRequest.query(query)
            .withTopK(20)  // 召回 20 个
            .withSimilarityThreshold(0.5)
    );
    
    // 2. Rerank 重排序（精准排序）
    RerankResponse rerankResponse = rerankModel.call(
        new RerankRequest(
            query,
            candidates,
            DashScopeRerankOptions.builder()
                .withModel("gte-rerank")
                .withTopN(5)  // 返回 Top 5
                .build()
        )
    );
    
    // 3. 返回重排序后的文档
    return rerankResponse.getResults()
        .stream()
        .map(DocumentWithScore::getOutput)
        .toList();
}
```

---

## API客户端实现

### 1. DashScopeApi (核心 API 客户端)

#### 架构设计

```
┌────────────────────────────────────────┐
│         DashScopeApi                    │
├────────────────────────────────────────┤
│ - RestClient  (同步调用)                │
│ - WebClient   (异步/流式调用)           │
│ - OkHttpClient (文件上传)               │
├────────────────────────────────────────┤
│ + chatCompletionEntity()               │
│ + chatCompletionStream()               │
│ + embeddings()                         │
│ + rerankEntity()                       │
│ + documentSplit()                      │
│ + uploadFile()                         │
└────────────────────────────────────────┘
```

#### 初始化

```java
public class DashScopeApi {
    
    private final String baseUrl;
    private final ApiKey apiKey;
    private final MultiValueMap<String, String> headers;
    private final RestClient restClient;
    private final WebClient webClient;
    private final ResponseErrorHandler responseErrorHandler;
    
    public static final String DEFAULT_BASE_URL = 
        "https://dashscope.aliyuncs.com";
    
    public DashScopeApi(
            String baseUrl,
            ApiKey apiKey,
            MultiValueMap<String, String> header,
            String completionsPath,
            String embeddingsPath,
            String workSpaceId,
            RestClient.Builder restClientBuilder,
            WebClient.Builder webClientBuilder,
            ResponseErrorHandler responseErrorHandler) {
        
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.headers = header;
        this.responseErrorHandler = responseErrorHandler;
        
        // 添加工作空间 ID 到 headers
        if (StringUtils.hasText(workSpaceId)) {
            this.headers.add(
                DashScopeApiConstants.HEADER_WORK_SPACE_ID, 
                workSpaceId
            );
        }
        
        // 配置 Headers
        Consumer<HttpHeaders> finalHeaders = h -> {
            // API Key 认证
            if (!(apiKey instanceof NoopApiKey)) {
                h.setBearerAuth(apiKey.getValue());
            }
            h.setContentType(MediaType.APPLICATION_JSON);
            h.addAll(headers);
        };
        
        // 构建 RestClient
        this.restClient = restClientBuilder.clone()
            .baseUrl(baseUrl)
            .defaultHeaders(finalHeaders)
            .defaultStatusHandler(responseErrorHandler)
            .build();
        
        // 构建 WebClient
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeaders(finalHeaders)
            .build();
    }
}
```

#### 聊天补全 API

```java
/**
 * 同步聊天补全
 */
public ResponseEntity<ChatCompletion> chatCompletionEntity(
        ChatCompletionRequest chatCompletionRequest) {
    
    return chatCompletionEntity(chatCompletionRequest, null);
}

public ResponseEntity<ChatCompletion> chatCompletionEntity(
        ChatCompletionRequest chatCompletionRequest,
        MultiValueMap<String, String> additionalHeaders) {
    
    Assert.notNull(chatCompletionRequest, 
        "The request body can not be null.");
    Assert.isTrue(!chatCompletionRequest.stream(), 
        "Request must set the stream property to false.");
    
    return restClient.post()
        .uri("/api/v1/services/aigc/text-generation/generation")
        .headers(h -> {
            if (additionalHeaders != null) {
                h.addAll(additionalHeaders);
            }
        })
        .body(chatCompletionRequest)
        .retrieve()
        .toEntity(ChatCompletion.class);
}

/**
 * 流式聊天补全
 */
public Flux<ChatCompletionChunk> chatCompletionStream(
        ChatCompletionRequest chatCompletionRequest) {
    
    return chatCompletionStream(chatCompletionRequest, null);
}

public Flux<ChatCompletionChunk> chatCompletionStream(
        ChatCompletionRequest chatCompletionRequest,
        MultiValueMap<String, String> additionalHeaders) {
    
    Assert.notNull(chatCompletionRequest, 
        "The request body can not be null.");
    Assert.isTrue(chatCompletionRequest.stream(), 
        "Request must set the stream property to true.");
    
    return webClient.post()
        .uri("/api/v1/services/aigc/text-generation/generation")
        .headers(h -> {
            if (additionalHeaders != null) {
                h.addAll(additionalHeaders);
            }
        })
        .body(Mono.just(chatCompletionRequest), ChatCompletionRequest.class)
        .retrieve()
        .bodyToFlux(String.class)
        .takeUntil(SSE_DONE_PREDICATE)
        .filter(content -> !SSE_DONE_PREDICATE.test(content))
        .map(content -> ModelOptionsUtils.jsonToObject(
            content, 
            ChatCompletionChunk.class
        ));
}
```

#### 向量嵌入 API

```java
/**
 * 向量嵌入
 */
public ResponseEntity<EmbeddingList> embeddings(EmbeddingRequest embeddingRequest) {
    Assert.notNull(embeddingRequest, "The request body can not be null.");
    
    return restClient.post()
        .uri("/api/v1/services/embeddings/text-embedding/text-embedding")
        .body(embeddingRequest)
        .retrieve()
        .toEntity(EmbeddingList.class);
}
```

#### 文档重排序 API

```java
/**
 * 文档重排序
 */
public ResponseEntity<RerankResponse> rerankEntity(RerankRequest rerankRequest) {
    Assert.notNull(rerankRequest, "The request body can not be null.");
    
    return restClient.post()
        .uri("/api/v1/services/rerank")
        .body(rerankRequest)
        .retrieve()
        .toEntity(RerankResponse.class);
}
```

#### 文件上传 API

```java
/**
 * 上传文件到知识库
 */
public void uploadDocument(File file, String categoryId) {
    try {
        // 1. 获取上传凭证
        UploadRequest uploadRequest = new UploadRequest(categoryId);
        ResponseEntity<UploadLeaseResponse> leaseResponse = 
            uploadLease(uploadRequest);
        
        if (leaseResponse.getBody() == null) {
            throw new DashScopeException(ErrorCodeEnum.READER_ADD_FILE_ERROR);
        }
        
        // 2. 上传文件
        uploadFile(file, leaseResponse.getBody());
        
        // 3. 添加文档
        AddDocumentRequest addDocumentRequest = new AddDocumentRequest(
            categoryId,
            leaseResponse.getBody().data.fileId,
            file.getName()
        );
        
        ResponseEntity<CommonResponse<DocumentAddResponse>> addResponse = 
            restClient.post()
                .uri("/api/v1/datacenter/docs")
                .body(addDocumentRequest)
                .retrieve()
                .toEntity(
                    new ParameterizedTypeReference<
                        CommonResponse<DocumentAddResponse>
                    >() {}
                );
        
        if (addResponse.getBody() == null || 
            !"success".equals(addResponse.getBody().code())) {
            throw new DashScopeException(ErrorCodeEnum.READER_ADD_FILE_ERROR);
        }
    } catch (Exception ex) {
        throw new DashScopeException(ErrorCodeEnum.READER_ADD_FILE_ERROR, ex);
    }
}

/**
 * 使用 OkHttp 上传文件
 */
private void uploadFile(File file, UploadLeaseResponse uploadLeaseResponse) {
    try {
        UploadLeaseParamData uploadParam = uploadLeaseResponse.data.param;
        
        // 创建 OkHttp 客户端
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        
        // 构建 Headers
        okhttp3.Headers.Builder headersBuilder = 
            new okhttp3.Headers.Builder();
        String contentType = uploadParam.header.remove("Content-Type");
        
        for (String key : uploadParam.header.keySet()) {
            headersBuilder.add(key, uploadParam.header.get(key));
        }
        
        // 构建 RequestBody
        RequestBody requestBody;
        if (StringUtils.hasLength(contentType)) {
            requestBody = RequestBody.create(
                file, 
                okhttp3.MediaType.parse(contentType)
            );
        } else {
            requestBody = RequestBody.create(file, null);
            headersBuilder.add("Content-Type", "");
        }
        
        // 构建请求
        Request request = new Request.Builder()
            .url(uploadParam.url)
            .headers(headersBuilder.build())
            .put(requestBody)
            .build();
        
        // 执行上传
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Unexpected response code: " + 
                    response.code());
            }
        }
    } catch (Exception ex) {
        throw new DashScopeException("Upload File Failed", ex);
    }
}
```

---

## RAG支持

### 1. DocumentRetrievalAdvisor (文档检索增强器)

#### 核心实现

```java
public class DocumentRetrievalAdvisor implements BaseAdvisor {
    
    // 默认 Prompt 模板
    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = 
        new PromptTemplate("""
            {query}
            
            Context information is below, surrounded by ---------------------
            ---------------------
            {question_answer_context}
            ---------------------
            Given the context and provided history information and not prior knowledge,
            reply to the user comment. If the answer is not in the context, inform
            the user that you can't answer the question.
            """);
    
    public static String RETRIEVED_DOCUMENTS = "question_answer_context";
    
    private final DocumentRetriever retriever;
    private final PromptTemplate promptTemplate;
    private final int order;
    
    /**
     * 请求前置处理 - 检索文档并增强 Prompt
     */
    @Override
    public ChatClientRequest before(ChatClientRequest request, 
            AdvisorChain advisorChain) {
        
        var context = request.context();
        var userMessage = request.prompt().getUserMessage();
        
        // 1. 构建查询
        Query query = new Query(
            userMessage.getText(), 
            request.prompt().getInstructions(), 
            context
        );
        
        // 2. 检索文档
        List<Document> documents = retriever.retrieve(query);
        context.put(RETRIEVED_DOCUMENTS, documents);
        
        // 3. 构建文档上下文
        String documentContext = documents.stream()
            .map(Document::getText)
            .collect(Collectors.joining(System.lineSeparator()));
        
        // 4. 增强用户消息
        String augmentedUserText = promptTemplate.render(Map.of(
            "query", userMessage.getText(),
            "question_answer_context", documentContext
        ));
        
        // 5. 更新请求
        return request.mutate()
            .prompt(request.prompt().augmentUserMessage(augmentedUserText))
            .context(context)
            .build();
    }
    
    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, 
            AdvisorChain advisorChain) {
        // 将检索到的文档添加到响应元数据
        ChatResponse.Builder chatResponseBuilder;
        if (chatClientResponse.chatResponse() == null) {
            chatResponseBuilder = ChatResponse.builder();
        } else {
            chatResponseBuilder = ChatResponse.builder()
                .from(chatClientResponse.chatResponse());
        }
        
        chatResponseBuilder.metadata(
            RETRIEVED_DOCUMENTS, 
            chatClientResponse.context().get(RETRIEVED_DOCUMENTS)
        );
        
        return ChatClientResponse.builder()
            .chatResponse(chatResponseBuilder.build())
            .context(chatClientResponse.context())
            .build();
    }
}
```

### 2. RetrievalRerankAdvisor (检索+重排序)

```java
public class RetrievalRerankAdvisor implements BaseAdvisor {
    
    private final VectorStore vectorStore;
    private final RerankModel rerankModel;
    private final PromptTemplate promptTemplate;
    private final SearchRequest searchRequest;
    private final Double minScore;  // 最小相关性分数阈值
    
    @Override
    public ChatClientRequest before(ChatClientRequest request, 
            AdvisorChain advisorChain) {
        
        var context = request.context();
        var userMessage = request.prompt().getUserMessage();
        
        // 1. 向量检索（召回阶段）
        var searchRequestToUse = SearchRequest.from(searchRequest)
            .query(userMessage.getText())
            .filterExpression(doGetFilterExpression(context))
            .build();
        
        List<Document> documents = vectorStore.similaritySearch(
            searchRequestToUse
        );
        context.put(RETRIEVED_DOCUMENTS, documents);
        
        // 2. 重排序（精排阶段）
        documents = doRerank(request, documents);
        
        // 3. 构建增强 Prompt
        String documentContext = documents.stream()
            .map(Document::getText)
            .collect(Collectors.joining(System.lineSeparator()));
        
        String augmentedUserText = promptTemplate.render(Map.of(
            "query", userMessage.getText(),
            "question_answer_context", documentContext
        ));
        
        // 4. 更新请求
        return request.mutate()
            .prompt(request.prompt().augmentUserMessage(augmentedUserText))
            .context(context)
            .build();
    }
    
    /**
     * 重排序实现
     */
    protected List<Document> doRerank(ChatClientRequest request, 
            List<Document> documents) {
        
        if (CollectionUtils.isEmpty(documents)) {
            return documents;
        }
        
        // 1. 调用 Rerank 模型
        var rerankRequest = new RerankRequest(
            request.prompt().getUserMessage().getText(), 
            documents
        );
        
        RerankResponse response = rerankModel.call(rerankRequest);
        logger.debug("reranked documents: {}", response);
        
        if (response == null || response.getResults() == null) {
            return documents;
        }
        
        // 2. 过滤和排序
        return response.getResults()
            .stream()
            .filter(doc -> doc != null && doc.getScore() >= minScore)
            .sorted(Comparator.comparingDouble(
                DocumentWithScore::getScore).reversed())
            .map(DocumentWithScore::getOutput)
            .collect(Collectors.toList());
    }
}
```

### 3. CompositeDocumentRetriever (多向量库检索)

```java
public class CompositeDocumentRetriever implements DocumentRetriever {
    
    private final List<DocumentRetriever> retrievers;
    private final int maxResultsPerRetriever;
    private final ResultMergeStrategy mergeStrategy;
    
    public enum ResultMergeStrategy {
        /**
         * 简单合并：按顺序拼接所有检索器的结果
         */
        SIMPLE_CONCAT,
        
        /**
         * 分数合并：根据相似度分数排序合并
         */
        SCORE_MERGE,
        
        /**
         * 去重合并：移除重复文档后合并
         */
        DEDUPLICATE_MERGE,
        
        /**
         * RRF (Reciprocal Rank Fusion) 合并
         */
        RRF_MERGE
    }
    
    @Override
    public List<Document> retrieve(Query query) {
        List<List<Document>> allResults = new ArrayList<>();
        
        // 1. 并行检索所有向量库
        for (DocumentRetriever retriever : retrievers) {
            try {
                List<Document> results = retriever.retrieve(query);
                // 限制每个检索器的结果数量
                if (results.size() > maxResultsPerRetriever) {
                    results = results.subList(0, maxResultsPerRetriever);
                }
                allResults.add(results);
            } catch (Exception e) {
                logger.warn("Retriever failed: {}", 
                    retriever.getClass().getSimpleName(), e);
            }
        }
        
        // 2. 根据策略合并结果
        return mergeResults(allResults, mergeStrategy);
    }
    
    /**
     * 合并结果
     */
    private List<Document> mergeResults(
            List<List<Document>> allResults, 
            ResultMergeStrategy strategy) {
        
        return switch (strategy) {
            case SIMPLE_CONCAT -> simpleConcat(allResults);
            case SCORE_MERGE -> scoreMerge(allResults);
            case DEDUPLICATE_MERGE -> deduplicateMerge(allResults);
            case RRF_MERGE -> rrfMerge(allResults);
        };
    }
    
    /**
     * RRF (Reciprocal Rank Fusion) 合并
     * RRF 分数 = Σ(1 / (k + rank_i))
     * k 通常取 60
     */
    private List<Document> rrfMerge(List<List<Document>> allResults) {
        final int K = 60;
        Map<Document, Double> documentScores = new HashMap<>();
        
        for (List<Document> results : allResults) {
            for (int rank = 0; rank < results.size(); rank++) {
                Document doc = results.get(rank);
                double rrfScore = 1.0 / (K + rank + 1);
                documentScores.merge(doc, rrfScore, Double::sum);
            }
        }
        
        return documentScores.entrySet()
            .stream()
            .sorted(Map.Entry.<Document, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .toList();
    }
}
```

### RAG 完整示例

```java
@Configuration
public class RagConfiguration {
    
    @Bean
    public ChatClient ragChatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore,
            RerankModel rerankModel) {
        
        // 方案1: 简单检索增强
        DocumentRetrievalAdvisor retrievalAdvisor = 
            new DocumentRetrievalAdvisor(
                vectorStore,
                SearchRequest.defaults()
                    .withTopK(10)
                    .withSimilarityThreshold(0.7)
            );
        
        // 方案2: 检索 + 重排序（推荐）
        RetrievalRerankAdvisor rerankAdvisor = 
            new RetrievalRerankAdvisor(
                vectorStore,
                rerankModel,
                SearchRequest.defaults()
                    .withTopK(20),  // 召回 20 个
                DEFAULT_PROMPT_TEMPLATE,
                0.3  // 重排序最小分数阈值
            );
        
        // 方案3: 多向量库 + 重排序
        List<DocumentRetriever> retrievers = List.of(
            vectorStore1,
            vectorStore2,
            vectorStore3
        );
        
        CompositeDocumentRetriever compositeRetriever = 
            new CompositeDocumentRetriever(
                retrievers,
                10,  // 每个检索器返回10个
                ResultMergeStrategy.RRF_MERGE
            );
        
        DocumentRetrievalAdvisor multiStoreAdvisor = 
            new DocumentRetrievalAdvisor(compositeRetriever);
        
        return builder
            .defaultAdvisors(rerankAdvisor)  // 使用方案2
            .build();
    }
}
```

---

## 可观测性

### 1. Chat Model 可观测

```java
public class DashScopeChatModel implements ChatModel {
    
    private final ObservationRegistry observationRegistry;
    private ChatModelObservationConvention observationConvention;
    
    @Override
    public ChatResponse call(Prompt prompt) {
        // 创建观测上下文
        ChatModelObservationContext observationContext = 
            ChatModelObservationContext.builder()
                .prompt(prompt)
                .provider(DashScopeApiConstants.PROVIDER_NAME)
                .build();
        
        // 执行观测
        return ChatModelObservationDocumentation.CHAT_MODEL_OPERATION
            .observation(observationConvention, 
                DEFAULT_OBSERVATION_CONVENTION, 
                () -> observationContext,
                observationRegistry)
            .observe(() -> {
                // 实际调用
                ChatResponse response = callApi(prompt);
                
                // 设置响应到上下文
                observationContext.setResponse(response);
                
                return response;
            });
    }
}
```

### 2. 自定义观测 Convention

```java
public class DashScopeChatModelObservationConvention 
        extends DefaultChatModelObservationConvention {
    
    @Override
    public KeyValues getHighCardinalityKeyValues(
            ChatModelObservationContext context) {
        
        KeyValues keyValues = super.getHighCardinalityKeyValues(context);
        
        // 添加自定义标签
        if (context.getRequest() != null) {
            keyValues = keyValues
                .and(KeyValue.of("ai.model.provider", "dashscope"))
                .and(KeyValue.of("ai.model.name", 
                    context.getRequest().getOptions().getModel()))
                .and(KeyValue.of("ai.request.temperature", 
                    String.valueOf(context.getRequest()
                        .getOptions().getTemperature())));
        }
        
        if (context.getResponse() != null) {
            var usage = context.getResponse().getMetadata().getUsage();
            keyValues = keyValues
                .and(KeyValue.of("ai.usage.input_tokens", 
                    String.valueOf(usage.getPromptTokens())))
                .and(KeyValue.of("ai.usage.output_tokens", 
                    String.valueOf(usage.getCompletionTokens())))
                .and(KeyValue.of("ai.usage.total_tokens", 
                    String.valueOf(usage.getTotalTokens())));
        }
        
        return keyValues;
    }
}
```

### 3. 导出到 ARMS

```yaml
management:
  tracing:
    enabled: true
    sampling:
      probability: 1.0
  otlp:
    tracing:
      endpoint: ${ARMS_ENDPOINT}
      headers:
        Authentication: ${ARMS_API_KEY}
```

---

## 配置与使用

### 1. Maven 依赖

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.alibaba.cloud.ai</groupId>
      <artifactId>spring-ai-alibaba-bom</artifactId>
      <version>1.0.0.2</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <!-- Core 模块通过 Starter 引入 -->
  <dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-dashscope</artifactId>
  </dependency>
</dependencies>
```

### 2. 配置文件

```yaml
spring:
  ai:
    alibaba:
      dashscope:
        # API Key（必需）
        api-key: ${DASHSCOPE_API_KEY}
        
        # 工作空间 ID（可选）
        workspace-id: ${WORKSPACE_ID}
        
        # 基础 URL（可选，默认官方地址）
        base-url: https://dashscope.aliyuncs.com
        
        # Chat Model 配置
        chat:
          enabled: true
          options:
            model: qwen-max
            temperature: 0.7
            top-p: 0.9
            max-tokens: 1500
        
        # Embedding Model 配置
        embedding:
          enabled: true
          options:
            model: text-embedding-v2
            text-type: document
            dimensions: 1536
        
        # Image Model 配置
        image:
          enabled: true
          options:
            model: wanx-v1
            size: "1024*1024"
            n: 1
```

### 3. 程序化配置

```java
@Configuration
public class DashScopeConfiguration {
    
    @Bean
    public DashScopeApi dashScopeApi(
            @Value("${spring.ai.alibaba.dashscope.api-key}") String apiKey,
            @Value("${spring.ai.alibaba.dashscope.base-url}") String baseUrl) {
        
        return DashScopeApi.builder()
            .baseUrl(baseUrl)
            .apiKey(new SimpleApiKey(apiKey))
            .restClientBuilder(RestClient.builder())
            .webClientBuilder(WebClient.builder())
            .responseErrorHandler(RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER)
            .build();
    }
    
    @Bean
    public DashScopeChatModel chatModel(DashScopeApi dashScopeApi) {
        return new DashScopeChatModel(
            dashScopeApi,
            DashScopeChatOptions.builder()
                .withModel("qwen-max")
                .withTemperature(0.7)
                .build(),
            ToolCallingManager.builder().build(),
            RetryUtils.DEFAULT_RETRY_TEMPLATE,
            ObservationRegistry.NOOP
        );
    }
    
    @Bean
    public DashScopeEmbeddingModel embeddingModel(DashScopeApi dashScopeApi) {
        return new DashScopeEmbeddingModel(
            dashScopeApi,
            MetadataMode.EMBED,
            DashScopeEmbeddingOptions.builder()
                .withModel("text-embedding-v2")
                .build()
        );
    }
    
    @Bean
    public DashScopeRerankModel rerankModel(DashScopeApi dashScopeApi) {
        return new DashScopeRerankModel(
            dashScopeApi,
            DashScopeRerankOptions.builder()
                .withModel("gte-rerank")
                .withTopN(5)
                .build()
        );
    }
}
```

### 4. 使用示例

#### 基础聊天

```java
@RestController
public class ChatController {
    
    private final ChatClient chatClient;
    
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
    
    @GetMapping("/chat/stream")
    public Flux<String> chatStream(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }
}
```

#### RAG 应用

```java
@Service
public class RagService {
    
    private final ChatClient ragChatClient;
    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    
    /**
     * 添加文档到向量库
     */
    public void addDocuments(List<String> texts) {
        List<Document> documents = texts.stream()
            .map(Document::new)
            .toList();
        
        vectorStore.add(documents);
    }
    
    /**
     * RAG 查询
     */
    public String queryWithContext(String question) {
        return ragChatClient.prompt()
            .user(question)
            .call()
            .content();
    }
}
```

---

## 总结

### 核心优势

1. **完整的模型支持** - Chat、Image、Audio、Video、Embedding、Rerank
2. **Spring AI 标准适配** - 完全兼容 Spring AI 接口
3. **企业级特性** - 重试、可观测、错误处理
4. **RAG 完整方案** - 检索、重排序、多向量库
5. **流式响应支持** - 同时支持同步和异步
6. **Function Calling** - 完整的工具调用能力

### 设计模式

1. **适配器模式** - 适配 Spring AI 接口到 DashScope API
2. **构建器模式** - Options 和 Request 构建
3. **策略模式** - 重试策略、合并策略
4. **模板方法** - Prompt 模板
5. **观察者模式** - Observation 可观测性

### 扩展点

1. **自定义 Advisor** - 实现 BaseAdvisor 接口
2. **自定义 Observation Convention** - 扩展可观测标签
3. **自定义 DocumentRetriever** - 实现检索逻辑
4. **自定义 RetryTemplate** - 定制重试策略

---

**文档版本**: v1.0  
**最后更新**: 2025-10-02  
**模块版本**: 1.1.0.0-SNAPSHOT

