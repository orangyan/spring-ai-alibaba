package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.ModelRequest;

import java.util.Collections;
import java.util.List;

/**
 * 语音合成提示类
 * 
 * 封装语音合成请求的输入信息，包含：
 * - 要合成的文本内容
 * - 语音合成选项配置
 * 
 * 用于构建语音合成 API 的请求参数。
 * 
 * @author kevinlin09
 * @since 1.0.0-M2
 */
public class SpeechSynthesisPrompt implements ModelRequest<List<SpeechSynthesisMessage>> {

	/** 要合成的文本内容 */
	private final List<SpeechSynthesisMessage> messages;

	/** 语音合成选项 */
	private final SpeechSynthesisOptions options;

	public SpeechSynthesisPrompt(String contents) {
		this(new SpeechSynthesisMessage(contents));
	}

	public SpeechSynthesisPrompt(SpeechSynthesisMessage message) {
		this(Collections.singletonList(message));
	}

	public SpeechSynthesisPrompt(List<SpeechSynthesisMessage> messages) {
		this(messages, null);
	}

	public SpeechSynthesisPrompt(String contents, SpeechSynthesisOptions options) {
		this(new SpeechSynthesisMessage(contents), options);
	}

	public SpeechSynthesisPrompt(SpeechSynthesisMessage message, SpeechSynthesisOptions options) {
		this(Collections.singletonList(message), options);
	}

	public SpeechSynthesisPrompt(List<SpeechSynthesisMessage> messages, SpeechSynthesisOptions options) {
		this.messages = messages;
		this.options = options;
	}

	@Override
	public SpeechSynthesisOptions getOptions() {
		return this.options;
	}

	@Override
	public List<SpeechSynthesisMessage> getInstructions() {
		return this.messages;
	}

}
