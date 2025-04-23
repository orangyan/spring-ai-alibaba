package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import java.nio.ByteBuffer;

/**
 * 语音合成输出类
 * 
 * 封装语音合成 API 的响应输出，包含：
 * - 合成音频的 URL
 * - 音频格式信息
 * - 音频时长信息
 * 
 * 用于接收和处理语音合成 API 的响应结果。
 * 
 * @author kevinlin09
 */
public class SpeechSynthesisOutput {

	/** 合成音频的 URL */
	private final ByteBuffer audio;

	public SpeechSynthesisOutput(ByteBuffer audio) {
		this.audio = audio;
	}

	public ByteBuffer getAudio() {
		return audio;
	}

}
