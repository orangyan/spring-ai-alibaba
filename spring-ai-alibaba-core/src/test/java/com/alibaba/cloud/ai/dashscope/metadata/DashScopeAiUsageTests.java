
package com.alibaba.cloud.ai.dashscope.metadata;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.TokenUsage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test cases for DashScopeAiUsage. Tests cover factory method, token calculations, and
 * null handling.
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M5.1
 */
class DashScopeAiUsageTests {

	@Test
	void testFromTokenUsage() {
		// Test factory method with valid TokenUsage
		TokenUsage tokenUsage = new TokenUsage(20, 10, 30);
		DashScopeAiUsage usage = DashScopeAiUsage.from(tokenUsage);

		// Verify token counts are converted correctly
		assertThat(usage.getPromptTokens()).isEqualTo(10L);
		assertThat(usage.getCompletionTokens()).isEqualTo(20);
		assertThat(usage.getTotalTokens()).isEqualTo(30L);
	}

	@Test
	void testFromNullTokenUsage() {
		// Test factory method with null TokenUsage
		assertThatThrownBy(() -> DashScopeAiUsage.from(null)).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Dashscope Usage must not be null");
	}

	@Test
	void testCalculatedTotalTokens() {
		// Test total tokens calculation when totalTokens is null
		TokenUsage tokenUsage = new TokenUsage(20, 10, null);
		DashScopeAiUsage usage = DashScopeAiUsage.from(tokenUsage);

		// Verify total tokens is calculated from prompt and generation tokens
		assertThat(usage.getTotalTokens()).isEqualTo(30L);
	}

	@Test
	void testZeroTokens() {
		// Test with all token counts set to zero
		TokenUsage tokenUsage = new TokenUsage(0, 0, 0);
		DashScopeAiUsage usage = DashScopeAiUsage.from(tokenUsage);

		// Verify all token counts are zero
		assertThat(usage.getPromptTokens()).isZero();
		assertThat(usage.getCompletionTokens()).isZero();
		assertThat(usage.getTotalTokens()).isZero();
	}

	@Test
	void testToString() {
		// Test toString method
		TokenUsage tokenUsage = new TokenUsage(20, 10, 30);
		DashScopeAiUsage usage = DashScopeAiUsage.from(tokenUsage);

		// Verify toString contains token usage information
		String toString = usage.toString();
		assertThat(toString).isEqualTo(tokenUsage.toString());
	}

}
