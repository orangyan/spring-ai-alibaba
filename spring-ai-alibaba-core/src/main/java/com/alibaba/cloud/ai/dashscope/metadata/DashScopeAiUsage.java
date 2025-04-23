
package com.alibaba.cloud.ai.dashscope.metadata;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.TokenUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.util.Assert;

/**
 * 实现Usage接口的类，用于记录和管理DashScope AI的使用情况
 * 该类提供了记录AI使用情况的方法和逻辑，以便跟踪和分析AI服务的使用情况
 */
public class DashScopeAiUsage implements Usage {

	private final TokenUsage usage;

	protected DashScopeAiUsage(TokenUsage usage) {
		Assert.notNull(usage, "Dashscope Usage must not be null");
		this.usage = usage;
	}

	public static DashScopeAiUsage from(TokenUsage usage) {
		return new DashScopeAiUsage(usage);
	}

	protected TokenUsage getUsage() {
		return this.usage;
	}

	@Override
	public Integer getPromptTokens() {
		return getUsage().inputTokens();
	}

	@Override
	public Integer getCompletionTokens() {
		return getUsage().outputTokens();
	}

	@Override
	public Integer getTotalTokens() {
		Integer totalTokens = getUsage().totalTokens();
		if (totalTokens != null) {
			return totalTokens;
		}
		else {
			return getPromptTokens() + getCompletionTokens();
		}
	}

	@Override
	public Object getNativeUsage() {
		return null;
	}

	@Override
	public String toString() {
		return getUsage().toString();
	}

}
