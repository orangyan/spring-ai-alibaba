
package com.alibaba.cloud.ai.parser.bshtml;

import com.alibaba.cloud.ai.document.DocumentParser;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.springframework.ai.document.Document;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author HeYQ
 * @since 2025-01-02 18:26
 */

public class BsHtmlDocumentParser implements DocumentParser {

	private final String charsetName;

	private final String baseUri;

	private final Parser parser;

	public BsHtmlDocumentParser(Parser parser) {
		this("UTF-8", "", parser);
	}

	public BsHtmlDocumentParser(String charsetName, String baseUri) {
		this(charsetName, baseUri, null);
	}

	public BsHtmlDocumentParser() {
		this("UTF-8", "", Parser.htmlParser().newInstance());
	}

	public BsHtmlDocumentParser(String charsetName, String baseUri, Parser parser) {
		this.charsetName = charsetName;
		this.baseUri = baseUri;
		this.parser = parser;
	}

	@Override
	public List<Document> parse(InputStream inputStream) {
		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(inputStream, charsetName, baseUri, parser);
			String text = doc.text();
			String title = doc.title().isEmpty() ? "" : doc.title();
			Document document = new Document(text);
			Map<String, Object> metaData = document.getMetadata();
			metaData.put("title", title);
			metaData.put("source", baseUri);
			metaData.put("originalDocument", doc);

			return List.of(document);

		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
