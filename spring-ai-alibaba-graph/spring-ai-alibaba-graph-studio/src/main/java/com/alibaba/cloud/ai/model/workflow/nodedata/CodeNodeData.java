
package com.alibaba.cloud.ai.model.workflow.nodedata;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;
import com.alibaba.cloud.ai.model.workflow.NodeData;

import java.util.List;

public class CodeNodeData extends NodeData {

	private String code;

	private String codeLanguage;

	public CodeNodeData() {
	}

	public CodeNodeData(List<VariableSelector> inputs, List<Variable> outputs) {
		super(inputs, outputs);
	}

	public String getCode() {
		return code;
	}

	public CodeNodeData setCode(String code) {
		this.code = code;
		return this;
	}

	public String getCodeLanguage() {
		return codeLanguage;
	}

	public CodeNodeData setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
		return this;
	}

}
