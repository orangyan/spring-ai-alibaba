
package com.alibaba.cloud.ai.reader.email.msg;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for MSG Email Document Reader
 *
 * @author brianxiadong
 * @since 2024-01-19
 */
class MsgEmailDocumentReaderTest {

	@Test
	void should_read_msg_file() throws IOException {
		// Given
		ClassPathResource emailResource = new ClassPathResource("strangeDate.msg");
		MsgEmailDocumentReader reader = new MsgEmailDocumentReader(emailResource.getFile().getAbsolutePath());

		// When
		List<Document> documents = reader.get();

		// Then
		assertNotNull(documents);
		assertEquals(1, documents.size());

		Document emailDoc = documents.get(0);
		Map<String, Object> metadata = emailDoc.getMetadata();

		// Verify metadata
		assertNotNull(metadata);
		assertNotEquals(0, metadata.size());

		// Verify content
		String content = emailDoc.getText();
		assertNotEquals("", content);
	}

	@Test
	void should_read_msg_file2() throws IOException {
		// Given
		ClassPathResource emailResource = new ClassPathResource("unicode.msg");
		MsgEmailDocumentReader reader = new MsgEmailDocumentReader(emailResource.getFile().getAbsolutePath());

		// When
		List<Document> documents = reader.get();

		// Then
		assertNotNull(documents);
		assertEquals(1, documents.size());

		Document emailDoc = documents.get(0);
		Map<String, Object> metadata = emailDoc.getMetadata();

		// Verify metadata
		assertNotNull(metadata);
		assertNotEquals(0, metadata.size());

		// Verify content
		String content = emailDoc.getText();
		assertNotEquals("", content);
	}

	@Test
	void should_handle_missing_file() {
		// Given
		MsgEmailDocumentReader reader = new MsgEmailDocumentReader("non-existent.msg");

		// When & Then
		assertThrows(RuntimeException.class, reader::get);
	}

	@Test
	void should_handle_invalid_msg_file() throws IOException {
		// Given
		ClassPathResource invalidResource = new ClassPathResource("1.eml");
		MsgEmailDocumentReader reader = new MsgEmailDocumentReader(invalidResource.getFile().getAbsolutePath());

		// When & Then
		assertThrows(RuntimeException.class, reader::get);
	}

}
