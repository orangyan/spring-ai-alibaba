package com.alibaba.cloud.ai.dashscope.audio.transcription;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;

import org.springframework.ai.model.Model;
import reactor.core.publisher.Flux;

/**
 * 音频转写模型接口
 * 
 * 定义了音频转写功能的标准接口，支持：
 * - 同步转写：直接返回转写结果
 * - 异步转写：通过任务 ID 查询转写结果
 * - 流式转写：实时获取转写结果
 * 
 * 实现类需要提供具体的音频转写实现，支持多种音频格式和转写参数配置。
 * 
 * @author kevinlin09
 * @since 1.0.0-M2
 */
public interface AudioTranscriptionModel extends Model<AudioTranscriptionPrompt, AudioTranscriptionResponse> {

	/**
	 * 同步调用音频转写
	 * 
	 * @param prompt 音频转写提示
	 * @return 音频转写响应
	 */
	@Override
	AudioTranscriptionResponse call(AudioTranscriptionPrompt prompt);

	/**
	 * 异步调用音频转写
	 * 
	 * @param prompt 音频转写提示
	 * @return 音频转写响应，包含任务 ID
	 */
	AudioTranscriptionResponse asyncCall(AudioTranscriptionPrompt prompt);

	/**
	 * 获取异步转写任务结果
	 * 
	 * @param taskId 任务 ID
	 * @return 音频转写响应
	 */
	AudioTranscriptionResponse fetch(String taskId);

	/**
	 * 流式调用音频转写
	 * 
	 * @param prompt 音频转写提示
	 * @return 音频转写响应的 Flux 流
	 */
	Flux<AudioTranscriptionResponse> stream(AudioTranscriptionPrompt prompt);

}
