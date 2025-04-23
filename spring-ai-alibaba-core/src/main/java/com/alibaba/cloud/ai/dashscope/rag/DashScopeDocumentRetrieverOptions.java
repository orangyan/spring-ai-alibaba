
package com.alibaba.cloud.ai.dashscope.rag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * DashScopeDocumentRetrieverOptions 类用于配置文档检索功能的选项。
 * 该类通过多个参数定义了文档检索的行为，包括索引名称、相似度计算、重写功能、重排序功能等。
 *
 * 参数说明：
 * - indexName: 指定用于检索的索引名称。
 * - denseSimilarityTopK: 密集向量相似度计算时返回的 Top K 结果数量，默认值为 100。
 * - sparseSimilarityTopK: 稀疏向量相似度计算时返回的 Top K 结果数量，默认值为 100。
 * - enableRewrite: 是否启用查询重写功能，默认值为 false。
 * - rewriteModelName: 查询重写功能使用的模型名称，默认值为 "conv-rewrite-qwen-1.8b"。
 * - enableReranking: 是否启用重排序功能，默认值为 true。
 * - rerankModelName: 重排序功能使用的模型名称，默认值为 "gte-rerank-hybrid"。
 * - rerankMinScore: 重排序功能的最小分数阈值，默认值为 0.01。
 * - rerankTopN: 重排序功能返回的 Top N 结果数量，默认值为 5。
 *
 * 此外，该类还提供了一个内部静态 Builder 类，用于以链式调用的方式构建配置实例。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashScopeDocumentRetrieverOptions {

	private @JsonProperty("index_name") String indexName;

	private @JsonProperty("dense_similarity_top_k") int denseSimilarityTopK = 100;

	private @JsonProperty("sparse_similarity_top_k") int sparseSimilarityTopK = 100;

	private @JsonProperty("enable_rewrite") boolean enableRewrite = false;

	private @JsonProperty("model_name") String rewriteModelName = "conv-rewrite-qwen-1.8b";

	private @JsonProperty("enable_reranking") boolean enableReranking = true;

	private @JsonProperty("model_name") String rerankModelName = "gte-rerank-hybrid";

	private @JsonProperty("rerank_min_score") float rerankMinScore = 0.01f;

	private @JsonProperty("rerank_top_n") int rerankTopN = 5;

	private @JsonProperty("search_filters") List<Map<String, Object>> searchFilters;

	public static DashScopeDocumentRetrieverOptions.Builder builder() {
		return new DashScopeDocumentRetrieverOptions.Builder();
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public int getDenseSimilarityTopK() {
		return denseSimilarityTopK;
	}

	public void setDenseSimilarityTopK(int denseSimilarityTopK) {
		this.denseSimilarityTopK = denseSimilarityTopK;
	}

	public int getSparseSimilarityTopK() {
		return sparseSimilarityTopK;
	}

	public void setSparseSimilarityTopK(int sparseSimilarityTopK) {
		this.sparseSimilarityTopK = sparseSimilarityTopK;
	}

	public boolean isEnableRewrite() {
		return enableRewrite;
	}

	public void setEnableRewrite(boolean enableRewrite) {
		this.enableRewrite = enableRewrite;
	}

	public String getRewriteModelName() {
		return rewriteModelName;
	}

	public void setRewriteModelName(String rewriteModelName) {
		this.rewriteModelName = rewriteModelName;
	}

	public boolean isEnableReranking() {
		return enableReranking;
	}

	public void setEnableReranking(boolean enableReranking) {
		this.enableReranking = enableReranking;
	}

	public String getRerankModelName() {
		return rerankModelName;
	}

	public void setRerankModelName(String rerankModelName) {
		this.rerankModelName = rerankModelName;
	}

	public float getRerankMinScore() {
		return rerankMinScore;
	}

	public void setRerankMinScore(float rerankMinScore) {
		this.rerankMinScore = rerankMinScore;
	}

	public int getRerankTopN() {
		return rerankTopN;
	}

	public void setRerankTopN(int rerankTopN) {
		this.rerankTopN = rerankTopN;
	}

	public void setSearchFilters(List<Map<String, Object>> searchFilters) {
		this.searchFilters = searchFilters;
	}

	public List<Map<String, Object>> getSearchFilters() {
		return searchFilters;
	}

	public static class Builder {

		protected DashScopeDocumentRetrieverOptions options;

		public Builder() {
			this.options = new DashScopeDocumentRetrieverOptions();
		}

		public DashScopeDocumentRetrieverOptions.Builder withIndexName(String indexName) {
			this.options.setIndexName(indexName);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withDenseSimilarityTopK(Integer denseSimilarityTopK) {
			this.options.setDenseSimilarityTopK(denseSimilarityTopK);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withSparseSimilarityTopK(int sparseSimilarityTopK) {
			this.options.setSparseSimilarityTopK(sparseSimilarityTopK);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withEnableRewrite(boolean enableRewrite) {
			this.options.setEnableRewrite(enableRewrite);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withRewriteModelName(String rewriteModelName) {
			this.options.setRewriteModelName(rewriteModelName);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withEnableReranking(boolean enableReranking) {
			this.options.setEnableReranking(enableReranking);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withRerankModelName(String textType) {
			this.options.setRerankModelName(textType);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withRerankMinScore(float rerankMinScore) {
			this.options.setRerankMinScore(rerankMinScore);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withRerankTopN(int rerankTopN) {
			this.options.setRerankTopN(rerankTopN);
			return this;
		}

		public DashScopeDocumentRetrieverOptions.Builder withSearchFilters(List<Map<String, Object>> searchFilters) {
			this.options.setSearchFilters(searchFilters);
			return this;
		}

		public DashScopeDocumentRetrieverOptions build() {
			return this.options;
		}

	}

}
