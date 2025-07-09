
package com.alibaba.cloud.ai.toolcalling.yuque;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @author 北极星
 */
public class YuqueDeleteDocService
		implements Function<YuqueDeleteDocService.DeleteDocRequest, YuqueDeleteDocService.DeleteDocResponse> {

	private static final Logger logger = LoggerFactory.getLogger(YuqueDeleteDocService.class);

	private final WebClientTool webClientTool;

	private final JsonParseTool jsonParseTool;

	public YuqueDeleteDocService(WebClientTool webClientTool, JsonParseTool jsonParseTool) {
		this.webClientTool = webClientTool;
		this.jsonParseTool = jsonParseTool;
	}

	@Override
	public YuqueDeleteDocService.DeleteDocResponse apply(YuqueDeleteDocService.DeleteDocRequest deleteDocRequest) {
		if (deleteDocRequest == null || deleteDocRequest.bookId == null) {
			return null;
		}
		String uri = "/repos/" + deleteDocRequest.bookId + "/docs/" + deleteDocRequest.id;
		try {
			String json = webClientTool.delete(uri).block();
			return jsonParseTool.jsonToObject(json, DeleteDocResponse.class);
		}
		catch (Exception e) {
			logger.error("Failed to delete the Yuque document.", e);
			return null;
		}
	}

	public record DeleteDocRequest(@JsonProperty("bookId") String bookId, @JsonProperty("id") String id) {
	}

	public record DeleteDocResponse(@JsonProperty("data") YuqueConstants.DocSerializer data) {
	}

}
