package com.alibaba.cloud.ai.document;

import org.springframework.ai.document.Document;

import java.io.InputStream;
import java.util.List;

/**
 * DocumentParser接口定义了文档解析的通用规范
 * 此接口旨在为不同的文档类型提供统一的解析方法入口
 */
public interface DocumentParser {

	/**
	 * Parses a given {@link InputStream} into a {@link Document}. The specific
	 * implementation of this method will depend on the type of the document being parsed.
	 * <p>
	 * Note: This method does not close the provided {@link InputStream} - it is the
	 * caller's responsibility to manage the lifecycle of the stream.
	 * @param inputStream The {@link InputStream} that contains the content of the
	 * {@link Document}.
	 * @return The parsed {@link Document}.
	 */
	List<Document> parse(InputStream inputStream);

}
