
package com.alibaba.cloud.ai.reader.email.msg;

import org.springframework.ai.document.Document;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * MSG Email Parser Responsible for converting MsgEmailElement to Document object
 *
 * @author xiadong
 * @since 2024-01-19
 */
public class MsgEmailParser {

	private MsgEmailParser() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Convert MsgEmailElement to Document
	 * @param element MSG email element
	 * @return Document object
	 */
	public static Document convertToDocument(MsgEmailElement element) {
		if (element == null) {
			throw new IllegalArgumentException("MsgEmailElement cannot be null");
		}

		// Build metadata
		Map<String, Object> metadata = new HashMap<>();

		// Add metadata with null check
		if (StringUtils.hasText(element.getSubject())) {
			metadata.put("subject", element.getSubject());
		}

		if (StringUtils.hasText(element.getFrom())) {
			metadata.put("from", element.getFrom());
		}

		if (StringUtils.hasText(element.getFromName())) {
			metadata.put("from_name", element.getFromName());
		}

		if (StringUtils.hasText(element.getTo())) {
			metadata.put("to", element.getTo());
		}

		if (StringUtils.hasText(element.getToName())) {
			metadata.put("to_name", element.getToName());
		}

		if (StringUtils.hasText(element.getDate())) {
			metadata.put("date", element.getDate());
		}

		if (StringUtils.hasText(element.getTextType())) {
			metadata.put("content_type", element.getTextType());
		}

		// Create Document object with content null check
		String content = StringUtils.hasText(element.getText()) ? element.getText() : "";
		return new Document(content, metadata);
	}

}
