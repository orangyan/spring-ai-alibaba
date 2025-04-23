
package com.alibaba.cloud.ai.vectorstore.opensearch;

import org.springframework.ai.vectorstore.properties.CommonVectorStoreProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.vectorstore.opensearch.OpenSearchVectorStoreProperties.DEFAULT_ALIBABA_OPENSEARCH_CONFIG_PREFIX;

/**
 * @author 北极星
 */

@ConfigurationProperties(prefix = DEFAULT_ALIBABA_OPENSEARCH_CONFIG_PREFIX)
public class OpenSearchVectorStoreProperties extends CommonVectorStoreProperties {

	protected static final String DEFAULT_ALIBABA_OPENSEARCH_CONFIG_PREFIX = "spring.ai.alibaba.vectorstore.opensearch";

	private Boolean enabled;

	private String instanceId;

	private String endpoint;

	private String accessUserName;

	private String accessPassWord;

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessUserName() {
		return accessUserName;
	}

	public void setAccessUserName(String accessUserName) {
		this.accessUserName = accessUserName;
	}

	public String getAccessPassWord() {
		return accessPassWord;
	}

	public void setAccessPassWord(String accessPassWord) {
		this.accessPassWord = accessPassWord;
	}

	public Map<String, Object> toClientParams() {
		Map<String, Object> params = new HashMap<>();
		params.put("instanceId", this.getInstanceId());
		params.put("endpoint", this.getEndpoint());
		params.put("accessUserName", this.getAccessUserName());
		params.put("accessPassWord", this.getAccessPassWord());
		return params;
	}

}
