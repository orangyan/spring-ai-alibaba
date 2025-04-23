
package com.alibaba.cloud.ai.reader.youtube;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * @author XiaoYunTao
 * @since 2025/1/18
 */
public class YoutubeDocumentReaderTest {

	private static final Logger logger = LoggerFactory.getLogger(YoutubeDocumentReaderTest.class);

	@Test
	void bilibiliDocumentReaderTest() {
		YoutubeDocumentReader youtubeDocumentReader = new YoutubeDocumentReader(
				"https://www.youtube.com/watch?v=q-9wxg9tQRk");
		List<Document> documents = youtubeDocumentReader.get();
		logger.info("documents: {}", documents);
	}

}
