

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

/**
 * @author Sunrisea
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
