package com.alibaba.cloud.ai.document;

import org.springframework.ai.document.Document;
import org.springframework.util.Assert;

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
			String text = new String(inputStream.readAllBytes(), charset);
			if (text.isBlank()) {
				throw new Exception();
			}
			return Collections.singletonList(new Document(text));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
