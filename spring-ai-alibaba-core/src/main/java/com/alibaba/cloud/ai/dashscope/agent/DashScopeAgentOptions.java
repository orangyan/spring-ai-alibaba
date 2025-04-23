
package com.alibaba.cloud.ai.dashscope.agent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.ai.chat.prompt.ChatOptions;

/**
 * DashScope代理配置选项类
 * 用于配置DashScope代理的各种参数和选项
 * 
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @since 1.0.0-M2
 */
public class DashScopeAgentOptions implements ChatOptions {

	// 应用ID，用于标识特定的DashScope应用
	@JsonProperty("app_id")
	private String appId;

	// 会话ID，用于维护对话上下文
	@JsonProperty("session_id")
	private String sessionId;

	// 记忆ID，用于存储和检索对话历史
	@JsonProperty("memory_id")
	private String memoryId;

	// 是否启用增量输出，控制流式输出的行为
	@JsonProperty("incremental_output")
	private Boolean incrementalOutput;

	// 是否包含思维过程，控制是否输出推理过程
	@JsonProperty("has_thoughts")
	private Boolean hasThoughts;

	// 图片列表，用于多模态输入
	@JsonProperty("images")
	private List<String> images;

	// 业务参数，用于传递自定义的业务逻辑参数
	@JsonProperty("biz_params")
	private JsonNode bizParams;

	// RAG选项，用于配置检索增强生成的相关参数
	@JsonProperty("rag_options")
	private DashScopeAgentRagOptions ragOptions;

	// 流式输出模式，控制流式输出的格式
	@JsonProperty("flow_stream_mode")
	private DashScopeAgentFlowStreamMode flowStreamMode;

	@Override
	public String getModel() {
		return null;
	}

	@Override
	public Double getFrequencyPenalty() {
		return null;
	}

	@Override
	public Integer getMaxTokens() {
		return null;
	}

	@Override
	public Double getPresencePenalty() {
		return null;
	}

	@Override
	public List<String> getStopSequences() {
		return null;
	}

	@Override
	public Double getTemperature() {
		return 0d;
	}

	@Override
	public Double getTopP() {
		return 0d;
	}

	@Override
	public Integer getTopK() {
		return 0;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public Boolean getIncrementalOutput() {
		return incrementalOutput;
	}

	public void setIncrementalOutput(Boolean incrementalOutput) {
		this.incrementalOutput = incrementalOutput;
	}

	public Boolean getHasThoughts() {
		return hasThoughts;
	}

	public void setHasThoughts(Boolean hasThoughts) {
		this.hasThoughts = hasThoughts;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public JsonNode getBizParams() {
		return bizParams;
	}

	public void setBizParams(JsonNode bizParams) {
		this.bizParams = bizParams;
	}

	public DashScopeAgentRagOptions getRagOptions() {
		return ragOptions;
	}

	public void setRagOptions(DashScopeAgentRagOptions ragOptions) {
		this.ragOptions = ragOptions;
	}

	public DashScopeAgentFlowStreamMode getFlowStreamMode() {
		return flowStreamMode;
	}

	public void setFlowStreamMode(DashScopeAgentFlowStreamMode flowStreamMode) {
		this.flowStreamMode = flowStreamMode;
	}

	@Override
	public ChatOptions copy() {
		return DashScopeAgentOptions.fromOptions(this);
	}

	public static DashScopeAgentOptions fromOptions(DashScopeAgentOptions options) {
		return DashScopeAgentOptions.builder()
			.withAppId(options.getAppId())
			.withSessionId(options.getSessionId())
			.withMemoryId(options.getMemoryId())
			.withIncrementalOutput(options.getIncrementalOutput())
			.withHasThoughts(options.getHasThoughts())
			.withBizParams(options.getBizParams())
			.build();
	}

	public static DashScopeAgentOptions.Builder builder() {

		return new DashScopeAgentOptions.Builder();
	}

	public static class Builder {

		protected DashScopeAgentOptions options;

		public Builder() {
			this.options = new DashScopeAgentOptions();
		}

		public Builder(DashScopeAgentOptions options) {
			this.options = options;
		}

		public Builder withAppId(String appId) {
			this.options.setAppId(appId);
			return this;
		}

		public Builder withSessionId(String sessionId) {
			this.options.sessionId = sessionId;
			return this;
		}

		public Builder withMemoryId(String memoryId) {
			this.options.memoryId = memoryId;
			return this;
		}

		public Builder withIncrementalOutput(Boolean incrementalOutput) {
			this.options.incrementalOutput = incrementalOutput;
			return this;
		}

		public Builder withHasThoughts(Boolean hasThoughts) {
			this.options.hasThoughts = hasThoughts;
			return this;
		}

		public Builder withImages(List<String> images) {
			this.options.images = images;
			return this;
		}

		public Builder withBizParams(JsonNode bizParams) {
			this.options.bizParams = bizParams;
			return this;
		}

		public Builder withRagOptions(DashScopeAgentRagOptions ragOptions) {
			this.options.ragOptions = ragOptions;
			return this;
		}

		public Builder withFlowStreamMode(DashScopeAgentFlowStreamMode flowStreamMode) {
			this.options.flowStreamMode = flowStreamMode;
			return this;
		}

		public DashScopeAgentOptions build() {
			return this.options;
		}

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DashScopeAgentOptions{");
		sb.append("appId='").append(appId).append('\'');
		sb.append(", sessionId='").append(sessionId).append('\'');
		sb.append(", memoryId='").append(memoryId).append('\'');
		sb.append(", incrementalOutput=").append(incrementalOutput);
		sb.append(", hasThoughts=").append(hasThoughts);
		sb.append(", images=").append(images);
		sb.append(", bizParams=").append(bizParams);
		sb.append(", ragOptions=").append(ragOptions);
		sb.append(", flowStreamMode=").append(flowStreamMode);
		sb.append('}');
		return sb.toString();
	}

}
