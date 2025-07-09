
package com.alibaba.cloud.ai.model.workflow.nodedata;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;
import com.alibaba.cloud.ai.model.workflow.NodeData;

import java.util.List;

public class EndNodeData extends NodeData {

	public String getOutputKey() {
		return outputKey;
	}

	public EndNodeData setOutputKey(String outputKey) {
		this.outputKey = outputKey;
		return this;
	}

	private String outputKey;

	public EndNodeData() {
	}

	public EndNodeData(List<VariableSelector> inputs, List<Variable> outputs) {
		super(inputs, outputs);
	}

}
