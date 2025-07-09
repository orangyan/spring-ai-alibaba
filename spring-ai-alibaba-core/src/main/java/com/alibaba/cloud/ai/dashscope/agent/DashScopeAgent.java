
package com.alibaba.cloud.ai.dashscope.agent;

import com.alibaba.cloud.ai.agent.Agent;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi.DashScopeAgentRequest;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi.DashScopeAgentResponse;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi.DashScopeAgentRequest.DashScopeAgentRequestInput.DashScopeAgentRequestMessage;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi.DashScopeAgentRequest.DashScopeAgentRequestParameters.DashScopeAgentRequestRagOptions;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DashScope低级别代理实现类
 * 提供与DashScope API的交互功能，支持同步和流式调用
 * 
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */
public final class DashScopeAgent extends Agent {

	// 日志记录器
	private static final Logger logger = LoggerFactory.getLogger(DashScopeAgent.class);

	// DashScope代理配置选项
	private final DashScopeAgentOptions options;

	// DashScope代理API客户端
	private final DashScopeAgentApi dashScopeAgentApi;

	/**
	 * 构造函数，使用默认配置
	 * @param dashScopeAgentApi DashScope代理API客户端
	 */
	public DashScopeAgent(DashScopeAgentApi dashScopeAgentApi) {
		this.dashScopeAgentApi = dashScopeAgentApi;
		this.options = DashScopeAgentOptions.builder()
			.withSessionId(null)
			.withMemoryId(null)
			.withIncrementalOutput(false)
			.withHasThoughts(false)
			.withImages(null)
			.withBizParams(null)
			.build();
	}

	/**
	 * 构造函数，使用指定的配置选项
	 * @param dashScopeAgentApi DashScope代理API客户端
	 * @param options DashScope代理配置选项
	 */
	public DashScopeAgent(DashScopeAgentApi dashScopeAgentApi, DashScopeAgentOptions options) {
		this.dashScopeAgentApi = dashScopeAgentApi;
		this.options = options;
	}

	/**
	 * 同步调用DashScope代理
	 * @param prompt 用户提示词
	 * @return 聊天响应
	 */
	@Override
	public ChatResponse call(Prompt prompt) {
		// 构建请求
		DashScopeAgentRequest request = toRequest(prompt, false);

		// 调用API
		ResponseEntity<DashScopeAgentResponse> response = this.dashScopeAgentApi.call(request);

		// 处理响应
		if (response == null || response.getBody() == null) {
			logger.warn("app call error: request: {}", request);
			return null;
		}

		return toChatResponse(response.getBody());
	}

	/**
	 * 流式调用DashScope代理
	 * @param prompt 用户提示词
	 * @return 流式聊天响应
	 */
	@Override
	public Flux<ChatResponse> stream(Prompt prompt) {
		// 构建请求
		DashScopeAgentRequest request = toRequest(prompt, true);

		// 调用API并转换为流式响应
		Flux<DashScopeAgentResponse> response = this.dashScopeAgentApi.stream(request);

		return Flux.from(response)
			.flatMap(result -> Flux.just(toChatResponse(result)))
			.publishOn(Schedulers.parallel());
	}

	/**
	 * 将提示词转换为DashScope请求
	 * @param prompt 用户提示词
	 * @param stream 是否使用流式调用
	 * @return DashScope请求对象
	 */
	private DashScopeAgentRequest toRequest(Prompt prompt, Boolean stream) {
		if (prompt == null) {
			throw new IllegalArgumentException("option is null");
		}

		// 合并运行时选项
		DashScopeAgentOptions runtimeOptions = mergeOptions(prompt.getOptions());
		String appId = runtimeOptions.getAppId();

		if (appId == null || appId.isEmpty()) {
			throw new IllegalArgumentException("appId must be set");
		}

		// 处理消息
		String requestPrompt = null;
		List<DashScopeAgentRequestMessage> requestMessages = List.of();

		List<Message> messages = prompt.getInstructions();
		boolean onlyOneUserMessage = messages.size() == 1 && messages.get(0).getMessageType() == MessageType.USER;
		if (onlyOneUserMessage) {
			requestPrompt = messages.get(0).getText();
		}
		else {
			requestMessages = messages.stream()
				.map(msg -> new DashScopeAgentRequestMessage(msg.getMessageType().getValue(), msg.getText()))
				.toList();
		}

		// 构建RAG选项
		DashScopeAgentRagOptions ragOptions = runtimeOptions.getRagOptions();
		return new DashScopeAgentRequest(appId,
				new DashScopeAgentRequest.DashScopeAgentRequestInput(requestPrompt, requestMessages,
						runtimeOptions.getSessionId(), runtimeOptions.getMemoryId(), runtimeOptions.getImages(),
						runtimeOptions.getBizParams()),
				new DashScopeAgentRequest.DashScopeAgentRequestParameters(runtimeOptions.getFlowStreamMode(),
						runtimeOptions.getHasThoughts(), stream && runtimeOptions.getIncrementalOutput(),
						ragOptions == null ? null
								: new DashScopeAgentRequestRagOptions(ragOptions.getPipelineIds(),
										ragOptions.getFileIds(), ragOptions.getMetadataFilter(), ragOptions.getTags(),
										ragOptions.getStructuredFilter(), ragOptions.getSessionFileIds())));
	}

	/**
	 * 将DashScope响应转换为聊天响应
	 * @param response DashScope响应
	 * @return 聊天响应
	 */
	private ChatResponse toChatResponse(DashScopeAgentResponse response) {
		// 获取输出和用量信息
		DashScopeAgentResponse.DashScopeAgentResponseOutput output = response.output();
		DashScopeAgentResponse.DashScopeAgentResponseUsage usage = response.usage();
		if (output == null) {
			throw new RuntimeException("output is null");
		}

		// 处理文本输出
		String text = output.text();
		if (text == null) {
			text = "";
		}

		// 构建元数据
		Map<String, Object> metadata = new HashMap<>();
		metadata.put(DashScopeApiConstants.REQUEST_ID, response.requestId());
		metadata.put(DashScopeApiConstants.USAGE, usage);
		metadata.put(DashScopeApiConstants.OUTPUT, output);

		// 构建助手消息和生成信息
		var assistantMessage = new AssistantMessage(text, metadata);
		var generationMetadata = ChatGenerationMetadata.builder().finishReason(output.finishReason()).build();
		Generation generation = new Generation(assistantMessage, generationMetadata);

		return new ChatResponse(List.of(generation));
	}

	/**
	 * 合并聊天选项和代理选项
	 * @param chatOptions 聊天选项
	 * @return 合并后的代理选项
	 */
	private DashScopeAgentOptions mergeOptions(ChatOptions chatOptions) {
		DashScopeAgentOptions agentOptions = ModelOptionsUtils.copyToTarget(chatOptions, ChatOptions.class,
				DashScopeAgentOptions.class);
		return ModelOptionsUtils.merge(agentOptions, this.options, DashScopeAgentOptions.class);
	}

}
