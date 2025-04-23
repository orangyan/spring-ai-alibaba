
package com.alibaba.cloud.ai.dashscope.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DashScopeStoreOptions 类用于定义与 DashScope 存储相关的配置选项。
 * 该类包含以下主要功能：
 * 1. 索引名称的设置与获取。
 * 2. 文档索引切分、向量化以及检索的相关配置管理。
 *
 * 通过构造函数初始化时，需要提供索引名称（indexName），这是存储配置的核心标识。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashScopeStoreOptions {

	private @JsonProperty("index_name") String indexName;

	/**
	 * 文档索引切分相关配置
	 */
	private @JsonProperty("transformer_options") DashScopeDocumentTransformerOptions transformerOptions;

	/**
	 * 文档索引向量化相关配置
	 */
	private @JsonProperty("embedding_options") DashScopeEmbeddingOptions embeddingOptions;

	/**
	 * 文档检索相关配置
	 */
	private @JsonProperty("retriever_options") DashScopeDocumentRetrieverOptions retrieverOptions;

	public DashScopeStoreOptions(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public DashScopeDocumentTransformerOptions getTransformerOptions() {
		return transformerOptions;
	}

	public void setTransformerOptions(DashScopeDocumentTransformerOptions transformerOptions) {
		this.transformerOptions = transformerOptions;
	}

	public DashScopeEmbeddingOptions getEmbeddingOptions() {
		return embeddingOptions;
	}

	public void setEmbeddingOptions(DashScopeEmbeddingOptions embeddingOptions) {
		this.embeddingOptions = embeddingOptions;
	}

	public DashScopeDocumentRetrieverOptions getRetrieverOptions() {
		return retrieverOptions;
	}

	public void setRetrieverOptions(DashScopeDocumentRetrieverOptions retrieverOptions) {
		this.retrieverOptions = retrieverOptions;
	}

}
