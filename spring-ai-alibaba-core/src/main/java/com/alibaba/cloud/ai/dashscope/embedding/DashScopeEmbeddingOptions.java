
package com.alibaba.cloud.ai.dashscope.embedding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.embedding.EmbeddingOptions;

/**
 * DashScopeEmbeddingOptions 类实现了 EmbeddingOptions 接口，用于定义和配置 DashScope 嵌入模型的选项。
 * 该类通过 Jackson 注解支持 JSON 序列化与反序列化，并提供了构建器模式以方便实例的创建和配置。
 * 主要功能包括设置模型名称、文本类型和嵌入维度等参数。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashScopeEmbeddingOptions implements EmbeddingOptions {

	private @JsonProperty("model") String model;

	private @JsonProperty("text_type") String textType;

	private @JsonProperty("dimensions") Integer dimensions;

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public Integer getDimensions() {
		return this.dimensions;
	}

	public void setDimensions(Integer dimensions) {
		this.dimensions = dimensions;
	}

	public String getTextType() {
		return this.textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	public static class Builder {

		protected DashScopeEmbeddingOptions options;

		public Builder() {
			this.options = new DashScopeEmbeddingOptions();
		}

		public Builder withModel(String model) {
			this.options.setModel(model);
			return this;
		}

		public Builder withDimensions(Integer dimensions) {
			this.options.setDimensions(dimensions);
			return this;
		}

		public Builder withTextType(String textType) {
			this.options.setTextType(textType);
			return this;
		}

		public DashScopeEmbeddingOptions build() {
			return this.options;
		}

	}

}
