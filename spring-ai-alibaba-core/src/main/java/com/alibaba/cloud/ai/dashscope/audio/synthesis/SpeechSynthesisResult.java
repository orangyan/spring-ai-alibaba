package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.ModelResult;

/**
 * 表示语音合成操作的结果，实现了ModelResult接口。
 * 该类封装了语音合成的输出数据和元数据。
 *
 * @author kevinlin09
 */
public class SpeechSynthesisResult implements ModelResult<SpeechSynthesisOutput> {

    // 语音合成的输出对象，包含合成的音频数据。
	private final SpeechSynthesisOutput output;

    // 语音合成结果的元数据对象，包含关于合成过程的附加信息。
	private final SpeechSynthesisResultMetadata metadata;

    /**
     * 使用仅输出参数构造SpeechSynthesisResult对象。
     * 当不需要为合成结果提供元数据时使用此构造函数。
     *
     * @param output 语音合成的输出对象，包含合成的音频数据。
     */
	public SpeechSynthesisResult(SpeechSynthesisOutput output) {
		this.output = output;
		this.metadata = SpeechSynthesisResultMetadata.NULL;
	}

    /**
     * 使用输出和元数据参数构造SpeechSynthesisResult对象。
     * 当需要提供关于合成过程的详细信息时使用此构造函数。
     *
     * @param output 语音合成的输出对象，包含合成的音频数据。
     * @param metadata 语音合成结果的元数据对象，包含关于合成过程的附加信息。
     */
	public SpeechSynthesisResult(SpeechSynthesisOutput output, SpeechSynthesisResultMetadata metadata) {
		this.output = output;
		this.metadata = metadata;
	}

    /**
     * 获取语音合成的输出对象。
     *
     * @return 语音合成的输出对象，包含合成的音频数据。
     */
	@Override
	public SpeechSynthesisOutput getOutput() {
		return this.output;
	}

    /**
     * 获取语音合成结果的元数据对象。
     *
     * @return 语音合成结果的元数据对象，包含关于合成过程的附加信息。
     */
	@Override
	public SpeechSynthesisResultMetadata getMetadata() {
		return this.metadata;
	}

}
