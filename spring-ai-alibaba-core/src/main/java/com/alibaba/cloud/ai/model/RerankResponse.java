package com.alibaba.cloud.ai.model;

import java.util.List;

import com.alibaba.cloud.ai.document.DocumentWithScore;

import org.springframework.ai.model.ModelResponse;
import org.springframework.util.CollectionUtils;

/**
 * 重新排序响应类，实现了ModelResponse接口，用于处理文档重新排序的任务
 * 该类用于封装文档重新排序后的结果，提供给上层调用者
 */
public class RerankResponse implements ModelResponse<DocumentWithScore> {

	private final List<DocumentWithScore> documents;

	private final RerankResponseMetadata metadata;

	public RerankResponse(List<DocumentWithScore> documents) {
		this(documents, new RerankResponseMetadata());
	}

	public RerankResponse(List<DocumentWithScore> documents, RerankResponseMetadata metadata) {
		this.documents = documents;
		this.metadata = metadata;
	}

	@Override
	public DocumentWithScore getResult() {
		if (CollectionUtils.isEmpty(this.documents)) {
			return null;
		}

		return this.documents.get(0);
	}

	@Override
	public List<DocumentWithScore> getResults() {
		return this.documents;
	}

	@Override
	public RerankResponseMetadata getMetadata() {
		return this.metadata;
	}

}
