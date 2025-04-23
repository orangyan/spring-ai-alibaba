
package com.alibaba.cloud.ai.dashscope.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * DashScope代理RAG选项类
 * 用于配置检索增强生成（RAG）相关的参数
 * 
 * @author kevinlin09
 */
public class DashScopeAgentRagOptions {

	/** 知识库ID列表，用于指定要检索的知识库 */
	@JsonProperty("pipeline_ids")
	private List<String> pipelineIds;

	/** 知识库文件ID列表，用于指定要检索的文件 */
	@JsonProperty("file_ids")
	private List<String> fileIds;

	/** 知识库标签列表，用于按标签过滤检索结果 */
	@JsonProperty("tags")
	private List<String> tags;

	/** 知识库查询的元数据过滤器，用于按元数据过滤检索结果 */
	@JsonProperty("metadata_filter")
	private JsonNode metadataFilter;

	/** 知识库查询的结构化过滤器，用于按结构化数据过滤检索结果 */
	@JsonProperty("structured_filter")
	private JsonNode structuredFilter;

	/** 会话文件ID列表，用于指定与当前会话关联的临时文件 */
	@JsonProperty("session_file_ids")
	private List<String> sessionFileIds;

	public List<String> getPipelineIds() {
		return pipelineIds;
	}

	public void setPipelineIds(List<String> pipelineIds) {
		this.pipelineIds = pipelineIds;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public JsonNode getMetadataFilter() {
		return metadataFilter;
	}

	public void setMetadataFilter(JsonNode metadataFilter) {
		this.metadataFilter = metadataFilter;
	}

	public JsonNode getStructuredFilter() {
		return structuredFilter;
	}

	public void setStructuredFilter(JsonNode structuredFilter) {
		this.structuredFilter = structuredFilter;
	}

	public List<String> getSessionFileIds() {
		return sessionFileIds;
	}

	public void setSessionFileIds(List<String> sessionFileIds) {
		this.sessionFileIds = sessionFileIds;
	}

	public static DashScopeAgentRagOptions.Builder builder() {

		return new DashScopeAgentRagOptions.Builder();
	}

	public static class Builder {

		protected DashScopeAgentRagOptions options;

		public Builder() {
			this.options = new DashScopeAgentRagOptions();
		}

		public Builder(DashScopeAgentRagOptions options) {
			this.options = options;
		}

		public Builder withPipelineIds(List<String> pipelineIds) {
			this.options.pipelineIds = pipelineIds;
			return this;
		}

		public Builder withFileIds(List<String> fileIds) {
			this.options.fileIds = fileIds;
			return this;
		}

		public Builder withTags(List<String> tags) {
			this.options.tags = tags;
			return this;
		}

		public Builder withMetadataFilter(JsonNode metadataFilter) {
			this.options.metadataFilter = metadataFilter;
			return this;
		}

		public Builder withStructuredFilter(JsonNode structuredFilter) {
			this.options.structuredFilter = structuredFilter;
			return this;
		}

		public Builder withSessionFileIds(List<String> sessionFileIds) {
			this.options.sessionFileIds = sessionFileIds;
			return this;
		}

		public DashScopeAgentRagOptions build() {
			return this.options;
		}

	}

}
