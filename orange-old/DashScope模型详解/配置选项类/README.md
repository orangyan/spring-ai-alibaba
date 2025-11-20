# 配置选项类（Options）索引

> **层级**：第六层 - 配置选项类  
> **文档数量**：7个核心配置类  
> **总字数**：约 70,000 字  
> **版本**：v1.0 | 2025-10-05

---

## 📚 文档导航

### 6.1 对话配置
**[DashScopeChatOptions详解](./6.1-DashScopeChatOptions详解.md)** - 最全面的配置类（25+字段）
- ✅ 基础参数（model/maxTokens/stream）
- ✅ 采样参数（temperature/topP/topK/seed）
- ✅ 工具调用（tools/toolChoice/parallelToolCalls）
- ✅ 搜索功能（enableSearch/searchOptions）
- ✅ 高级功能（思维链/多模态/响应格式）
- 📊 **字段数量**：25+
- 📄 **篇幅**：约 12,000 字

### 6.2 图像配置
**[DashScopeImageOptions详解](./6.2-DashScopeImageOptions详解.md)** - 图像生成全参数
- ✅ 基础参数（model/n/width/height）
- ✅ 风格控制（style/seed）
- ✅ 参考图像（refImg/refStrength/refMode）
- ✅ 高级功能（negativePrompt/promptExtend）
- ✅ 图像编辑（inpainting/outpainting/sketch）
- 📊 **字段数量**：20+
- 📄 **篇幅**：约 10,000 字

### 6.3 嵌入配置
**[DashScopeEmbeddingOptions详解](./6.3-DashScopeEmbeddingOptions详解.md)** - 向量化配置
- ✅ 模型选择（text-embedding-v3等）
- ✅ 文本类型（query/document）
- ✅ 维度优化（512/768/1024/1536）
- ✅ RAG应用配置
- 📊 **字段数量**：3
- 📄 **篇幅**：约 8,000 字

### 6.4 重排序配置
**[DashScopeRerankOptions详解](./6.4-DashScopeRerankOptions详解.md)** - 重排序参数
- ✅ 模型选择（gte-rerank）
- ✅ Top-N配置（默认3）
- ✅ 返回文档控制
- ✅ RAG集成优化
- 📊 **字段数量**：3
- 📄 **篇幅**：约 9,000 字

### 6.5 语音合成配置
**[DashScopeAudioSpeechOptions详解](./6.5-DashScopeAudioSpeechOptions详解.md)** - TTS全参数
- ✅ 50+音色选择
- ✅ 音频参数（sampleRate/format）
- ✅ 语音控制（speed/pitch/volume）
- ✅ 高级功能（SSML/timestamp）
- 📊 **字段数量**：11
- 📄 **篇幅**：约 11,000 字

### 6.6 语音识别配置
**[DashScopeAudioTranscriptionOptions详解](./6.6-DashScopeAudioTranscriptionOptions详解.md)** - ASR全参数
- ✅ 模型选择（paraformer-v2等）
- ✅ 热词定制（vocabularyId）
- ✅ 音频参数（sampleRate/format）
- ✅ 高级功能（去口语化/多声道）
- 📊 **字段数量**：8
- 📄 **篇幅**：约 10,000 字

### 6.7 视频配置
**[DashScopeVideoOptions详解](./6.7-DashScopeVideoOptions详解.md)** - 视频生成全参数
- ✅ 4种生成模式（T2V/I2V/关键帧/模板）
- ✅ 视频参数（size/duration）
- ✅ Prompt优化技巧
- ✅ 20+视频模板
- 📊 **字段数量**：11
- 📄 **篇幅**：约 10,000 字

---

## 🎯 学习路径推荐

### 路径1：快速入门（2小时）
适合：快速了解基本配置
```
1. ChatOptions（30分钟） - 最常用
2. EmbeddingOptions（15分钟） - RAG必备
3. ImageOptions（20分钟） - 图像生成
4. 浏览其他配置（55分钟）
```

### 路径2：全面掌握（6小时）
适合：深入理解所有配置
```
1. ChatOptions（90分钟） - 重点学习
2. ImageOptions（60分钟） - 详细配置
3. EmbeddingOptions（30分钟） - RAG基础
4. RerankOptions（30分钟） - RAG优化
5. AudioSpeechOptions（60分钟） - TTS
6. AudioTranscriptionOptions（60分钟） - ASR
7. VideoOptions（60分钟） - 视频生成
```

