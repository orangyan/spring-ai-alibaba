
package com.alibaba.cloud.ai.dashscope.rag;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.common.DashScopeException;
import com.alibaba.cloud.ai.dashscope.common.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 实现了DocumentTransformer接口的类，用于对文档进行转换处理
 * 该类通过调用DashScope API对文档进行处理，旨在提供一种对文档内容进行转换或分析的方式
 */
public class DashScopeDocumentTransformer implements DocumentTransformer {

	private static final Logger logger = LoggerFactory.getLogger(DashScopeDocumentTransformer.class);

	private final DashScopeApi dashScopeApi;

	private final DashScopeDocumentTransformerOptions options;

	public DashScopeDocumentTransformer(DashScopeApi dashScopeApi) {
		this(dashScopeApi, DashScopeDocumentTransformerOptions.builder().build());
	}

	public DashScopeDocumentTransformer(DashScopeApi dashScopeApi, DashScopeDocumentTransformerOptions options) {
		Assert.notNull(dashScopeApi, "DashScopeApi must not be null");
		Assert.notNull(options, "DashScopeDocumentTransformerOptions must not be null");
		this.dashScopeApi = dashScopeApi;
		this.options = options;
	}

	private List<Document> doSplitDocuments(List<Document> documents) {
		validateDocuments(documents);
		Document document = documents.get(0);

		ResponseEntity<DashScopeApi.DocumentSplitResponse> splitResponseEntity = dashScopeApi.documentSplit(document,
				options);
		validateSplitResponse(splitResponseEntity);

		DashScopeApi.DocumentSplitResponse splitResponse = splitResponseEntity.getBody();
		validateChunkResult(splitResponse);

		List<DashScopeApi.DocumentSplitResponse.DocumentChunk> chunkList = splitResponse.chunkService().chunkResult();
		List<Document> documentList = new ArrayList<>();

		chunkList.forEach(e -> {
			Document chunk = new Document(document.getId() + "_" + e.chunkId(), e.content(), document.getMetadata());
			documentList.add(chunk);
		});

		return documentList;
	}

	private void validateDocuments(List<Document> documents) {
		if (CollectionUtils.isEmpty(documents)) {
			throw new RuntimeException("Documents must not be null");
		}
		if (documents.size() > 1) {
			throw new RuntimeException("Just support one Document");
		}
	}

	private void validateSplitResponse(ResponseEntity<DashScopeApi.DocumentSplitResponse> splitResponseEntity) {
		if (splitResponseEntity == null) {
			throw new DashScopeException(ErrorCodeEnum.SPLIT_DOCUMENT_ERROR);
		}
	}

	private void validateChunkResult(DashScopeApi.DocumentSplitResponse splitResponse) {
		if (splitResponse == null || splitResponse.chunkService() == null
				|| CollectionUtils.isEmpty(splitResponse.chunkService().chunkResult())) {
			logger.error("DashScopeDocumentTransformer NoSplitResult");
			throw new DashScopeException(ErrorCodeEnum.SPLIT_DOCUMENT_ERROR);
		}
	}

	@Override
	public List<Document> apply(List<Document> documents) {
		return this.doSplitDocuments(documents);
	}

}
