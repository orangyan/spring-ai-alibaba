

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

/**
 * McpToolsInfo 类用于封装 MCP 工具相关信息。
 * 包含以下主要功能：
 * 1. 提供工具列表（tools）的获取与设置方法；
 * 2. 提供工具元信息映射（toolsMeta）的获取与设置方法。
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
