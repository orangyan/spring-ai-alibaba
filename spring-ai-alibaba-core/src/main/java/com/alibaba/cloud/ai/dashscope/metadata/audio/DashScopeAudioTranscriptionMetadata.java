
package com.alibaba.cloud.ai.dashscope.metadata.audio;

import org.springframework.ai.model.ResultMetadata;

/**
 * 接口DashScopeAudioTranscriptionMetadata继承自ResultMetadata，用于定义音频转录的元数据标准。
 * 这个接口主要在音频转录功能中使用，以提供转录结果的额外信息和上下文。
 */

public interface DashScopeAudioTranscriptionMetadata extends ResultMetadata {

	/**
	 * A constant instance of {@link DashScopeAudioTranscriptionMetadata} that represents
	 * a null or empty metadata.
	 */
	DashScopeAudioTranscriptionMetadata NULL = DashScopeAudioTranscriptionMetadata.create();

	/**
	 * Factory method for creating a new instance of
	 * {@link DashScopeAudioTranscriptionMetadata}.
	 * @return a new instance of {@link DashScopeAudioTranscriptionMetadata}
	 */
	static DashScopeAudioTranscriptionMetadata create() {
		return new DashScopeAudioTranscriptionMetadata() {
		};
	}

}
