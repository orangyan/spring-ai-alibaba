
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
public class YuqueUpdateDocService
		implements Function<YuqueUpdateDocService.updateDocRequest, YuqueUpdateDocService.updateDocResponse> {

	private static final Logger logger = LoggerFactory.getLogger(YuqueUpdateDocService.class);

	private final WebClientTool webClientTool;

	private final JsonParseTool jsonParseTool;

	public YuqueUpdateDocService(WebClientTool webClientTool, JsonParseTool jsonParseTool) {
		this.webClientTool = webClientTool;
		this.jsonParseTool = jsonParseTool;
	}

	@Override
	public YuqueUpdateDocService.updateDocResponse apply(YuqueUpdateDocService.updateDocRequest updateDocRequest) {
		if (updateDocRequest.bookId == null || updateDocRequest.id == null) {
			return null;
		}
		String uri = "/" + updateDocRequest.bookId + "/docs/" + updateDocRequest.id;
		try {
			String json = webClientTool.put(uri, updateDocRequest).block();
			return jsonParseTool.jsonToObject(json, updateDocResponse.class);
		}
		catch (Exception e) {
			logger.error("Failed to update the Yuque document.", e);
			return null;
		}
	}

	protected record updateDocRequest(@JsonProperty("bookId") String bookId, @JsonProperty("id") String id) {
	}

	protected record updateDocResponse(@JsonProperty("id") String id, @JsonProperty("slug") String slug,
			@JsonProperty("type") String type, @JsonProperty("description") String description,
			@JsonProperty("cover") String cover, @JsonProperty("user_id") String userId,
			@JsonProperty("book_id") String bookId, @JsonProperty("last_editor_id") String lastEditorId,
			@JsonProperty("format") String format, @JsonProperty("body_draft") String bodyDraft,
			@JsonProperty("body_sheet") String bodySheet, @JsonProperty("body_table") String bodyTable,
			@JsonProperty("body_html") String bodyHtml, @JsonProperty("public") int isPublic,
			@JsonProperty("status") String status, @JsonProperty("likes_count") int likesCount,
			@JsonProperty("read_count") int readCount, @JsonProperty("comments_count") int commentsCount,
			@JsonProperty("word_count") int wordCount, @JsonProperty("created_at") String createdAt,
			@JsonProperty("updated_at") String updatedAt) {
	}

}
