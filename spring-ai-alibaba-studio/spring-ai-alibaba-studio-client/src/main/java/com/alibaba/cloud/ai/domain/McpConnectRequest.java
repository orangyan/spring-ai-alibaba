

package com.alibaba.cloud.ai.domain;

import com.alibaba.cloud.ai.common.McpTransportType;
import com.fasterxml.jackson.databind.JsonNode;

public class McpConnectRequest {

	private McpTransportType transportType;

	private JsonNode params;

	public JsonNode getParams() {
		return params;
	}

	public void setParams(JsonNode params) {
		this.params = params;
	}

	public McpTransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(McpTransportType transportType) {
		this.transportType = transportType;
	}

	@Override
	public String toString() {
		return transportType + ":" + params;
	}

}
