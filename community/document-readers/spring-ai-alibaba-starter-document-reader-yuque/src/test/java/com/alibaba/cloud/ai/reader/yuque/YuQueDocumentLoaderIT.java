
package com.alibaba.cloud.ai.reader.yuque;

import com.alibaba.cloud.ai.parser.tika.TikaDocumentParser;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * Integration tests for YuQueDocumentReader. Tests will be skipped if YUQUE_TOKEN and
 * YUQUE_RESOURCE_PATH environment variables are not set.
 */
@EnabledIfEnvironmentVariable(named = "YUQUE_TOKEN", matches = ".+")
@EnabledIfEnvironmentVariable(named = "YUQUE_RESOURCE_PATH", matches = ".+")
class YuQueDocumentLoaderIT {

	private static final String YU_QUE_TOKEN = System.getenv("YUQUE_TOKEN");

	private static final String RESOURCE_PATH = System.getenv("YUQUE_RESOURCE_PATH");

	YuQueDocumentReader reader;

	YuQueResource source;

	static {
		if (YU_QUE_TOKEN == null || RESOURCE_PATH == null) {
			System.out
				.println("YUQUE_TOKEN or YUQUE_RESOURCE_PATH environment variable is not set. Tests will be skipped.");
		}
	}

	@BeforeEach
	public void beforeEach() {
		// Skip test if environment variables are not set
		Assumptions.assumeTrue(YU_QUE_TOKEN != null && !YU_QUE_TOKEN.isEmpty(),
				"Skipping test because YUQUE_TOKEN is not set");
		Assumptions.assumeTrue(RESOURCE_PATH != null && !RESOURCE_PATH.isEmpty(),
				"Skipping test because YUQUE_RESOURCE_PATH is not set");

		source = YuQueResource.builder().yuQueToken(YU_QUE_TOKEN).resourcePath(RESOURCE_PATH).build();
		reader = new YuQueDocumentReader(source, new TikaDocumentParser());
	}

	@Test
	public void should_load_file() {
		// Skip test if reader is not initialized
		Assumptions.assumeTrue(reader != null, "Skipping test because reader is not initialized");

		List<Document> document = reader.get();
		String content = document.get(0).getText();

		System.out.println(content);
	}

}
