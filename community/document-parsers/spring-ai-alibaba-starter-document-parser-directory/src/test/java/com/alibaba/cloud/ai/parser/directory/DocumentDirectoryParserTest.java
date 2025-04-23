

package com.alibaba.cloud.ai.parser.directory;

import com.alibaba.cloud.ai.document.TextDocumentParser;
import org.junit.Test;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * @author HeYQ
 * @author brianxiadong
 * @since 2025-02-07 19:21
 */

public class DocumentDirectoryParserTest {

	// Load all non-hidden files in a directory.
	@Test
	public void testAllNoHidden() {
		String path = "src/test/resources";

		DocumentDirectoryParser parser = new DocumentDirectoryParser.Builder(path)
			.documentParser(new TextDocumentParser())
			.build();
		List<Document> documents = parser.parse();
		for (Document document : documents) {
			System.out.println(document.getText());
		}
	}

	// Load all text files in a directory without recursion.
	@Test
	public void testAllTextNoHidden() {
		String path = "src/test/resources";

		DocumentDirectoryParser parser = new DocumentDirectoryParser.Builder(path)
			.documentParser(new TextDocumentParser())
			.glob("*.txt")
			.build();
		List<Document> documents = parser.parse();
		for (Document document : documents) {
			System.out.println(document.getText());
		}
	}

	// Recursively load all text files in a directory.
	@Test
	public void testAllTextRecursive() {
		String path = "src/test/resources";
		DocumentDirectoryParser parser = new DocumentDirectoryParser.Builder(path)
			.documentParser(new TextDocumentParser())
			.glob("*.txt")
			.recursive(true)
			.build();
		List<Document> documents = parser.parse();
		for (Document document : documents) {
			System.out.println(document.getText());
		}
	}

	// Load all files in a directory, except for py files.
	@Test
	public void testExceptNoHidden() {
		String path = "src/test/resources";

		DocumentDirectoryParser parser = new DocumentDirectoryParser.Builder(path)
			.documentParser(new TextDocumentParser())
			.exclude("*.py")
			.build();
		List<Document> documents = parser.parse();
		for (Document document : documents) {
			System.out.println(document.getText());
		}
	}

}
