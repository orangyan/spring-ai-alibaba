
package com.alibaba.cloud.ai.dashscope.api;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletion;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionChunk;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionFinishReason;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionMessage;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionMessage.ChatCompletionFunction;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionMessage.Role;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionMessage.ToolCall;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionOutput;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.ChatCompletionOutput.Choice;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi.TokenUsage;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * DashScope AI 流式函数调用辅助类
 * 用于处理流式函数调用消息，支持合并流式 ChatCompletionChunk
 * 
 * @author Ken
 */
public class DashScopeAiStreamFunctionCallingHelper {

	/** 是否启用增量输出 */
	private Boolean incrementalOutput = false;

	/**
	 * 默认构造函数
	 */
	public DashScopeAiStreamFunctionCallingHelper() {
	}

	/**
	 * 带增量输出配置的构造函数
	 * 
	 * @param incrementalOutput 是否启用增量输出
	 */
	public DashScopeAiStreamFunctionCallingHelper(Boolean incrementalOutput) {
		this.incrementalOutput = incrementalOutput;
	}

	/**
	 * 合并前一个和当前的 ChatCompletionChunk
	 * 
	 * @param previous 前一个 ChatCompletionChunk
	 * @param current 当前的 ChatCompletionChunk
	 * @return 合并后的 ChatCompletionChunk
	 */
	public ChatCompletionChunk merge(ChatCompletionChunk previous, ChatCompletionChunk current) {
		if (previous == null) {
			return current;
		}

		String id = (current.requestId() != null ? current.requestId() : previous.requestId());
		TokenUsage usage = (current.usage() != null ? current.usage() : previous.usage());

		Choice previousChoice0 = previous.output() == null ? null
				: CollectionUtils.isEmpty(previous.output().choices()) ? null : previous.output().choices().get(0);
		Choice currentChoice0 = current.output() == null ? null
				: CollectionUtils.isEmpty(current.output().choices()) ? null : current.output().choices().get(0);

		// 处理流式函数调用的兼容性（当 incremental_output 为 false 时）
		if (!incrementalOutput && isStreamingToolFunctionCall(current)) {
			if (!isStreamingToolFunctionCallFinish(current)) {
				return new ChatCompletionChunk(id, new ChatCompletionOutput(null, List.of()), usage);
			}
			else {
				return new ChatCompletionChunk(id, new ChatCompletionOutput(null, List.of(currentChoice0)), usage);
			}
		}

		Choice choice = merge(previousChoice0, currentChoice0);
		List<Choice> chunkChoices = choice == null ? List.of() : List.of(choice);
		return new ChatCompletionChunk(id, new ChatCompletionOutput(null, chunkChoices), usage);
	}

	/**
	 * 合并两个 Choice 对象
	 * 
	 * @param previous 前一个 Choice
	 * @param current 当前的 Choice
	 * @return 合并后的 Choice
	 */
	private Choice merge(Choice previous, Choice current) {
		if (previous == null) {
			return current;
		}

		ChatCompletionFinishReason finishReason = (current.finishReason() != null ? current.finishReason()
				: previous.finishReason());

		ChatCompletionMessage message = merge(previous.message(), current.message());
		return new Choice(finishReason, message);
	}

