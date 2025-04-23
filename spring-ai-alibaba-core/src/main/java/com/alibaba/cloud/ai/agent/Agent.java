
package com.alibaba.cloud.ai.agent;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

/**
 * 代理类，用于管理和执行AI代理任务
 * 提供代理的创建、配置和执行功能
 * 
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */
public abstract class Agent {

	/**
	 * 使用聊天模型进行调用
	 * @param prompt 用户提示词
	 * @return 聊天响应
	 */
	public abstract ChatResponse call(Prompt prompt);

	/**
	 * 使用聊天模型进行流式调用
	 * @param prompt 用户提示词
	 * @return 流式聊天响应
	 */
	public abstract Flux<ChatResponse> stream(Prompt prompt);

}
