

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

/**
 * RemoteServerConfigInfo类用于存储远程服务器的配置信息
 * 它提供了一些方法来获取和设置服务器的配置参数，如IP地址、端口号等
 */
public class RemoteServerConfigInfo {

	private ServiceRefInfo serviceRef;

	private String exportPath;

	public ServiceRefInfo getServiceRef() {
		return serviceRef;
	}

	public void setServiceRef(ServiceRefInfo serviceRef) {
		this.serviceRef = serviceRef;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

}