### 路径3：场景驱动（4小时）
适合：按业务场景学习
```
对话场景：
└── ChatOptions

RAG场景：
├── EmbeddingOptions
└── RerankOptions

多模态场景：
├── ImageOptions
├── AudioSpeechOptions
├── AudioTranscriptionOptions
└── VideoOptions
```

---

## 🔍 快速查找

### 按功能分类

**基础对话**
- [对话模型配置](./6.1-DashScopeChatOptions详解.md) - temperature/topP/maxTokens

**生成式AI**
- [图像生成配置](./6.2-DashScopeImageOptions详解.md) - style/refImg/negativePrompt
- [视频生成配置](./6.7-DashScopeVideoOptions详解.md) - prompt/imageUrl/template

**RAG增强**
- [嵌入向量配置](./6.3-DashScopeEmbeddingOptions详解.md) - model/textType/dimensions
- [重排序配置](./6.4-DashScopeRerankOptions详解.md) - topN/returnDocuments

**语音处理**
- [语音合成配置](./6.5-DashScopeAudioSpeechOptions详解.md) - voice/speed/pitch
- [语音识别配置](./6.6-DashScopeAudioTranscriptionOptions详解.md) - vocabularyId/disfluencyRemoval

### 按复杂度分类

**简单（3个字段以内）**
- ✅ EmbeddingOptions（3字段）
- ✅ RerankOptions（3字段）

**中等（5-15个字段）**
- ✅ AudioSpeechOptions（11字段）
- ✅ AudioTranscriptionOptions（8字段）
- ✅ VideoOptions（11字段）

**复杂（15+字段）**
- ✅ ImageOptions（20+字段）
- ✅ ChatOptions（25+字段）

---

## 📊 文档对比表

| 配置类 | 字段数 | 篇幅 | 难度 | 使用频率 | 推荐优先级 |
|-------|-------|------|------|---------|-----------|
| **ChatOptions** | 25+ | 12k | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 🔥**最高** |
| **ImageOptions** | 20+ | 10k | ⭐⭐⭐ | ⭐⭐⭐⭐ | 🔥**高** |
| **EmbeddingOptions** | 3 | 8k | ⭐ | ⭐⭐⭐⭐⭐ | 🔥**高** |
| **RerankOptions** | 3 | 9k | ⭐ | ⭐⭐⭐ | 🔥**中** |
| **AudioSpeechOptions** | 11 | 11k | ⭐⭐ | ⭐⭐⭐ | 🔥**中** |
| **AudioTranscriptionOptions** | 8 | 10k | ⭐⭐ | ⭐⭐ | 🔥**中** |
| **VideoOptions** | 11 | 10k | ⭐⭐ | ⭐⭐ | 🔥**低** |

---

## 💡 核心概念

### 1. 配置选项的作用
```
配置选项类是连接应用代码和底层API的桥梁

应用代码
    ↓
DashScopeXxxOptions（配置层）
    ↓
DashScopeXxxModel（模型层）
    ↓
DashScopeXxxApi（API层）
    ↓
DashScope服务
```

### 2. Builder模式
所有Options类都使用Builder模式：
```java
DashScopeChatOptions options = DashScopeChatOptions.builder()
    .withModel("qwen-plus")
    .withTemperature(0.7)
    .withMaxToken(1500)
    .build();
```

### 3. 选项合并机制
```
最终选项 = 实例默认选项 + 运行时选项

DashScopeChatModel model = new DashScopeChatModel(api, defaultOptions);
ChatResponse response = model.call(new Prompt(msg, runtimeOptions));

实际使用: merge(defaultOptions, runtimeOptions)
```

### 4. 常用参数类型

**采样参数**（控制随机性）
- temperature：温度（0-2）
- topP：核采样（0-1）
- topK：Top-K采样
- seed：随机种子

**生成控制**
- maxTokens：最大Token数
- stop：停止词
- repetitionPenalty：重复惩罚

**高级功能**
- tools：工具调用
- enableSearch：联网搜索
- responseFormat：响应格式
- negativePrompt：反向提示词

---

## ⚡ 配置快速参考

