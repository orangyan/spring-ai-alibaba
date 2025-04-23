package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import java.util.Objects;

/**
 * 语音合成消息类
 * 
 * 用于封装语音合成请求中的文本内容，支持：
 * - 文本内容设置
 * - 文本类型设置
 * - 文本格式设置
 * 
 * 用于构建语音合成请求的输入部分。
 * 
 * @author kevinlin09
 */
public class SpeechSynthesisMessage {

	/** 文本内容 */
	private String text;

	/**
	 * Constructs a new {@link SpeechSynthesisMessage} object with the given text.
	 * @param text the text to be converted to speech
	 */
	public SpeechSynthesisMessage(String text) {
		this.text = text;
	}

	/**
	 * Returns the text of this speech synthesis message.
	 * @return the text of this speech synthesis message
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text of this speech synthesis message.
	 * @param text the new text for this speech synthesis message
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {

			return true;
		}

		if (!(o instanceof SpeechSynthesisMessage that)) {

			return false;
		}

		return Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {

		return Objects.hash(text);
	}

}
