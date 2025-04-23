
package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.model.ModelOptions;
import org.springframework.lang.Nullable;

/**
 * 语音合成选项接口
 * 
 * 定义了语音合成功能的配置选项，用于：
 * - 设置语音合成模型
 * - 配置语音合成参数
 * - 自定义语音合成行为
 * 
 * 实现类需要提供具体的语音合成选项实现。
 * 
 * @author kevinlin09
 * @since 1.0.0-M2
 */
public interface SpeechSynthesisOptions extends ModelOptions {

	/**
	 * 获取语音合成模型名称
	 * 
	 * @return 语音合成模型名称，可能为 null
	 */
	@Nullable
	String getModel();

}
