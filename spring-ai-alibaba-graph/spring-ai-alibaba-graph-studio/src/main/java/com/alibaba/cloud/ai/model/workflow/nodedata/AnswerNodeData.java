
package com.alibaba.cloud.ai.model.workflow.nodedata;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;
import com.alibaba.cloud.ai.model.VariableType;
import com.alibaba.cloud.ai.model.workflow.NodeData;

import java.util.Collections;
import java.util.List;

public class AnswerNodeData extends NodeData {

	public static final List<Variable> DEFAULT_OUTPUTS = List.of(new Variable("answer", VariableType.STRING.value()));

	// a string template
	private String answer;

	private String outputKey;

	public AnswerNodeData() {
		super(Collections.emptyList(), Collections.emptyList());
	}

	public AnswerNodeData(List<VariableSelector> inputs, List<com.alibaba.cloud.ai.model.Variable> outputs) {
		super(inputs, outputs);
	}

	public String getAnswer() {
		return answer;
	}

	public AnswerNodeData setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public String getOutputKey() {
		return outputKey;
	}

	public void setOutputKey(String outputKey) {
		this.outputKey = outputKey;
	}

}
