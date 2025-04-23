
package com.alibaba.cloud.ai.dashscope.metadata.audio;

import org.springframework.ai.audio.transcription.AudioTranscriptionResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * DashScopeAudioTranscriptionResponseMetadata类继承自AudioTranscriptionResponseMetadata
 * 用于处理DashScope音频转录服务的响应元数据
 * 该类可能包含特定于DashScope服务的额外信息或处理逻辑
 */
public class DashScopeAudioTranscriptionResponseMetadata extends AudioTranscriptionResponseMetadata {

	public static final DashScopeAudioTranscriptionResponseMetadata NULL = new DashScopeAudioTranscriptionResponseMetadata() {

	};

	protected static final String AI_METADATA_STRING = "{ @type: %1$s, rateLimit: %2$s }";

	@Nullable
	private RateLimit rateLimit;

	protected DashScopeAudioTranscriptionResponseMetadata() {
		this(null);
	}

	protected DashScopeAudioTranscriptionResponseMetadata(@Nullable RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	public static DashScopeAudioTranscriptionResponseMetadata from(String result) {
		Assert.notNull(result, "OpenAI Transcription must not be null");
		return new DashScopeAudioTranscriptionResponseMetadata();
	}

	@Nullable
	public RateLimit getRateLimit() {
		RateLimit rateLimit = this.rateLimit;
		return rateLimit != null ? rateLimit : new EmptyRateLimit();
	}

	public DashScopeAudioTranscriptionResponseMetadata withRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
		return this;
	}

	@Override
	public String toString() {
		return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
	}

}
