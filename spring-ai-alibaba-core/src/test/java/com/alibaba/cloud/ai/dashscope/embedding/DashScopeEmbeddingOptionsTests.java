
package com.alibaba.cloud.ai.dashscope.embedding;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingOptions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for DashScopeEmbeddingOptions. Tests cover builder pattern, getters/setters,
 * and JSON properties.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class DashScopeEmbeddingOptionsTests {

	private static final String TEST_MODEL = "text-embedding-v2";

	private static final String TEST_TEXT_TYPE = "document";

	private static final Integer TEST_DIMENSIONS = 1536;

	@Test
	void testBuilderAndGetters() {
		// Test building DashScopeEmbeddingOptions using builder pattern and verify
		// getters
		DashScopeEmbeddingOptions options = DashScopeEmbeddingOptions.builder()
			.withModel(TEST_MODEL)
			.withTextType(TEST_TEXT_TYPE)
			.withDimensions(TEST_DIMENSIONS)
			.build();

		// Verify all fields are set correctly
		assertThat(options.getModel()).isEqualTo(TEST_MODEL);
		assertThat(options.getTextType()).isEqualTo(TEST_TEXT_TYPE);
		assertThat(options.getDimensions()).isEqualTo(TEST_DIMENSIONS);
	}

	@Test
	void testSettersAndGetters() {
		// Test setters and getters
		DashScopeEmbeddingOptions options = new DashScopeEmbeddingOptions();

		options.setModel(TEST_MODEL);
		options.setTextType(TEST_TEXT_TYPE);
		options.setDimensions(TEST_DIMENSIONS);

		// Verify all fields are set correctly
		assertThat(options.getModel()).isEqualTo(TEST_MODEL);
		assertThat(options.getTextType()).isEqualTo(TEST_TEXT_TYPE);
		assertThat(options.getDimensions()).isEqualTo(TEST_DIMENSIONS);
	}

	@Test
	void testDefaultValues() {
		// Test default values when creating a new instance
		DashScopeEmbeddingOptions options = new DashScopeEmbeddingOptions();

		// Verify default values are null
		assertThat(options.getModel()).isNull();
		assertThat(options.getTextType()).isNull();
		assertThat(options.getDimensions()).isNull();
	}

	@Test
	void testBuilderWithDefaultModel() {
		// Test builder with default embedding model
		DashScopeEmbeddingOptions options = DashScopeEmbeddingOptions.builder()
			.withModel(DashScopeApi.DEFAULT_EMBEDDING_MODEL)
			.build();

		// Verify default model is set correctly
		assertThat(options.getModel()).isEqualTo(DashScopeApi.DEFAULT_EMBEDDING_MODEL);
	}

	@Test
	void testBuilderWithDefaultTextType() {
		// Test builder with default text type
		DashScopeEmbeddingOptions options = DashScopeEmbeddingOptions.builder()
			.withTextType(DashScopeApi.DEFAULT_EMBEDDING_TEXT_TYPE)
			.build();

		// Verify default text type is set correctly
		assertThat(options.getTextType()).isEqualTo(DashScopeApi.DEFAULT_EMBEDDING_TEXT_TYPE);
	}

	@Test
	void testImplementsEmbeddingOptions() {
		// Test that DashScopeEmbeddingOptions implements EmbeddingOptions interface
		DashScopeEmbeddingOptions options = new DashScopeEmbeddingOptions();

		assertThat(options).isInstanceOf(EmbeddingOptions.class);
	}

}
