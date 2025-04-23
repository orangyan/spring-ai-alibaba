
package com.alibaba.cloud.ai.dashscope.metadata.audio;

import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResult;

import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.MutableResponseMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @since 2023.0.1.0
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
