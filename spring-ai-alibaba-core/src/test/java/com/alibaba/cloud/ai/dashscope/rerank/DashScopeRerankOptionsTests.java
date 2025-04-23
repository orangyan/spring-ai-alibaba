
package com.alibaba.cloud.ai.dashscope.rerank;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for DashScopeRerankOptions. Tests cover default values, builder pattern, and
 * property modifications.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class DashScopeRerankOptionsTests {

	// Test constants
	private static final String TEST_MODEL = "test-rerank-model";

	private static final Integer TEST_TOP_N = 5;

	private static final Boolean TEST_RETURN_DOCUMENTS = true;

	/**
	 * Test default values of DashScopeRerankOptions. Verifies that a newly created
	 * instance has the expected default values: - model = "gte-rerank" - topN = 3 -
	 * returnDocuments = false
	 */
	@Test
	void testDefaultValues() {
		DashScopeRerankOptions options = DashScopeRerankOptions.builder().build();

		assertThat(options.getModel()).isEqualTo("gte-rerank");
		assertThat(options.getTopN()).isEqualTo(3);
		assertThat(options.getReturnDocuments()).isFalse();
	}

	/**
	 * Test builder pattern with all properties set. Verifies that all properties can be
	 * set using the builder pattern and are correctly assigned to the created instance.
	 */
	@Test
	void testBuilderPattern() {
		DashScopeRerankOptions options = DashScopeRerankOptions.builder()
			.withModel(TEST_MODEL)
			.withTopN(TEST_TOP_N)
			.withReturnDocuments(TEST_RETURN_DOCUMENTS)
			.build();

		assertThat(options.getModel()).isEqualTo(TEST_MODEL);
		assertThat(options.getTopN()).isEqualTo(TEST_TOP_N);
		assertThat(options.getReturnDocuments()).isEqualTo(TEST_RETURN_DOCUMENTS);
	}

	/**
	 * Test setters and getters. Verifies that all properties can be modified after
	 * instance creation using setter methods and retrieved using getter methods.
	 */
	@Test
	void testSettersAndGetters() {
		DashScopeRerankOptions options = DashScopeRerankOptions.builder().build();

		options.setModel(TEST_MODEL);
		options.setTopN(TEST_TOP_N);
		options.setReturnDocuments(TEST_RETURN_DOCUMENTS);

		assertThat(options.getModel()).isEqualTo(TEST_MODEL);
		assertThat(options.getTopN()).isEqualTo(TEST_TOP_N);
		assertThat(options.getReturnDocuments()).isEqualTo(TEST_RETURN_DOCUMENTS);
	}

	/**
	 * Test builder with partial values set. Verifies that when only some properties are
	 * set using the builder, the unset properties retain their default values.
	 */
	@Test
	void testBuilderWithPartialValues() {
		DashScopeRerankOptions options = DashScopeRerankOptions.builder()
			.withModel(TEST_MODEL)
			.withTopN(TEST_TOP_N)
			.build();

		assertThat(options.getModel()).isEqualTo(TEST_MODEL);
		assertThat(options.getTopN()).isEqualTo(TEST_TOP_N);
		assertThat(options.getReturnDocuments()).isFalse(); // Default value
	}

}
