
package com.alibaba.cloud.ai.reader.notion;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.document.Document;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Notion Document Reader
 *
 * Tests are only run if NOTION_TOKEN environment variable is set.
 *
 * @author xiadong
 * @since 2024-01-06
 */
@EnabledIfEnvironmentVariable(named = "NOTION_TOKEN", matches = ".+")
class NotionDocumentReaderIT {

	private static final String NOTION_TOKEN = System.getenv("NOTION_TOKEN");

	// Test page ID
	private static final String TEST_PAGE_ID = "${pageId}";

	// Test database ID
	private static final String TEST_DATABASE_ID = "${databaseId}";

	// Static initializer to log a message if environment variable is not set
	static {
		if (NOTION_TOKEN == null || NOTION_TOKEN.isEmpty()) {
			System.out.println("Skipping Notion tests because NOTION_TOKEN environment variable is not set.");
		}
	}

	NotionDocumentReader pageReader;

	NotionDocumentReader databaseReader;

	@BeforeEach
	public void beforeEach() {
		// Only initialize if NOTION_TOKEN is set
		if (NOTION_TOKEN != null && !NOTION_TOKEN.isEmpty()) {
			// Create page reader
			NotionResource pageResource = NotionResource.builder()
				.notionToken(NOTION_TOKEN)
				.resourceId(TEST_PAGE_ID)
				.resourceType(NotionResource.ResourceType.PAGE)
				.build();
			pageReader = new NotionDocumentReader(pageResource);

			// Create database reader
			NotionResource databaseResource = NotionResource.builder()
				.notionToken(NOTION_TOKEN)
				.resourceId(TEST_DATABASE_ID)
				.resourceType(NotionResource.ResourceType.DATABASE)
				.build();
			databaseReader = new NotionDocumentReader(databaseResource);
		}
	}

	@Test
	void should_load_page() {
		// Skip test if pageReader is null
		Assumptions.assumeTrue(pageReader != null,
				"Skipping test because NotionDocumentReader could not be initialized");

		// when
		List<Document> documents = pageReader.get();

		// then
		assertThat(documents).isNotEmpty();
		Document document = documents.get(0);

		// Verify metadata
		assertThat(document.getMetadata()).containsKey(NotionResource.SOURCE);
		assertThat(document.getMetadata().get(NotionResource.SOURCE)).isEqualTo("notion://page/" + TEST_PAGE_ID);
		assertThat(document.getMetadata().get("resourceType")).isEqualTo(NotionResource.ResourceType.PAGE.name());
		assertThat(document.getMetadata().get("resourceId")).isEqualTo(TEST_PAGE_ID);

		// Verify content
		String content = document.getText();
		assertThat(content).isNotEmpty();
		System.out.println("Page content: " + content);
	}

	@Test
	void should_load_database() {
		// Skip test if databaseReader is null
		Assumptions.assumeTrue(databaseReader != null,
				"Skipping test because NotionDocumentReader could not be initialized");

		// when
		List<Document> documents = databaseReader.get();

		// then
		assertThat(documents).isNotEmpty();
		Document document = documents.get(0);

		// Verify metadata
		assertThat(document.getMetadata()).containsKey(NotionResource.SOURCE);
		assertThat(document.getMetadata().get(NotionResource.SOURCE))
			.isEqualTo("notion://database/" + TEST_DATABASE_ID);
		assertThat(document.getMetadata().get("resourceType")).isEqualTo(NotionResource.ResourceType.DATABASE.name());
		assertThat(document.getMetadata().get("resourceId")).isEqualTo(TEST_DATABASE_ID);

		// Verify content
		String content = document.getText();
		assertThat(content).isNotEmpty();
		System.out.println("Database content: " + content);
	}

}