### ChatOptions核心配置
```java
// 通用对话
.withModel("qwen-plus")
.withTemperature(0.85)  // 平衡
.withMaxToken(1500)

// 事实问答
.withTemperature(0.3)   // 低温度
.withEnableSearch(true)  // 启用搜索

// 创意写作
.withTemperature(1.2)    // 高温度
.withRepetitionPenalty(1.2)
```

### ImageOptions核心配置
```java
// 基础生成
.withModel("wanx-v1")
.withWidth(1024).withHeight(1024)
.withStyle("photography")

// 高质量
.withN(4)  // 生成4张选择
.withPromptExtend(true)
.withNegativePrompt("模糊,低质量,噪点")
```

### EmbeddingOptions核心配置
```java
// 查询嵌入
.withModel("text-embedding-v3")
.withTextType("query")

// 文档嵌入
.withTextType("document")
.withDimensions(1024)
```

### RerankOptions核心配置
```java
// RAG重排序
.withModel("gte-rerank")
.withTopN(5)  // 返回前5个
.withReturnDocuments(true)
```

### AudioSpeechOptions核心配置
```java
// 基础TTS
.withModel("cosyvoice-v1")
.withVoice("longxiaochun")  // 中文女声

// 高质量
.withSampleRate(48000)
.withFormat("wav")
```

### AudioTranscriptionOptions核心配置
```java
// 通用ASR
.withModel("paraformer-v2")
.withDisfluencyRemovalEnabled(true)  // 去口语化

// 专业场景
.withVocabularyId("medical-vocab")  // 热词
```

### VideoOptions核心配置
```java
// 文本生成视频
.withModel("wanx2.1-t2v-turbo")
.withPrompt("海浪拍打沙滩")
.withSize("1280*720")

// 图像生成视频
.withModel("wanx2.1-i2v")
.withImageUrl("https://...")
```

---

## 🎓 学习建议

### 对于初学者
1. **先学ChatOptions**：最常用，理解基本概念
2. **再学EmbeddingOptions**：RAG必备
3. **按需学习其他**：根据业务场景

### 对于进阶者
1. **深入采样参数**：理解temperature/topP/topK
2. **掌握工具调用**：Function Calling配置
3. **优化成本**：maxTokens/模型选择

### 对于专家
1. **参数调优**：针对特定场景
2. **配置预设**：封装常用配置
3. **性能优化**：并发/流式/批量

---

## 📈 配置层统计

### 文档统计
- **文档数量**：7个
- **总字数**：约 70,000 字
- **代码示例**：150+
- **配置预设**：50+
- **最佳实践**：30+

### 字段统计
- **总字段数**：约 80+
- **最复杂**：ChatOptions（25+字段）
- **最简单**：EmbeddingOptions/RerankOptions（3字段）

### 覆盖范围
- ✅ 对话生成
- ✅ 图像生成
- ✅ 向量嵌入
- ✅ 文档重排序
- ✅ 语音合成
- ✅ 语音识别
- ✅ 视频生成

---

## 🔗 相关索引

### 上层索引
- [DashScope模型详解总索引](../README.md) - 返回上级

### 平级索引
- [模型实现层索引](../模型实现层/README.md) - Model Layer
- [API客户端层索引](../API客户端层/README.md) - API Client Layer
- [Agent智能体层索引](../Agent智能体层/README.md) - Agent Layer
- [通信协议层索引](../通信协议层/README.md) - Protocol Layer

---

## 🆕 更新记录

### v1.0 - 2025-10-05
- ✅ 创建配置选项类层索引
- ✅ 完成7个配置类详解文档
- ✅ ChatOptions详解（12,000字）
- ✅ ImageOptions详解（10,000字）
- ✅ EmbeddingOptions详解（8,000字）
- ✅ RerankOptions详解（9,000字）
- ✅ AudioSpeechOptions详解（11,000字）
- ✅ AudioTranscriptionOptions详解（10,000字）
- ✅ VideoOptions详解（10,000字）
- ✅ 配置快速参考
- ✅ 学习路径规划
- ✅ 场景配置预设

---

**文档版本**: v1.0  
**最后更新**: 2025-10-05  
**维护者**: Spring AI Alibaba Team  
**反馈**: 如有问题或建议，请提交Issue

