
package com.alibaba.cloud.ai.dashscope.metadata.audio;

import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResult;

import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.MutableResponseMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 继承自 MutableResponseMetadata 的类，用于处理 DashScope 音频合成响应的元数据。
 * 该类提供了获取请求 ID、日志记录和跟踪信息的方法，有助于监控和调试音频合成请求。
 */

public class DashScopeAudioSpeechResponseMetadata extends MutableResponseMetadata {

	public static final DashScopeAudioSpeechResponseMetadata NULL = new DashScopeAudioSpeechResponseMetadata() {
	};

	protected static final String AI_METADATA_STRING = "{ @type: %1$s, requestsLimit: %2$s }";

	@Nullable
	private RateLimit rateLimit;

	public DashScopeAudioSpeechResponseMetadata() {
		this(null);
	}

	public DashScopeAudioSpeechResponseMetadata(@Nullable RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	public static DashScopeAudioSpeechResponseMetadata from(SpeechSynthesisResult result) {
		Assert.notNull(result, "DashScope speech must not be null");
		DashScopeAudioSpeechResponseMetadata speechResponseMetadata = new DashScopeAudioSpeechResponseMetadata();
		return speechResponseMetadata;
	}

	public static DashScopeAudioSpeechResponseMetadata from(String result) {
		Assert.notNull(result, "DashScope speech must not be null");
		DashScopeAudioSpeechResponseMetadata speechResponseMetadata = new DashScopeAudioSpeechResponseMetadata();
		return speechResponseMetadata;
	}

	@Nullable
	public RateLimit getRateLimit() {
		RateLimit rateLimit = this.rateLimit;
		return rateLimit != null ? rateLimit : new EmptyRateLimit();
	}

	public DashScopeAudioSpeechResponseMetadata withRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
		return this;
	}

	@Override
	public String toString() {
		return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
	}

}
