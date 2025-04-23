
package com.alibaba.cloud.ai.dashscope.protocol;

import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;

/**
 * DashScopeWebSocketClientOptions 类用于定义和配置与 DashScope 服务进行 WebSocket 通信时的客户端选项。
 * 该类提供了对 WebSocket 连接的基本配置，例如 URL、API 密钥以及工作空间 ID 等。
 * 此外，还提供了一个构建器（Builder）模式的静态内部类，用于更方便地创建和配置实例。
 */
public class DashScopeWebSocketClientOptions {

	private String url = DashScopeApiConstants.DEFAULT_WEBSOCKET_URL;

	private String apiKey;

	private String workSpaceId = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getWorkSpaceId() {
		return workSpaceId;
	}

	public void setWorkSpaceId(String workSpaceId) {
		this.workSpaceId = workSpaceId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		protected DashScopeWebSocketClientOptions options;

		public Builder() {
			this.options = new DashScopeWebSocketClientOptions();
		}

		public Builder(DashScopeWebSocketClientOptions options) {
			this.options = options;
		}

		public Builder withUrl(String baseUrl) {
			options.setUrl(baseUrl);
			return this;
		}

		public Builder withApiKey(String apiKey) {
			options.setApiKey(apiKey);
			return this;
		}

		public Builder withWorkSpaceId(String workSpaceId) {
			options.setWorkSpaceId(workSpaceId);
			return this;
		}

		public DashScopeWebSocketClientOptions build() {
			return options;
		}

	}

}
