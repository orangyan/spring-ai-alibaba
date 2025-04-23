

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

/**
 * @author Sunrisea
 */
public class McpToolsInfo {

	private List<McpSchema.Tool> tools;

	private Map<String, ToolMetaInfo> toolsMeta;

	public List<McpSchema.Tool> getTools() {
		return tools;
	}

	public void setTools(List<McpSchema.Tool> tools) {
		this.tools = tools;
	}

	public Map<String, ToolMetaInfo> getToolsMeta() {
		return toolsMeta;
	}

	public void setToolsMeta(Map<String, ToolMetaInfo> toolsMeta) {
		this.toolsMeta = toolsMeta;
	}

}
