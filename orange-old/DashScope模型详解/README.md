# DashScope 模型详解系列

> **系列说明**：本系列文档详细介绍 Spring AI Alibaba 中与阿里云百炼（DashScope）相关的核心模型实现类。每个文档深入剖析一个模型类的设计、实现、使用和最佳实践。

---

## 📚 文档导航

### 1️⃣ [DashScopeChatModel 详解](./1.1-DashScopeChatModel详解.md) ⭐⭐⭐ 🔥 必读

**模型类型**: 对话模型（Chat Model）  
**核心功能**: 文本对话、多轮对话、Function Calling、多模态  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 核心特性（7大特性）
  - 同步调用
  - 流式调用
  - Function Calling
  - 多轮对话
  - 多模态支持
  - 重试机制
  - 可观测性

✓ 类结构与依赖详解
✓ 核心功能详解
  - 同步对话调用（call()）
  - 流式对话调用（stream()）
  - Function Calling 工作流
  - 多模态输入（图片/视频/音频）
  - 重试机制
  - 可观测性集成

✓ 构造方法与初始化
  - 6种构造方法
  - Builder模式
  - Spring Boot自动配置

✓ 主要方法详解（源码级）
  - call() - 同步调用
  - stream() - 流式调用
  - createRequest() - 请求构建
  - toChatResponse() - 响应转换
  - convertMediaContent() - 多模态转换

✓ 配置选项
  - 基础配置
  - DashScope特有配置
  - 工具调用配置
  - 多模态配置

✓ 工作原理
  - 同步调用时序图
  - 流式调用时序图
  - Function Calling流程图

✓ 完整使用示例
  - Spring Boot简单使用
  - 使用ChatClient（推荐）
  - Function Calling示例
  - 多模态示例
  - 高级配置示例

✓ 最佳实践
  - 配置管理策略
  - 性能优化技巧
  - 错误处理方案
  - 日志和监控

✓ 注意事项与FAQ
  - 6个核心注意事项
  - 5个常见问题详解
```

**适合人群**: 所有开发者，必读文档  
**阅读时长**: 约 30-40 分钟

---

### 2️⃣ [DashScopeImageModel 详解](./1.2-DashScopeImageModel详解.md) ⭐⭐⭐

**模型类型**: 图像生成模型（Image Generation Model）  
**核心功能**: 文生图、图生图、风格控制、负提示词  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 核心特性（8大特性）
  - 异步任务模式
  - 多种生成模式
  - 丰富的风格
  - 高清输出
  - 负提示词
  - 重试机制
  - 可观测性
  - 批量生成

✓ 支持的模型
  - wanx-v1（基础版）
  - wanx-x-painting-v1（绘画版）
  - wanx-2.1（高质量）
  - wanx-sketch-to-image-v1（草图转图）

✓ 核心功能详解
  - 异步任务模式
  - 文生图（T2I）
  - 图生图
  - 负提示词
  - 风格控制（10+种预设风格）
  - 批量生成

✓ 主要方法详解
  - call() - 生成图像
  - submitImageGenTask() - 提交任务
  - getImageGenTask() - 获取结果
  - toImageResponse() - 响应转换

✓ 配置选项
  - 基础配置（model, n, size, seed）
  - 风格配置
  - 参考图配置
  - 高级配置

✓ 工作原理
  - 异步任务时序图
  - 重试策略详解

✓ 完整使用示例
  - 基础图像生成
  - 高质量图像生成
  - 参考图生成
  - REST API示例

✓ 最佳实践
  - 提示词优化技巧
  - 负提示词使用
  - 性能优化
  - 错误处理
  - 图片下载和保存

✓ 注意事项与FAQ
  - 6个核心注意事项
  - 5个常见问题详解
```

**适合人群**: AI绘画、图像生成应用开发者  
**阅读时长**: 约 25-30 分钟

---

### 3️⃣ [DashScopeEmbeddingModel 详解](./1.3-DashScopeEmbeddingModel详解.md) ⭐⭐⭐ 🔥 RAG必读

**模型类型**: 嵌入向量模型（Embedding Model）  
**核心功能**: 文本向量化、语义搜索、RAG基础  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 什么是Embedding？
  - 向量化原理
  - 相似度计算
  - 应用场景

