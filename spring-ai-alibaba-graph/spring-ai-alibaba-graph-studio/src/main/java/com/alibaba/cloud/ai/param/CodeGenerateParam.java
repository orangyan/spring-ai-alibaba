
package com.alibaba.cloud.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * code generate param TODO complement
 */
public class CodeGenerateParam {

	@Schema(description = "node type", example = "CODE")
	private String nodeType;

	@Schema(description = "node data")
	private Map<String, Object> nodeData;

	public String getNodeType() {
		return nodeType;
	}

	public CodeGenerateParam setNodeType(String nodeType) {
		this.nodeType = nodeType;
		return this;
	}

	public Map<String, Object> getNodeData() {
		return nodeData;
	}

	public CodeGenerateParam setNodeData(Map<String, Object> nodeData) {
		this.nodeData = nodeData;
		return this;
	}

}