	/**
	 * 合并两个 ChatCompletionMessage 对象
	 * 
	 * @param previous 前一个消息
	 * @param current 当前消息
	 * @return 合并后的消息
	 */
	private ChatCompletionMessage merge(ChatCompletionMessage previous, ChatCompletionMessage current) {

		String content = (current.content() != null ? current.content()
				: (previous.content() != null) ? previous.content() : "");
		Role role = (current.role() != null ? current.role() : previous.role());
		role = (role != null ? role : Role.ASSISTANT); // 默认为 ASSISTANT（如果为 null）
		String name = (StringUtils.hasText(current.name()) ? current.name() : previous.name());
		String toolCallId = (StringUtils.hasText(current.toolCallId()) ? current.toolCallId() : previous.toolCallId());
		String reasoningContent = (current.reasoningContent() != null ? current.reasoningContent()
				: previous.reasoningContent());

		List<ToolCall> toolCalls = new ArrayList<>();
		ToolCall lastPreviousTooCall = null;
		if (previous.toolCalls() != null) {
			lastPreviousTooCall = previous.toolCalls().get(previous.toolCalls().size() - 1);
			if (previous.toolCalls().size() > 1) {
				toolCalls.addAll(previous.toolCalls().subList(0, previous.toolCalls().size() - 1));
			}
		}
		if (!CollectionUtils.isEmpty(current.toolCalls())) {
			if (current.toolCalls().size() > 1) {
				throw new IllegalStateException("Currently only one tool call is supported per message!");
			}
			var currentToolCall = current.toolCalls().iterator().next();
			if (StringUtils.hasText(currentToolCall.id())) {
				if (lastPreviousTooCall != null) {
					toolCalls.add(lastPreviousTooCall);
				}
				toolCalls.add(currentToolCall);
			}
			else {
				toolCalls.add(merge(lastPreviousTooCall, currentToolCall));
			}
		}
		else {
			if (lastPreviousTooCall != null) {
				toolCalls.add(lastPreviousTooCall);
			}
		}
		return new ChatCompletionMessage(content, role, name, toolCallId, toolCalls, reasoningContent);
	}

	/**
	 * 合并两个 ToolCall 对象
	 * 
	 * @param previous 前一个工具调用
	 * @param current 当前工具调用
	 * @return 合并后的工具调用
	 */
	private ToolCall merge(ToolCall previous, ToolCall current) {
		if (previous == null) {
			return current;
		}
		String id = (StringUtils.hasText(current.id()) ? current.id() : previous.id());
		String type = (StringUtils.hasText(current.type()) ? current.type() : previous.type());
		ChatCompletionFunction function = merge(previous.function(), current.function());
		return new ToolCall(id, type, function);
	}

	/**
	 * 合并两个 ChatCompletionFunction 对象
	 * 
	 * @param previous 前一个函数
	 * @param current 当前函数
	 * @return 合并后的函数
	 */
	private ChatCompletionFunction merge(ChatCompletionFunction previous, ChatCompletionFunction current) {
		if (previous == null) {
			return current;
		}
		String name = (StringUtils.hasText(current.name()) ? current.name() : previous.name());
		StringBuilder arguments = new StringBuilder();
		if (previous.arguments() != null) {
			arguments.append(previous.arguments());
		}
		if (current.arguments() != null) {
			arguments.append(current.arguments());
		}
		return new ChatCompletionFunction(name, arguments.toString());
	}

	/**
	 * 检查 ChatCompletionChunk 是否为流式工具函数调用
	 * 
	 * @param chatCompletion 要检查的 ChatCompletionChunk
	 * @return 如果是流式工具函数调用则返回 true
	 */
	public boolean isStreamingToolFunctionCall(ChatCompletionChunk chatCompletion) {

		if (chatCompletion == null || chatCompletion.output() == null
				|| CollectionUtils.isEmpty(chatCompletion.output().choices())) {
			return false;
		}

		var choice = chatCompletion.output().choices().get(0);
		if (choice == null || choice.message() == null) {
			return false;
		}
		return !CollectionUtils.isEmpty(choice.message().toolCalls());
	}

	/**
	 * 检查 ChatCompletionChunk 是否为流式工具函数调用的最后一个
	 * 
	 * @param chatCompletion 要检查的 ChatCompletionChunk
	 * @return 如果是流式工具函数调用的最后一个则返回 true
	 */
	public boolean isStreamingToolFunctionCallFinish(ChatCompletionChunk chatCompletion) {

		if (chatCompletion == null || CollectionUtils.isEmpty(chatCompletion.output().choices())) {
			return false;
		}

		var choice = chatCompletion.output().choices().get(0);
		if (choice == null || choice.message() == null) {
			return false;
		}
		return choice.finishReason() == ChatCompletionFinishReason.TOOL_CALLS;
	}

	/**
	 * 将 ChatCompletionChunk 转换为 ChatCompletion
	 * Usage 设置为 null
	 * 
	 * @param chunk 要转换的 ChatCompletionChunk
	 * @return 转换后的 ChatCompletion
	 */
	public ChatCompletion chunkToChatCompletion(ChatCompletionChunk chunk) {
		return new ChatCompletion(chunk.requestId(), chunk.output(), chunk.usage());
	}

}
