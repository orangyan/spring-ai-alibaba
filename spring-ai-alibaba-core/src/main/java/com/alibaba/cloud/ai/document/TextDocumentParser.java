package com.alibaba.cloud.ai.document;

import org.springframework.ai.document.Document;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * TextDocumentParser类实现了DocumentParser接口，用于解析文本类型的文档
 * 该类提供了处理和解析文本文档的方法，旨在将非结构化的文本数据转换为结构化数据
 * 以便进一步处理或存储
 */
public class TextDocumentParser implements DocumentParser {

	private final Charset charset;

	public TextDocumentParser() {
		this(StandardCharsets.UTF_8);
	}

	public TextDocumentParser(Charset charset) {
		Assert.notNull(charset, "charset");
		this.charset = charset;
	}

	@Override
	public List<Document> parse(InputStream inputStream) {
		try {
			// Read all text from the input stream and decode it using the specified
			// character set.
			String text = new String(inputStream.readAllBytes(), this.charset);
			// If the text is completely empty, report an illegal argument exception.
			if (text.isBlank()) {
				throw new IllegalArgumentException("text must not be blank");
			}
			return Collections.singletonList(new Document(text));
		}
		catch (IOException e) {
			// Convert any IO exception into a RuntimeException for propagation.
			throw new RuntimeException(e);
		}
	}

}
