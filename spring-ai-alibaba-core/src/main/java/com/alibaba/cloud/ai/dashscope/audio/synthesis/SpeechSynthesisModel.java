
package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.Model;

import reactor.core.publisher.Flux;

/**
 * 语音合成模型接口
 * 
 * 定义了语音合成功能的标准接口，支持：
 * - 文本转语音
 * - 多音色选择
 * - 语音参数调整
 * - 音频格式设置
 * 
 * 实现类需要提供具体的语音合成实现，支持多种文本格式和语音参数配置。
 * 
 * @author kevinlin09
 * @since 1.0.0-M2
 */
public interface SpeechSynthesisModel extends Model<SpeechSynthesisPrompt, SpeechSynthesisResponse> {

	/**
	 * 同步调用语音合成
	 * 
	 * @param prompt 语音合成提示
	 * @return 语音合成响应
	 */
	@Override
	SpeechSynthesisResponse call(SpeechSynthesisPrompt prompt);

	/**
	 * 流式调用语音合成
	 * 
	 * @param prompt 语音合成提示
	 * @return 语音合成响应的 Flux 流
	 */
	Flux<SpeechSynthesisResponse> stream(SpeechSynthesisPrompt prompt);

}