✓ 核心特性（8大特性）
  - 高质量向量
  - 多种维度（512/768/1024/1536）
  - 批量处理
  - 文档嵌入
  - MetadataMode
  - TextType（query/document）
  - 可观测性
  - 重试机制

✓ 支持的模型
  - text-embedding-v3（推荐）
  - text-embedding-v2
  - text-embedding-v1
  - text-embedding-v1-multilingual

✓ 核心功能详解
  - 单个文本嵌入
  - 批量文本嵌入
  - 文档嵌入
  - EmbeddingRequest使用
  - TextType：query vs document
  - 向量维度选择

✓ 主要方法详解
  - call() - 核心嵌入方法
  - embed(String) - 单个文本
  - embed(List<String>) - 批量文本
  - embed(Document) - 文档嵌入
  - embedForResponse() - 完整响应
  - buildEmbeddingRequest() - 请求构建

✓ MetadataMode详解
  - ALL / EMBED / NONE
  - 使用场景和区别

✓ 配置选项
  - 基础配置
  - 模型选择
  - 维度选择
  - TextType选择

✓ 工作原理
  - 同步调用时序图
  - 向量化流程

✓ 完整使用示例
  - Spring Boot简单使用
  - 语义搜索示例
  - RAG应用示例
  - 文本聚类示例
  - REST API示例

✓ 最佳实践
  - 批量处理优化
  - 缓存嵌入结果
  - 维度选择策略
  - TextType最佳实践
  - 错误处理

✓ 注意事项与FAQ
  - 6个核心注意事项
  - 5个常见问题详解
```

**适合人群**: RAG、语义搜索、推荐系统开发者  
**阅读时长**: 约 25-30 分钟

---

### 4️⃣ [DashScopeRerankModel 详解](./1.4-DashScopeRerankModel详解.md) ⭐⭐⭐ 🔥 RAG进阶必读

**模型类型**: 重排序模型（Rerank Model）  
**核心功能**: 文档重排序、相关性打分、RAG质量提升  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 什么是Rerank？
  - 重排序原理
  - 为什么需要Rerank
  - 与向量检索的区别

✓ 核心特性（6大特性）
  - 精准打分
  - 批量重排
  - Top-N筛选
  - 文档保留
  - 重试机制
  - 简单易用

✓ 核心功能详解
  - 基本重排序
  - Top-N筛选
  - 分数过滤
  - 与VectorStore集成
  - 使用量统计

✓ 主要方法详解
  - call() - 执行重排序
  - createRequest() - 创建API请求
  - mergeOptions() - 合并配置

✓ 完整使用示例
  - 基础重排序
  - RAG完整流程
  - 批量文档评分
  - REST API示例
  - 对比测试

✓ 最佳实践
  - 召回与重排的平衡
  - Top-N参数选择
  - 分数阈值过滤
  - 缓存策略
  - 错误处理

✓ 注意事项与FAQ
  - 6个核心注意事项
  - 5个常见问题详解
```

**适合人群**: RAG应用、搜索引擎、推荐系统开发者  
**阅读时长**: 约 20-25 分钟

---

### 5️⃣ [DashScopeAudioSpeechModel 详解](./1.5-DashScopeAudioSpeechModel详解.md) ⭐⭐

**模型类型**: 语音合成模型（Text-to-Speech / TTS）  
**核心功能**: 文本转语音、多音色、语速控制  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 什么是TTS？

✓ 核心特性（7大特性）
  - 流式输出
  - 多种音色
  - 语速控制
  - 音调控制
  - 多种格式
  - 高质量音频
  - 时间戳支持

✓ 支持的音色
  - 知性女声、温柔女声、活力女声
  - 标准男声、情感男声
  - 童声

✓ 核心功能详解
  - 基本语音合成
  - 流式语音合成
  - 自定义音色和语速
  - 多种音频格式
  - 时间戳获取

✓ 主要方法详解
  - call() - 同步合成
  - stream() - 流式合成
  - createRequest() - 创建请求

✓ 完整使用示例
  - 基础文本转语音
  - 流式实时播放
  - 批量文本转语音
  - 多音色对比
  - REST API示例

✓ 最佳实践
  - 音色选择
  - 语速控制
  - 音频格式选择
  - 错误处理

