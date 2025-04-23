
package com.alibaba.cloud.ai.dashscope.protocol;

import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for DashScopeWebSocketClientOptions. Tests cover builder pattern, default
 * values, and property setters.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class DashScopeWebSocketClientOptionsTests {

	@Test
	void testDefaultConstructor() {
		// Test default constructor initializes with default values
		DashScopeWebSocketClientOptions options = new DashScopeWebSocketClientOptions();

		// Verify default values
		assertThat(options.getUrl()).isEqualTo(DashScopeApiConstants.DEFAULT_WEBSOCKET_URL);
		assertThat(options.getApiKey()).isNull();
		assertThat(options.getWorkSpaceId()).isNull();
	}

	@Test
	void testBuilderPattern() {
		// Test builder pattern with all properties set
		String testUrl = "wss://test.example.com";
		String testApiKey = "test-api-key";
		String testWorkspaceId = "test-workspace";

		DashScopeWebSocketClientOptions options = DashScopeWebSocketClientOptions.builder()
			.withUrl(testUrl)
			.withApiKey(testApiKey)
			.withWorkSpaceId(testWorkspaceId)
			.build();

		// Verify all properties are set correctly
		assertThat(options.getUrl()).isEqualTo(testUrl);
		assertThat(options.getApiKey()).isEqualTo(testApiKey);
		assertThat(options.getWorkSpaceId()).isEqualTo(testWorkspaceId);
	}

	@Test
	void testSetterMethods() {
		// Test setter methods
		DashScopeWebSocketClientOptions options = new DashScopeWebSocketClientOptions();

		String testUrl = "wss://test.example.com";
		String testApiKey = "test-api-key";
		String testWorkspaceId = "test-workspace";

		// Set values using setters
		options.setUrl(testUrl);
		options.setApiKey(testApiKey);
		options.setWorkSpaceId(testWorkspaceId);

		// Verify values are set correctly
		assertThat(options.getUrl()).isEqualTo(testUrl);
		assertThat(options.getApiKey()).isEqualTo(testApiKey);
		assertThat(options.getWorkSpaceId()).isEqualTo(testWorkspaceId);
	}

	@Test
	void testBuilderWithDefaultUrl() {
		// Test builder with only required fields
		String testApiKey = "test-api-key";

		DashScopeWebSocketClientOptions options = DashScopeWebSocketClientOptions.builder()
			.withApiKey(testApiKey)
			.build();

		// Verify default URL is used when not specified
		assertThat(options.getUrl()).isEqualTo(DashScopeApiConstants.DEFAULT_WEBSOCKET_URL);
		assertThat(options.getApiKey()).isEqualTo(testApiKey);
		assertThat(options.getWorkSpaceId()).isNull();
	}

	@Test
	void testBuilderWithNullWorkspaceId() {
		// Test builder explicitly setting null workspace ID
		String testUrl = "wss://test.example.com";
		String testApiKey = "test-api-key";

		DashScopeWebSocketClientOptions options = DashScopeWebSocketClientOptions.builder()
			.withUrl(testUrl)
			.withApiKey(testApiKey)
			.withWorkSpaceId(null)
			.build();

		// Verify properties are set correctly with null workspace ID
		assertThat(options.getUrl()).isEqualTo(testUrl);
		assertThat(options.getApiKey()).isEqualTo(testApiKey);
		assertThat(options.getWorkSpaceId()).isNull();
	}

}
