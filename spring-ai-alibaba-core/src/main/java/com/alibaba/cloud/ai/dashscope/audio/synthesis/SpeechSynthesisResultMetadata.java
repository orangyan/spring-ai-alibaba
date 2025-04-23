package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.ResultMetadata;

/**
 * SpeechSynthesisResultMetadata类是一个用于表示语音合成结果的元数据类。
 * @author kevinlin09
 */
public class SpeechSynthesisResultMetadata implements ResultMetadata {

    // 表示一个空的SpeechSynthesisResultMetadata实例。
	public static final SpeechSynthesisResultMetadata NULL = SpeechSynthesisResultMetadata.create();

    /**
     * 工厂方法，用于构造一个新的SpeechSynthesisResultMetadata实例。
     *
     * @return 新的SpeechSynthesisResultMetadata实例。
     */
	static SpeechSynthesisResultMetadata create() {
		return new SpeechSynthesisResultMetadata() {
		};
	}

}
