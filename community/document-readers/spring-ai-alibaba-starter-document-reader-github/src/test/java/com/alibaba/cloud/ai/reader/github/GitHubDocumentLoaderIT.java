
package com.alibaba.cloud.ai.reader.github;

import com.alibaba.cloud.ai.document.TextDocumentParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.Assumptions;
import org.springframework.ai.document.Document;
import com.alibaba.cloud.ai.document.DocumentParser;
import java.util.List;

/**
 * Integration tests for GitHub document loader. Tests are only run if GITHUB_TOKEN
 * environment variable is set.
 */
@EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
class GitHubDocumentLoaderIT {

	private static final String TEST_OWNER = "sincerity-being";

	private static final String TEST_REPO = "llm-course";

	// Static initializer to log a message if GITHUB_TOKEN is not set
	static {
		if (System.getenv("GITHUB_TOKEN") == null || System.getenv("GITHUB_TOKEN").isEmpty()) {
			System.out
				.println("Skipping GitHub document loader tests because GITHUB_TOKEN environment variable is not set.");
		}
	}

	private GitHubDocumentReader reader;

	private GitHubResource source;

	private DocumentParser parser = new TextDocumentParser();

	@BeforeEach
	public void beforeEach() {
		// Only create the GitHubResource and reader if GITHUB_TOKEN is available
		String githubToken = System.getenv("GITHUB_TOKEN");
		if (githubToken != null && !githubToken.isEmpty()) {
			source = GitHubResource.builder()
				.gitHubToken(githubToken)
				.owner(TEST_OWNER)
				.repo(TEST_REPO)
				.branch("main")
				.path("Mergekit.ipynb")
				.build();

			reader = new GitHubDocumentReader(source, parser);
		}
	}

	@Test
	public void should_load_file() {
		// Skip test if reader is null (which means GITHUB_TOKEN is not set)
		Assumptions.assumeTrue(reader != null, "Skipping test because GITHUB_TOKEN is not set");

		List<Document> document = reader.get();
		String content = document.get(0).getText();
		System.out.println(content);
		// assertThat(content).contains("<groupId>com.alibaba.cloud</groupId>");
	}

}
