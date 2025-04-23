
package com.alibaba.cloud.ai.reader.chatgpt.data;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for ChatGptDataDocumentReader
 *
 * @author brianxiadong
 */
class ChatGptDataDocumentReaderTests {

	// Path to test file
	private static final String TEST_FILE_PATH = "src/test/resources/conversations.json";

	@Test
	void shouldLoadAllDocuments() {
		// Create reader with fixed file path
		ChatGptDataDocumentReader reader = new ChatGptDataDocumentReader(TEST_FILE_PATH);
		List<Document> documents = reader.get();

		assertThat(documents).isNotEmpty();
	}

	@Test
	void shouldLoadLimitedDocuments() {
		// Load only 2 records
		ChatGptDataDocumentReader reader = new ChatGptDataDocumentReader(TEST_FILE_PATH, 2);
		List<Document> documents = reader.get();

		assertThat(documents).hasSize(2);
	}

	@Test
	void shouldThrowExceptionForInvalidFile() {
		String invalidPath = "non-existent-file.json";
		ChatGptDataDocumentReader reader = new ChatGptDataDocumentReader(invalidPath);

		assertThrows(RuntimeException.class, () -> {
			reader.get();
		});
	}

}
