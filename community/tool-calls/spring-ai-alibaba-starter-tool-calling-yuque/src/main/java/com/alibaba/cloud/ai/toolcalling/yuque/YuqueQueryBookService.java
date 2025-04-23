
package com.alibaba.cloud.ai.toolcalling.yuque;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.function.Function;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author 北极星
 */
public class YuqueQueryBookService
		implements Function<YuqueQueryBookService.queryBookRequest, YuqueQueryBookService.queryBookResponse> {

	private static final Logger logger = LoggerFactory.getLogger(YuqueQueryBookService.class);

	private final WebClientTool webClientTool;

	private final JsonParseTool jsonParseTool;

	public YuqueQueryBookService(WebClientTool webClientTool, JsonParseTool jsonParseTool) {
		this.webClientTool = webClientTool;
		this.jsonParseTool = jsonParseTool;
	}

	@Override
	public queryBookResponse apply(queryBookRequest queryBookRequest) {
		if (queryBookRequest == null || queryBookRequest.bookId == null) {
			return null;
		}
		String uri = "/" + queryBookRequest.bookId + "/docs";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		if (queryBookRequest.slug() != null) {
			params.add("slug", queryBookRequest.slug());
		}
		if (queryBookRequest.title() != null) {
			params.add("title", queryBookRequest.title());
		}
		if (queryBookRequest.id() != null) {
			params.add("id", queryBookRequest.id());
		}
		try {
			String json = webClientTool.get(uri, params).block();
			return jsonParseTool.jsonToObject(json, queryBookResponse.class);
		}
		catch (Exception e) {
			logger.error("Failed to query the Yuque book.", e);
			return null;
		}
	}

	protected record queryBookRequest(@JsonProperty("slug") String slug, @JsonProperty("title") String title,
			String bookId, String id) {
	}

	protected record queryBookResponse(@JsonProperty("meta") int meta, @JsonProperty("data") List<data> data) {
	}

	protected record data(@JsonProperty("id") String id, @JsonProperty("docId") String docId,
			@JsonProperty("slug") String slug, @JsonProperty("title") String title,
			@JsonProperty("userId") String userId, @JsonProperty("user") YuqueQueryDocService.UserSerializer user,
			@JsonProperty("draft") String draft, @JsonProperty("body") String body,
			@JsonProperty("bodyAsl") String bodyAsl, @JsonProperty("bodyHtml") String bodyHtml,
			@JsonProperty("createdAt") String createdAt, @JsonProperty("updatedAt") String updatedAt) {
	}

}
