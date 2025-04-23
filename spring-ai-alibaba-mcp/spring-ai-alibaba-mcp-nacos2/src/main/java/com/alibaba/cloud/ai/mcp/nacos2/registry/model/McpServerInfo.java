

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

import java.util.Map;

/**
 * McpServerInfo 类用于描述 MCP（Multi-Channel Platform）服务器的相关信息。
 *
 * 该类包含以下主要字段：
 * - type: 服务器类型。
 * - name: 服务器名称。
 * - description: 服务器描述信息。
 * - version: 服务器版本号。
 * - enabled: 是否启用该服务器配置，默认为 true。
 * - remoteServerConfig: 远程服务器配置信息。
 * - localServerConfig: 本地服务器配置信息，以键值对形式存储。
 * - toolsDescriptionRef: 工具描述引用信息。
 * - promptDescriptionRef: 提示描述引用信息。
 * - resourceDescriptionRef: 资源描述引用信息。
 *
 * 此类提供了 getter 和 setter 方法，用于访问和修改上述字段的值。
 */
public class McpServerInfo {

	private String protocol;

	private String name;

	private String description;

	private String version;

	private Boolean enabled = true;

	private RemoteServerConfigInfo remoteServerConfig;

	private Map<Object, Object> localServerConfig;

	private String toolsDescriptionRef;

	private String promptDescriptionRef;

	private String resourceDescriptionRef;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public RemoteServerConfigInfo getRemoteServerConfig() {
		return remoteServerConfig;
	}

	public void setRemoteServerConfig(RemoteServerConfigInfo remoteServerConfig) {
		this.remoteServerConfig = remoteServerConfig;
	}

	public Map<Object, Object> getLocalServerConfig() {
		return localServerConfig;
	}

	public void setLocalServerConfig(Map<Object, Object> localServerConfig) {
		this.localServerConfig = localServerConfig;
	}

	public String getToolsDescriptionRef() {
		return toolsDescriptionRef;
	}

	public void setToolsDescriptionRef(String toolsDescriptionRef) {
		this.toolsDescriptionRef = toolsDescriptionRef;
	}

	public String getPromptDescriptionRef() {
		return promptDescriptionRef;
	}

	public void setPromptDescriptionRef(String promptDescriptionRef) {
		this.promptDescriptionRef = promptDescriptionRef;
	}

	public String getResourceDescriptionRef() {
		return resourceDescriptionRef;
	}

	public void setResourceDescriptionRef(String resourceDescriptionRef) {
		this.resourceDescriptionRef = resourceDescriptionRef;
	}

}
