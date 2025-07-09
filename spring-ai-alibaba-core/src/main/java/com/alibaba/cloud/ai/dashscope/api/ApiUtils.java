
package com.alibaba.cloud.ai.dashscope.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * API 工具类，提供处理 API 请求头部信息的工具方法
 * 
 * @author nuocheng.lxm
 * @since 1.0.0-M2
 */
public class ApiUtils {

	/** 用户代理标识 */
	private static final String USER_AGENT = userAgent();

	/**
	 * 获取 JSON 内容类型的请求头
	 * 
	 * @param apiKey API 密钥
	 * @return 请求头配置消费者
	 */
	public static Consumer<HttpHeaders> getJsonContentHeaders(String apiKey) {
		return getJsonContentHeaders(apiKey, null);
	}

	/**
	 * 获取 JSON 内容类型的请求头
	 * 
	 * @param apiKey API 密钥
	 * @param workspaceId 工作空间 ID
	 * @return 请求头配置消费者
	 */
	public static Consumer<HttpHeaders> getJsonContentHeaders(String apiKey, String workspaceId) {
		return getJsonContentHeaders(apiKey, workspaceId, false);
	}

	/**
	 * 获取 JSON 内容类型的请求头
	 * 
	 * @param apiKey API 密钥
	 * @param workspaceId 工作空间 ID
	 * @param stream 是否启用流式传输
	 * @return 请求头配置消费者
	 */
	public static Consumer<HttpHeaders> getJsonContentHeaders(String apiKey, String workspaceId, boolean stream) {
		return (headers) -> {
			headers.setBearerAuth(apiKey);
			headers.set(DashScopeApiConstants.HEADER_OPENAPI_SOURCE, DashScopeApiConstants.SOURCE_FLAG);

			headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
			if (workspaceId != null) {
				headers.set(DashScopeApiConstants.HEADER_WORK_SPACE_ID, workspaceId);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (stream) {
				headers.set("X-DashScope-SSE", "enable");
			}
		};
	}

	/**
	 * 获取 Map 内容类型的请求头
	 * 
	 * @param apiKey API 密钥
	 * @param isSecurityCheck 是否启用安全检查
	 * @param workspace 工作空间
	 * @param customHeaders 自定义请求头
	 * @return 请求头 Map
	 */
	public static Map<String, String> getMapContentHeaders(String apiKey, boolean isSecurityCheck, String workspace,
			Map<String, String> customHeaders) {
		Map<String, String> headers = new HashMap<>();
		headers.put(HttpHeaders.AUTHORIZATION, "bearer " + apiKey);
		headers.put(HttpHeaders.USER_AGENT, USER_AGENT);
		if (workspace != null && !workspace.isEmpty()) {
			headers.put(DashScopeApiConstants.HEADER_WORK_SPACE_ID, workspace);
		}
		if (isSecurityCheck) {
			headers.put("X-DashScope-DataInspection", "enable");
		}
		if (customHeaders != null && !customHeaders.isEmpty()) {
			headers.putAll(customHeaders);
		}
		return headers;
	}

	/**
	 * 获取音频转录的请求头
	 * 
	 * @param apiKey API 密钥
	 * @param workspace 工作空间
	 * @param isAsyncTask 是否异步任务
	 * @param isSecurityCheck 是否启用安全检查
	 * @param isSSE 是否启用 SSE
	 * @return 请求头配置消费者
	 */
	public static Consumer<HttpHeaders> getAudioTranscriptionHeaders(String apiKey, String workspace,
			Boolean isAsyncTask, Boolean isSecurityCheck, Boolean isSSE) {
		return (headers) -> {
			headers.setBearerAuth(apiKey);
			headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
			if (isSecurityCheck) {
				headers.set("X-DashScope-DataInspection", "enable");
			}

			if (workspace != null && !workspace.isEmpty()) {
				headers.set(DashScopeApiConstants.HEADER_WORK_SPACE_ID, workspace);
			}

			if (isAsyncTask) {
				headers.set("X-DashScope-Async", "enable");
			}

			headers.setContentType(MediaType.APPLICATION_JSON);
			if (isSSE) {
				headers.set(HttpHeaders.CACHE_CONTROL, "no-cache");
				headers.setAccept(List.of(MediaType.TEXT_EVENT_STREAM));
				headers.set("X-Accel-Buffering", "no");
				headers.set("X-DashScope-SSE", "enable");
			}
			else {
				headers.setAccept(List.of(MediaType.parseMediaType("application/json; charset=utf-8")));
			}
		};
	}

	/**
	 * 获取文件上传的请求头
	 * 
	 * @param input 输入参数
	 * @return 请求头配置消费者
	 */
	public static Consumer<HttpHeaders> getFileUploadHeaders(Map<String, String> input) {
		return (headers) -> {
			String contentType = input.remove(HttpHeaders.CONTENT_TYPE);
			for (Map.Entry<String, String> entry : input.entrySet()) {
				headers.set(entry.getKey(), entry.getValue());
			}
			headers.setContentType(MediaType.parseMediaType((contentType)));
		};
	}

	/**
	 * 生成用户代理字符串
	 * 
	 * @return 用户代理字符串
	 */
	private static String userAgent() {
		return String.format("%s/%s; java/%s; platform/%s; processor/%s", DashScopeApiConstants.SDK_FLAG, "1.0.0",
				System.getProperty("java.version"), System.getProperty("os.name"), System.getProperty("os.arch"));
	}

}