✓ 注意事项与FAQ
```

**适合人群**: 有声阅读、智能客服、语音助手开发者  
**阅读时长**: 约 15-20 分钟

---

### 6️⃣ [DashScopeAudioTranscriptionModel 详解](./1.6-DashScopeAudioTranscriptionModel详解.md) ⭐⭐

**模型类型**: 语音识别模型（Speech-to-Text / ASR）  
**核心功能**: 语音转文本、实时识别、热词定制  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 什么是ASR？

✓ 核心特性（6大特性）
  - 异步识别
  - 同步识别
  - 实时流式
  - 热词定制
  - 多语言
  - 去除口语化

✓ 三种识别模式
  - 异步识别（录音文件）
  - 同步识别（简单场景）
  - 实时流式（实时对话）

✓ 主要方法详解
  - asyncCall() - 异步提交
  - fetch() - 获取结果
  - call() - 同步识别
  - stream() - 流式识别

✓ 完整使用示例
  - 录音文件识别
  - 实时语音识别
  - 批量文件识别
  - REST API示例

✓ 最佳实践
  - 识别模式选择
  - 音频格式要求
  - 错误处理
  - 性能优化

✓ 注意事项与FAQ
```

**适合人群**: 语音助手、会议记录、字幕生成开发者  
**阅读时长**: 约 15-20 分钟

---

### 7️⃣ [DashScopeVideoModel 详解](./1.7-DashScopeVideoModel详解.md) ⭐⭐

**模型类型**: 视频生成模型（Video Generation Model）  
**核心功能**: 文生视频、图生视频、首尾帧控制  
**文档版本**: v1.0

**涵盖内容**:
```
✓ 类概述与设计目的
✓ 什么是视频生成？

✓ 核心特性（7大特性）
  - 异步任务模式
  - 文生视频（T2V）
  - 图生视频（I2V）
  - 首尾帧控制
  - 时长控制
  - 分辨率控制
  - 负提示词

✓ 核心功能详解
  - 异步任务模式
  - 文生视频
  - 图生视频
  - 首尾帧控制
  - 负提示词

✓ 主要方法详解
  - call() - 生成视频
  - submitGenTask() - 提交任务
  - getVideoTask() - 获取状态

✓ 完整使用示例
  - 基础视频生成
  - 高级视频生成
  - 异步任务处理
  - 批量视频生成
  - REST API示例

✓ 最佳实践
  - 提示词优化
  - 分辨率选择
  - 时长控制
  - 错误处理

```

**适合人群**: 短视频创作、营销素材、内容创作开发者  
**阅读时长**: 约 15-20 分钟

---

## 二、API客户端层（API Client Layer）

### 8️⃣ [DashScopeApi 详解](./API客户端层/2.1-DashScopeApi详解.md) ⭐⭐⭐ 🔥 **底层核心**

**类别**：核心API客户端（Core API Client）  
**复杂度**：⭐⭐⭐⭐⭐  
**重要性**：⭐⭐⭐

**涵盖内容**：
```
✓ 所有模型的底层HTTP通信基础
✓ 双客户端架构（RestClient + WebClient）
✓ 6大服务：对话/嵌入/重排序/文件/文档/RAG
✓ 同步和流式调用机制
✓ 30+数据结构详解
✓ 完整的错误处理和重试机制
```

**适合人群**: 框架开发者、需要深入理解底层的高级开发者  
**阅读时长**: 约 40-50 分钟

---

### 9️⃣ [DashScopeImageApi 详解](./API客户端层/2.2-DashScopeImageApi详解.md) ⭐⭐⭐

**类别**：图像API客户端（Image API Client）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ 图像生成服务的HTTP通信
✓ 异步任务模式（提交-轮询）
✓ 8个图像模型支持
✓ 完整的请求/响应结构
✓ 智能轮询策略（指数退避）
✓ 批量任务管理
```

**适合人群**: 图像生成应用开发者、需要批量生成的开发者  
**阅读时长**: 约 25-30 分钟

---

---

### 🔟 [DashScopeAudioSpeechApi 详解](./API客户端层/2.3-DashScopeAudioSpeechApi详解.md) ⭐⭐

**类别**：语音合成API客户端（TTS API Client）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ WebSocket流式通信机制
✓ 实时音频流输出
✓ 6种音色支持
✓ 多种音频格式（PCM/WAV/MP3）
✓ 时间戳支持（音素/单词级）
✓ SSML标记语言
```

**适合人群**: 语音合成应用开发者、有声阅读、智能客服开发者  
**阅读时长**: 约 25-30 分钟

---

