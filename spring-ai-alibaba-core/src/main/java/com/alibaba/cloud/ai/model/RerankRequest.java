

package com.alibaba.cloud.ai.model;

import org.springframework.ai.document.Document;
import org.springframework.ai.model.ModelRequest;

import java.util.List;

/**
 * Title rerank request.<br>
 * Description rerank request.<br>
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
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
