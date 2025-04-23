
package com.alibaba.cloud.ai.model;

import org.junit.jupiter.api.Test;
import org.springframework.ai.model.ResultMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for RerankResultMetadata. Tests the basic functionality and interface
 * implementation.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class RerankResultMetadataTests {

	@Test
	void testInstanceCreation() {
		// Test that we can create an instance of RerankResultMetadata
		RerankResultMetadata metadata = new RerankResultMetadata();

		// Verify the instance is not null
		assertThat(metadata).isNotNull();
	}

	@Test
	void testImplementsResultMetadata() {
		// Test that RerankResultMetadata implements ResultMetadata interface
		RerankResultMetadata metadata = new RerankResultMetadata();

		// Verify the instance implements ResultMetadata interface
		assertThat(metadata).isInstanceOf(ResultMetadata.class);
	}

	@Test
	void testToString() {
		// Test toString method
		RerankResultMetadata metadata = new RerankResultMetadata();

		// Verify toString returns non-null value
		assertThat(metadata.toString()).isNotNull();
	}

}