### 1️⃣1️⃣ [DashScopeAudioTranscriptionApi 详解](./API客户端层/2.4-DashScopeAudioTranscriptionApi详解.md) ⭐⭐⭐

**类别**：语音识别API客户端（ASR API Client）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ 双模式设计（REST + WebSocket）
✓ 异步任务识别（录音文件）
✓ 实时流式识别（实时对话）
✓ 热词定制支持
✓ 详细的时间戳（句子/单词级）
✓ 多人对话识别
```

**适合人群**: 语音识别应用开发者、会议记录、字幕生成开发者  
**阅读时长**: 约 30-35 分钟

---

---

### 1️⃣2️⃣ [DashScopeVideoApi 详解](./API客户端层/2.5-DashScopeVideoApi详解.md) ⭐⭐

**类别**：视频生成API客户端（Video Generation API Client）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ 异步任务模式（提交-轮询）
✓ 5种视频模型（T2V/I2V/KF2V）
✓ 20+种视频模板（特效）
✓ 3种生成模式（文本/图像/关键帧生视频）
✓ 智能轮询策略和批量生成管理
```

**适合人群**: 视频生成应用开发者、短视频创作工具开发者  
**阅读时长**: 约 30-35 分钟

---

### 1️⃣3️⃣ [DashScopeAgentApi 详解](./API客户端层/2.6-DashScopeAgentApi详解.md) ⭐⭐⭐

**类别**：Agent API客户端（Agent Completion API Client）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐⭐

**涵盖内容**：
```
✓ 双模式调用（同步 + 流式）
✓ 会话管理（sessionId）和记忆机制（memoryId）
✓ RAG集成（知识库检索）
✓ 多模态输入（图文混合）
✓ 思维链输出（可观察推理过程）
✓ 文档引用和工具调用支持
```

**适合人群**: Agent应用开发者、智能助手和对话机器人开发者  
**阅读时长**: 约 30-35 分钟

---

### 📂 [API客户端层索引](./API客户端层/README.md)

查看完整的API客户端层文档导航和学习路径（共6个API客户端详解）。

---

### 1️⃣4️⃣ [DashScopeAgent 详解](./Agent智能体层/3.1-DashScopeAgent详解.md) ⭐⭐⭐ 🔥

**类别**：Agent智能体实现（Agent Implementation）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐⭐

**涵盖内容**：
```
✓ Spring AI Agent接口实现
✓ 选项合并机制
✓ 请求/响应转换
✓ 同步/流式调用
✓ 会话管理和记忆机制
✓ RAG集成
✓ 多模态输入
✓ 思维链输出
```

**适合人群**: Agent应用开发者、智能助手开发者  
**阅读时长**: 约 40-45 分钟

---

### 1️⃣5️⃣ [DashScopeAgentFlowStreamMode 详解](./Agent智能体层/3.2-DashScopeAgentFlowStreamMode详解.md) ⭐⭐

