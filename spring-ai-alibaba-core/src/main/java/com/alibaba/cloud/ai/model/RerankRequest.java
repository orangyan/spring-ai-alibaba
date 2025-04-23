package com.alibaba.cloud.ai.model;

import org.springframework.ai.document.Document;
import org.springframework.ai.model.ModelRequest;

import java.util.List;

/**
 * RerankRequest类实现了ModelRequest接口，用于对文档列表进行重新排序请求
 * 它封装了与重新排序相关的操作和数据，以便于向模型请求处理
 */
public class RerankRequest implements ModelRequest<List<Document>> {

	private final String query;

	private final List<Document> documents;

	private final RerankOptions options;

	public RerankRequest(String query, List<Document> documents) {
		this(query, documents, null);
	}

	public RerankRequest(String query, List<Document> documents, RerankOptions options) {
		this.query = query;
		this.documents = documents;
		this.options = options;
	}

	@Override
	public List<Document> getInstructions() {
		return this.documents;
	}

	@Override
	public RerankOptions getOptions() {
		return this.options;
	}

	public String getQuery() {
		return query;
	}

}
