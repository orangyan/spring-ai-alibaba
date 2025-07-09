package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.ModelResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 表示语音合成的响应，包含合成结果和元数据。
 * 该类的主要职责是封装语音合成结果及其相关元数据，并提供获取这些信息的方法。
 *
 * @author kevinlin09
 */
public class SpeechSynthesisResponse implements ModelResponse<SpeechSynthesisResult> {

	private final SpeechSynthesisResult result; // 语音合成结果

	private final SpeechSynthesisResponseMetadata metadata; // 响应的元数据

	/**
	 * 构造一个包含语音合成结果的 SpeechSynthesisResponse 对象。
	 * 该构造函数默认将元数据设置为 NULL。
	 *
	 * @param result 语音合成结果
	 */
	public SpeechSynthesisResponse(SpeechSynthesisResult result) {
		this(result, SpeechSynthesisResponseMetadata.NULL);
	}

	/**
	 * 构造一个包含语音合成结果和元数据的 SpeechSynthesisResponse 对象。
	 *
	 * @param result  语音合成结果
	 * @param metadata 响应的元数据
	 */
	public SpeechSynthesisResponse(SpeechSynthesisResult result, SpeechSynthesisResponseMetadata metadata) {
		this.result = result;
		this.metadata = metadata;
	}

	/**
	 * 获取语音合成结果。
	 *
	 * @return 语音合成结果
	 */
	@Override
	public SpeechSynthesisResult getResult() {
		return this.result;
	}

	/**
	 * 获取语音合成结果的列表。
	 * 由于一个响应只包含一个结果，因此该方法返回一个包含单个元素的列表。
	 *
	 * @return 包含语音合成结果的列表
	 */
	@Override
	public List<SpeechSynthesisResult> getResults() {
		return Collections.singletonList(this.result);
	}

	/**
	 * 获取语音合成响应的元数据。
	 *
	 * @return 语音合成响应的元数据
	 */
	@Override
	public SpeechSynthesisResponseMetadata getMetadata() {
		return this.metadata;
	}

	/**
	 * 比较当前对象是否与另一个对象相等。
	 * 如果两个 SpeechSynthesisResponse 对象的合成结果和元数据相等，则认为它们相等。
	 *
	 * @param o 要比较的另一个对象
	 * @return 如果对象相等则返回 true，否则返回 false
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SpeechSynthesisResponse that)) {
			return false;
		}
		return Objects.equals(this.result, that.result) && Objects.equals(this.metadata, that.metadata);
	}

	/**
	 * 计算当前对象的哈希值。
	 * 哈希值基于合成结果和元数据计算得出。
	 *
	 * @return 当前对象的哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.metadata);
	}

}