**类别**：Agent流式模式枚举（Agent Flow Stream Mode Enum）  
**复杂度**：⭐⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ FULL_THOUGHTS模式（完整透明）
✓ AGENT_FORMAT模式（简洁高效）
✓ 流式输出对比
✓ 场景选择建议
✓ 环境区分策略
```

**适合人群**: 需要调试Agent的开发者、关注性能的架构师  
**阅读时长**: 约 15-20 分钟

---

### 1️⃣6️⃣ [DashScopeAgentRagOptions 详解](./Agent智能体层/3.3-DashScopeAgentRagOptions详解.md) ⭐⭐⭐

**类别**：Agent RAG配置类（Agent RAG Options）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐⭐

**涵盖内容**：
```
✓ 6个配置字段详解
✓ Pipeline ID管理
✓ 文件/标签过滤
✓ 元数据过滤（复杂查询）
✓ 会话临时文档
✓ 动态RAG配置
✓ 性能优化建议
```

**适合人群**: RAG应用开发者、知识库管理员  
**阅读时长**: 约 30-35 分钟

---

### 📂 [Agent智能体层索引](./Agent智能体层/README.md)

查看完整的Agent智能体层文档导航和学习路径（共3个Agent智能体详解）。

---

### 1️⃣7️⃣ [DashScopeWebSocketClient 详解](./通信协议层/5.1-DashScopeWebSocketClient详解.md) ⭐⭐⭐ 🔥

**类别**：WebSocket客户端（WebSocket Client）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐⭐

**涵盖内容**：
```
✓ WebSocket连接管理
✓ 文本/二进制消息发送
✓ 6种事件处理
✓ Flux流式响应
✓ 自动重连机制
✓ 背压处理
✓ TTS/ASR示例
```

**适合人群**: 实时通信开发者、语音应用开发者  
**阅读时长**: 约 35-40 分钟

---

### 1️⃣8️⃣ [DashScopeWebSocketClientOptions 详解](./通信协议层/5.2-DashScopeWebSocketClientOptions详解.md) ⭐⭐

**类别**：WebSocket配置类（WebSocket Configuration）  
**复杂度**：⭐  
**重要性**：⭐⭐

**涵盖内容**：
```
✓ 3个配置字段（url/apiKey/workSpaceId）
✓ Builder模式
✓ 多环境配置
✓ 多租户支持
✓ 配置验证和安全
```

**适合人群**: 所有WebSocket使用者  
**阅读时长**: 约 15-20 分钟

---

### 📂 [通信协议层索引](./通信协议层/README.md)

查看完整的通信协议层文档导航和学习路径（共2个通信协议详解）。

---

## 六、配置选项类（Options）

### 1️⃣9️⃣ [DashScopeChatOptions 详解](./配置选项类/6.1-DashScopeChatOptions详解.md) ⭐⭐⭐⭐⭐ 🔥 **最全面**

**类别**：对话配置类（Chat Options）  
**复杂度**：⭐⭐⭐⭐⭐  
**重要性**：⭐⭐⭐⭐⭐  
**字段数**：25+

**涵盖内容**：
```
✓ 基础参数（model/maxTokens/stream）
✓ 采样参数（temperature/topP/topK/seed/repetitionPenalty/stop）
✓ 工具调用（tools/toolChoice/parallelToolCalls/toolCallbacks）
✓ 搜索功能（enableSearch/searchOptions）
✓ 高级功能（思维链/多模态/响应格式）
✓ 场景配置预设（通用/事实/创意/代码）
✓ 参数调优策略
```

**适合人群**: 所有对话应用开发者，必读配置文档  
**阅读时长**: 约 30-35 分钟

---

### 2️⃣0️⃣ [DashScopeImageOptions 详解](./配置选项类/6.2-DashScopeImageOptions详解.md) ⭐⭐⭐⭐ 🔥

**类别**：图像配置类（Image Options）  
**复杂度**：⭐⭐⭐⭐  
**重要性**：⭐⭐⭐⭐  
**字段数**：20+

**涵盖内容**：
```
✓ 基础参数（model/n/width/height）
✓ 风格控制（10+种预设风格）
✓ 参考图像（refImg/refStrength/refMode）
✓ 高级功能（negativePrompt/promptExtend）
✓ 图像编辑（inpainting/outpainting/sketch）
✓ 场景配置预设（通用/高质量/动漫/产品）
```

**适合人群**: 图像生成应用开发者  
**阅读时长**: 约 25-30 分钟

---

### 2️⃣1️⃣ [DashScopeEmbeddingOptions 详解](./配置选项类/6.3-DashScopeEmbeddingOptions详解.md) ⭐⭐⭐ 🔥 **RAG必备**

**类别**：嵌入配置类（Embedding Options）  
**复杂度**：⭐  
**重要性**：⭐⭐⭐⭐⭐  
**字段数**：3

**涵盖内容**：
```
✓ 模型选择（text-embedding-v3等）
✓ 文本类型（query/document）
✓ 维度优化（512/768/1024/1536）
✓ RAG应用配置
✓ 存储成本分析
```

**适合人群**: RAG应用开发者，所有向量化场景  
**阅读时长**: 约 20-25 分钟

---

### 2️⃣2️⃣ [DashScopeRerankOptions 详解](./配置选项类/6.4-DashScopeRerankOptions详解.md) ⭐⭐⭐ 🔥 **RAG优化**

**类别**：重排序配置类（Rerank Options）  
**复杂度**：⭐  
**重要性**：⭐⭐⭐  
**字段数**：3

**涵盖内容**：
```
✓ 模型选择（gte-rerank）
✓ Top-N配置（默认3）
✓ 返回文档控制
✓ RAG集成优化
✓ 成本效益分析
```

**适合人群**: RAG应用开发者，搜索质量优化者  
**阅读时长**: 约 20-25 分钟

---

### 2️⃣3️⃣ [DashScopeAudioSpeechOptions 详解](./配置选项类/6.5-DashScopeAudioSpeechOptions详解.md) ⭐⭐⭐ 🔥

**类别**：语音合成配置类（TTS Options）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐⭐  
**字段数**：11

**涵盖内容**：
```
✓ 50+音色选择（中英文男女声）
✓ 音频参数（sampleRate/format/volume）
✓ 语音控制（speed/pitch）
✓ 高级功能（SSML/timestamp）
✓ 场景配置预设（播客/有声书/客服/短视频）
```

**适合人群**: 语音应用开发者，有声阅读、智能客服  
**阅读时长**: 约 25-30 分钟

---

### 2️⃣4️⃣ [DashScopeAudioTranscriptionOptions 详解](./配置选项类/6.6-DashScopeAudioTranscriptionOptions详解.md) ⭐⭐⭐

**类别**：语音识别配置类（ASR Options）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐⭐  
**字段数**：8

**涵盖内容**：
```
✓ 模型选择（paraformer-v2等）
✓ 热词定制（vocabularyId/phraseId）
✓ 音频参数（sampleRate/format）
✓ 高级功能（去口语化/多声道/语言提示）
✓ 场景配置预设（通用/实时/客服/会议/医疗）
```

**适合人群**: 语音识别应用开发者，会议记录、字幕生成  
**阅读时长**: 约 25-30 分钟

---

### 2️⃣5️⃣ [DashScopeVideoOptions 详解](./配置选项类/6.7-DashScopeVideoOptions详解.md) ⭐⭐⭐

**类别**：视频配置类（Video Options）  
**复杂度**：⭐⭐⭐  
**重要性**：⭐⭐  
**字段数**：11

**涵盖内容**：
```
✓ 4种生成模式（T2V/I2V/关键帧/模板）
✓ 视频参数（size/duration/seed）
✓ Prompt优化技巧
✓ 20+视频模板
✓ 场景配置预设（通用/高质量/短视频/产品）
```

**适合人群**: 视频生成应用开发者，短视频创作  
**阅读时长**: 约 25-30 分钟

---

### 📂 [配置选项类索引](./配置选项类/README.md)

查看完整的配置选项类文档导航和学习路径（共7个配置类详解）。

---

## 🎯 学习路径建议

### 初学者路径
```
Week 1 - 核心模型：
Day 1-2: DashScopeChatModel 详解
         - 对话模型基础
         - Function Calling
         
