
package com.alibaba.cloud.ai.toolcalling.yuque;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

/**
 * @author 北极星
 */
public class YuqueQueryBookService
		implements Function<YuqueQueryBookService.QueryBookRequest, YuqueQueryBookService.QueryBookResponse> {

	private static final Logger logger = LoggerFactory.getLogger(YuqueQueryBookService.class);

	private final WebClientTool webClientTool;

	private final JsonParseTool jsonParseTool;

	public YuqueQueryBookService(WebClientTool webClientTool, JsonParseTool jsonParseTool) {
		this.webClientTool = webClientTool;
		this.jsonParseTool = jsonParseTool;
	}

	@Override
	public QueryBookResponse apply(QueryBookRequest queryBookRequest) {
		if (queryBookRequest == null || queryBookRequest.bookId == null) {
			return null;
		}
		String uri = "/repos/" + queryBookRequest.bookId + "/docs";
		try {
			String json = webClientTool.get(uri).block();
			return jsonParseTool.jsonToObject(json, QueryBookResponse.class);
		}
		catch (Exception e) {
			logger.error("Failed to query the Yuque book.", e);
			return null;
		}
	}

	protected record QueryBookRequest(String bookId) {
	}

	protected record QueryBookResponse(@JsonProperty("meta") Meta meta,
			@JsonProperty("data") List<YuqueConstants.DocSerializer> data) {
	}

	protected record Meta(@JsonProperty("total") String total) {
	}

}
