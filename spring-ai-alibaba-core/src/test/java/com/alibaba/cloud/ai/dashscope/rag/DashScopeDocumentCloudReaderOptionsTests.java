
package com.alibaba.cloud.ai.dashscope.rag;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for DashScopeDocumentCloudReaderOptions. Tests cover default constructor and
 * parameterized constructor behavior.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class DashScopeDocumentCloudReaderOptionsTests {

	@Test
	void testDefaultConstructor() {
		// Test default constructor
		DashScopeDocumentCloudReaderOptions options = new DashScopeDocumentCloudReaderOptions();

		// Verify default value is "default"
		assertThat(options.getCategoryId()).isEqualTo("default");
	}

	@Test
	void testParameterizedConstructor() {
		// Test parameterized constructor
		String customCategoryId = "custom-category";
		DashScopeDocumentCloudReaderOptions options = new DashScopeDocumentCloudReaderOptions(customCategoryId);

		// Verify custom value is set correctly
		assertThat(options.getCategoryId()).isEqualTo(customCategoryId);
	}

}