Day 3: DashScopeImageModel 详解
       - 图像生成
       
Day 4: DashScopeEmbeddingModel 详解
       - 向量化和RAG基础
       
Day 5: DashScopeRerankModel 详解
       - 文档重排序
       - RAG质量提升

Week 2 - 音视频模型：
Day 6: DashScopeAudioSpeechModel 详解
       - 语音合成（TTS）
       
Day 7: DashScopeAudioTranscriptionModel 详解
       - 语音识别（ASR）
       
Day 8: DashScopeVideoModel 详解
       - 视频生成
       
Day 9-10: 综合实战
          - 构建完整的多模态AI应用
```

### 进阶学习者路径
```
Step 1: 按模型类别深入学习
        - 文本类：ChatModel + EmbeddingModel + RerankModel
        - 图像类：ImageModel
        - 音频类：AudioSpeechModel + AudioTranscriptionModel  
        - 视频类：VideoModel
        
Step 2: 精读"工作原理"章节
        理解异步/同步/流式模式的区别
        
Step 3: 研读"主要方法详解"章节
        掌握源码级实现
        
Step 4: 实践"最佳实践"章节
        应用到实际项目
        
Step 5: 构建综合应用
        整合多个模型构建完整AI应用
```

---

## 🔍 快速查找

### 按功能查找

**对话相关**
- 基础对话 → [1.1 DashScopeChatModel](./1.1-DashScopeChatModel详解.md#31-同步对话调用)
- 流式对话 → [1.1 DashScopeChatModel](./1.1-DashScopeChatModel详解.md#32-流式对话调用)
- 多轮对话 → [1.1 DashScopeChatModel](./1.1-DashScopeChatModel详解.md#82-使用-chatclient推荐)

**工具调用**
- Function Calling → [1.1 DashScopeChatModel](./1.1-DashScopeChatModel详解.md#33-function-calling工具调用)
- 工具定义 → [1.1 DashScopeChatModel](./1.1-DashScopeChatModel详解.md#83-function-calling-示例)

**图像生成**
- 文生图 → [1.2 DashScopeImageModel](./1.2-DashScopeImageModel详解.md#32-文生图t2i)
- 图生图 → [1.2 DashScopeImageModel](./1.2-DashScopeImageModel详解.md#33-图生图)
- 提示词优化 → [1.2 DashScopeImageModel](./1.2-DashScopeImageModel详解.md#91-提示词优化)

**向量化与RAG**
- 文本向量化 → [1.3 DashScopeEmbeddingModel](./1.3-DashScopeEmbeddingModel详解.md#31-单个文本嵌入)
- 语义搜索 → [1.3 DashScopeEmbeddingModel](./1.3-DashScopeEmbeddingModel详解.md#82-语义搜索示例)
- RAG应用 → [1.3 DashScopeEmbeddingModel](./1.3-DashScopeEmbeddingModel详解.md#83-rag-应用示例)
- 文档重排序 → [1.4 DashScopeRerankModel](./1.4-DashScopeRerankModel详解.md#21-基本重排序)
- RAG完整流程 → [1.4 DashScopeRerankModel](./1.4-DashScopeRerankModel详解.md#82-rag-完整流程示例)

**音频处理**
- 文本转语音 → [1.5 DashScopeAudioSpeechModel](./1.5-DashScopeAudioSpeechModel详解.md#31-基本语音合成)
- 流式语音合成 → [1.5 DashScopeAudioSpeechModel](./1.5-DashScopeAudioSpeechModel详解.md#32-流式语音合成)
- 语音转文本 → [1.6 DashScopeAudioTranscriptionModel](./1.6-DashScopeAudioTranscriptionModel详解.md#21-异步识别推荐用于文件)
- 实时语音识别 → [1.6 DashScopeAudioTranscriptionModel](./1.6-DashScopeAudioTranscriptionModel详解.md#23-实时流式识别)

**视频生成**
- 文生视频 → [1.7 DashScopeVideoModel](./1.7-DashScopeVideoModel详解.md#22-文生视频t2v)
- 图生视频 → [1.7 DashScopeVideoModel](./1.7-DashScopeVideoModel详解.md#23-图生视频i2v)

---

## 📊 文档对比

| 维度 | ChatModel | ImageModel | EmbeddingModel | RerankModel | AudioSpeechModel | AudioTranscriptionModel | VideoModel |
|------|-----------|------------|----------------|-------------|------------------|------------------------|-----------|
| **核心用途** | 文本对话 | 图像生成 | 文本向量化 | 文档重排序 | 文本转语音 | 语音转文本 | 视频生成 |
| **调用模式** | 同步/流式 | 异步任务 | 同步 | 同步 | 同步/流式 | 异步/同步/流式 | 异步任务 |
| **典型耗时** | 1-5秒 | 5-30秒 | < 1秒 | 1-3秒 | 1-5秒 | 3-10秒 | 5-30分钟 |
| **输入** | 文本/多模态 | 提示词/图片 | 文本/文档 | 查询+文档列表 | 文本 | 音频文件 | 提示词/图片 |
| **输出** | 文本 | 图片URL | 向量数组 | 带分数的文档 | 音频流 | 文本 | 视频URL |
| **高级特性** | Function Calling | 风格控制 | 维度选择 | Top-N筛选 | 多音色 | 热词定制 | 首尾帧控制 |
| **成本** | 按Token计费 | 按次数计费 | 按Token计费 | 按Token计费 | 按字符计费 | 按时长计费 | 按次数/时长 |
| **应用场景** | 对话/问答 | AI绘画 | RAG/搜索 | RAG优化 | 有声阅读 | 语音助手 | 短视频创作 |

---

## 💡 核心概念速查

### ChatModel 核心概念
- **Prompt**: 提示词，包含消息列表和配置
- **ChatResponse**: 响应，包含生成内容和元数据
- **Function Calling**: 让LLM调用外部工具
- **Stream**: 流式输出，逐token返回

### ImageModel 核心概念
- **异步任务**: 提交任务 → 轮询状态 → 获取结果
- **负提示词**: 描述不想要的内容
- **参考图**: 基于参考图生成新图
- **风格**: 预设的艺术风格

### EmbeddingModel 核心概念
- **Embedding**: 文本的向量表示
- **维度**: 向量的长度（512/768/1024/1536）
- **余弦相似度**: 衡量向量相似程度
- **TextType**: query（查询）vs document（文档）

### RerankModel 核心概念
- **Rerank**: 二次精排，提升相关性
- **相关性分数**: 0.0-1.0，越高越相关
- **Top-N**: 只返回最相关的N个文档
- **召回+重排**: 先广泛召回，再精准筛选

### AudioSpeechModel 核心概念
- **TTS**: Text-to-Speech，文本转语音
- **音色**: 不同的语音风格（男声/女声等）
- **语速**: 播放速度（0.5-2.0倍）
- **流式输出**: 实时返回音频数据

### AudioTranscriptionModel 核心概念
- **ASR**: Automatic Speech Recognition，语音识别
- **三种模式**: 异步、同步、实时流式
- **热词**: 自定义词汇提升识别准确率
- **去口语化**: 去除"嗯"、"啊"等

### VideoModel 核心概念
- **T2V**: Text-to-Video，文本生成视频
- **I2V**: Image-to-Video，图片生成视频
- **异步任务**: 提交 → 轮询 → 获取
- **首尾帧**: 控制视频开始和结束画面

---

## 🛠️ 配置快速参考

### ChatModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      chat:
        options:
          model: qwen-plus
          temperature: 0.7
          max-tokens: 2000
```

### ImageModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      image:
        options:
          model: wanx-v1
          n: 1
          size: 1024*1024
```

### EmbeddingModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      embedding:
        options:
          model: text-embedding-v3
          dimensions: 1024
```

### RerankModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      rerank:
        options:
          model: gte-rerank
          top-n: 5
```

### AudioSpeechModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      audio:
        speech:
          options:
            voice: zhixiaoxia
            speed: 1.0
            sample-rate: 16000
```

### AudioTranscriptionModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      audio:
        transcription:
          options:
            model: paraformer-realtime-v1
            disfluency-removal-enabled: true
```

### VideoModel 配置
```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      video:
        options:
          model: wanx-v2v-v1
          duration: 10
          size: 1280*720
```

---

## 📚 相关文档

- [DashScope核心实现类全景图](../DashScope核心实现类全景图.md) - 55+核心类总览
- [Spring AI Alibaba特有设计详解](../Spring-AI-Alibaba特有设计详解.md) - 框架设计详解
- [Spring AI Alibaba调用流程深度解析](../Spring-AI-Alibaba调用流程深度解析.md) - 完整调用流程
- [spring-ai-alibaba-core模块深度分析](../spring-ai-alibaba-core模块深度分析.md) - Core模块分析

---

## 🤝 贡献指南

如果你发现文档中的错误或有改进建议，欢迎：
1. 提交 Issue
2. 创建 Pull Request
3. 在社区群中反馈

---

## 📝 更新记录

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2025-10-05 | 初始版本，创建前三个核心模型详解文档（Chat/Image/Embedding） |
| v1.1 | 2025-10-05 | 新增四个模型详解文档（Rerank/AudioSpeech/AudioTranscription/Video） |
| v1.2 | 2025-10-05 | 新增API客户端层文档（DashScopeApi/DashScopeImageApi） |
| v1.3 | 2025-10-05 | 新增语音API文档（DashScopeAudioSpeechApi/DashScopeAudioTranscriptionApi） |
| v1.4 | 2025-10-05 | 新增视频和Agent API文档（DashScopeVideoApi/DashScopeAgentApi） |
| v1.5 | 2025-10-05 | 新增Agent智能体层文档（DashScopeAgent/FlowStreamMode/RagOptions） |
| v1.6 | 2025-10-05 | 新增通信协议层文档（DashScopeWebSocketClient/Options） |
| **v1.7** | **2025-10-05** | **新增配置选项类文档（7个Options详解）**🎉 **完整版** |

---

**系列版本**: v1.7 🎉 **完整版**  
**文档数量**: 7个模型详解 + 6个API客户端详解 + 3个Agent智能体详解 + 2个通信协议详解 + **7个配置选项类详解** + 7个导航索引  
**总字数**: 约 **287,000 字** （增加 70,000 字）  
**最后更新**: 2025-10-05  
**作者**: Spring AI Alibaba Team

---

**祝学习愉快！**

如有任何问题，欢迎随时交流讨论。


