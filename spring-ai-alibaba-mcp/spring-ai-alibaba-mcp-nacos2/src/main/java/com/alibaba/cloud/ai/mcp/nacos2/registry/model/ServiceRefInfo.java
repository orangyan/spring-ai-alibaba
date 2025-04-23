

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

/**
 * @author Sunrisea
 */
public class ServiceRefInfo {

	private String namespaceId;

	private String groupName;

	private String serviceName;

	public String getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(String namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
