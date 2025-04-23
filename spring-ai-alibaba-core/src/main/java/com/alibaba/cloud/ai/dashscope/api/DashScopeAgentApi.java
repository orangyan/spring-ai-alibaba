
package com.alibaba.cloud.ai.dashscope.api;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentFlowStreamMode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants.DEFAULT_BASE_URL;

import java.util.List;

/**
 * DashScope 智能体 API 接口，提供与智能体服务交互的功能
 * 支持同步调用和流式调用两种方式
 * 
 * @author linkesheng.lks
 * @since 1.0.0-M2
 */
public class DashScopeAgentApi {

	/** REST 客户端，用于同步 API 调用 */
	private final RestClient restClient;

	/** Web 客户端，用于流式 API 调用 */
	private final WebClient webClient;

	/**
	 * 构造函数，使用默认配置初始化 API 客户端
	 * 
	 * @param apiKey API 密钥
	 */
	public DashScopeAgentApi(String apiKey) {
		this(DEFAULT_BASE_URL, apiKey, RestClient.builder(), WebClient.builder(),
				RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
	}

	/**
	 * 构造函数，使用默认配置和工作空间 ID 初始化 API 客户端
	 * 
	 * @param apiKey API 密钥
	 * @param workSpaceId 工作空间 ID
	 */
	public DashScopeAgentApi(String apiKey, String workSpaceId) {
		this(DEFAULT_BASE_URL, apiKey, workSpaceId, RestClient.builder(), WebClient.builder(),
				RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
	}

	/**
	 * 构造函数，使用自定义基础 URL 和工作空间 ID 初始化 API 客户端
	 * 
	 * @param baseUrl API 基础 URL
	 * @param apiKey API 密钥
	 * @param workSpaceId 工作空间 ID
	 */
	public DashScopeAgentApi(String baseUrl, String apiKey, String workSpaceId) {
		this(baseUrl, apiKey, workSpaceId, RestClient.builder(), WebClient.builder(),
				RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
	}

	/**
	 * 构造函数，使用自定义配置初始化 API 客户端
	 * 
	 * @param baseUrl API 基础 URL
	 * @param apiKey API 密钥
	 * @param restClientBuilder REST 客户端构建器
	 * @param webClientBuilder Web 客户端构建器
	 * @param responseErrorHandler 响应错误处理器
	 */
	public DashScopeAgentApi(String baseUrl, String apiKey, RestClient.Builder restClientBuilder,
			WebClient.Builder webClientBuilder, ResponseErrorHandler responseErrorHandler) {
		this.restClient = restClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey))
			.defaultStatusHandler(responseErrorHandler)
			.build();

		this.webClient = webClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey, null, true))
			.build();
	}

	/**
	 * 构造函数，使用自定义配置和工作空间 ID 初始化 API 客户端
	 * 
	 * @param baseUrl API 基础 URL
	 * @param apiKey API 密钥
	 * @param workSpaceId 工作空间 ID
	 * @param restClientBuilder REST 客户端构建器
	 * @param webClientBuilder Web 客户端构建器
	 * @param responseErrorHandler 响应错误处理器
	 */
	public DashScopeAgentApi(String baseUrl, String apiKey, String workSpaceId, RestClient.Builder restClientBuilder,
			WebClient.Builder webClientBuilder, ResponseErrorHandler responseErrorHandler) {
		this.restClient = restClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey, workSpaceId))
			.defaultStatusHandler(responseErrorHandler)
			.build();

		this.webClient = webClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey, workSpaceId, true))
			.build();
	}

	/**
	 * 同步调用智能体服务
	 * 
	 * @param request 智能体请求参数
	 * @return 智能体响应实体
	 */
	public ResponseEntity<DashScopeAgentResponse> call(DashScopeAgentRequest request) {
		String uri = "/api/v1/apps/" + request.appId() + "/completion";
		return restClient.post().uri(uri).body(request).retrieve().toEntity(DashScopeAgentResponse.class);
	}

	/**
	 * 流式调用智能体服务
	 * 
	 * @param request 智能体请求参数
	 * @return 智能体响应流
	 */
	public Flux<DashScopeAgentResponse> stream(DashScopeAgentRequest request) {
		String uri = "/api/v1/apps/" + request.appId() + "/completion";
		return webClient.post()
			.uri(uri)
			.body(Mono.just(request), DashScopeAgentResponse.class)
			.retrieve()
			.bodyToFlux(DashScopeAgentResponse.class)
			.handle((data, sink) -> {
				sink.next(data);
			});
	}

	/**
	 * 智能体请求参数
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record DashScopeAgentRequest(
			@JsonProperty("app_id") String appId,
			@JsonProperty("input") DashScopeAgentRequestInput input,
			@JsonProperty("parameters") DashScopeAgentRequestParameters parameters) {
		
		/**
		 * 智能体请求输入参数
		 */
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public record DashScopeAgentRequestInput(
				@JsonProperty("prompt") String prompt,
				@JsonProperty("messages") List<DashScopeAgentRequestMessage> messages,
				@JsonProperty("session_id") String sessionId,
				@JsonProperty("memory_id") String memoryId,
				@JsonProperty("image_list") List<String> images,
				@JsonProperty("biz_params") JsonNode bizParams) {
			
			/**
			 * 智能体请求消息
			 */
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public record DashScopeAgentRequestMessage(
					@JsonProperty("role") String role,
					@JsonProperty("content") String content) {
			}
		}

		/**
		 * 智能体请求配置参数
		 */
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public record DashScopeAgentRequestParameters(
				@JsonProperty("flow_stream_mode") DashScopeAgentFlowStreamMode flowStreamMode,
				@JsonProperty("has_thoughts") Boolean hasThoughts,
				@JsonProperty("incremental_output") Boolean incrementalOutput,
				@JsonProperty("rag_options") DashScopeAgentRequestRagOptions ragOptions
		) {
			
			/**
			 * 智能体 RAG 选项参数
			 */
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public record DashScopeAgentRequestRagOptions(
					@JsonProperty("pipeline_ids") List<String> pipelineIds,
					@JsonProperty("file_ids") List<String> fileIds,
					@JsonProperty("metadata_filter") JsonNode metadataFilter,
					@JsonProperty("tags") List<String> tags,
					@JsonProperty("structured_filter") JsonNode structuredFilter,
					@JsonProperty("session_file_ids") List<String> sessionFileIds) {
			}
		}
	}

	/**
	 * 智能体响应
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record DashScopeAgentResponse(
			@JsonProperty("status_code") Integer statusCode,
			@JsonProperty("request_id") String requestId,
			@JsonProperty("code") String code,
			@JsonProperty("message") String message,
			@JsonProperty("output") DashScopeAgentResponseOutput output,
			@JsonProperty("usage") DashScopeAgentResponseUsage usage) {
		
		/**
		 * 智能体响应输出
		 */
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public record DashScopeAgentResponseOutput(
				@JsonProperty("text") String text,
				@JsonProperty("finish_reason") String finishReason,
				@JsonProperty("session_id") String sessionId,
				@JsonProperty("thoughts") List<DashScopeAgentResponseOutputThoughts> thoughts,
				@JsonProperty("doc_references") List<DashScopeAgentResponseOutputDocReference> docReferences) {
			
			/**
			 * 智能体响应思考过程
			 */
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public record DashScopeAgentResponseOutputThoughts(
					@JsonProperty("thought") String thought,
					@JsonProperty("action_type") String actionType,
					@JsonProperty("action_name") String actionName,
					@JsonProperty("action") String action,
					@JsonProperty("action_input_stream") String actionInputStream,
					@JsonProperty("action_input") String actionInput,
					@JsonProperty("response") String response,
					@JsonProperty("observation") String observation,
					@JsonProperty("reasoning_content") String reasoningContent) {
			}

			/**
			 * 智能体响应文档引用
			 */
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public record DashScopeAgentResponseOutputDocReference(
					@JsonProperty("index_id") String indexId,
					@JsonProperty("title") String title,
					@JsonProperty("doc_id") String docId,
					@JsonProperty("doc_name") String docName,
					@JsonProperty("text") String text,
					@JsonProperty("images") List<String> images,
					@JsonProperty("page_number") List<Integer> pageNumber) {
			}
		}

		/**
		 * 智能体响应使用情况
		 */
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public record DashScopeAgentResponseUsage(
				@JsonProperty("models") List<DashScopeAgentResponseUsageModels> models) {
			
			/**
			 * 智能体响应模型使用情况
			 */
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public record DashScopeAgentResponseUsageModels(
					@JsonProperty("model_id") String modelId,
					@JsonProperty("input_tokens") Integer inputTokens,
					@JsonProperty("output_tokens") Integer outputTokens) {
			}
		}
	}
}
