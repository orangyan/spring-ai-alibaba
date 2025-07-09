
package com.alibaba.cloud.ai.model.workflow.nodedata;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;
import com.alibaba.cloud.ai.model.workflow.Case;
import com.alibaba.cloud.ai.model.workflow.NodeData;

import java.util.List;

public class BranchNodeData extends NodeData {

	private List<Case> cases;

	private String outputKey;

	public BranchNodeData() {
	}

	public BranchNodeData(List<VariableSelector> inputs, List<Variable> outputs) {
		super(inputs, outputs);
	}

	public List<Case> getCases() {
		return cases;
	}

	public BranchNodeData setCases(List<Case> cases) {
		this.cases = cases;
		return this;
	}

	public String getOutputKey() {
		return outputKey;
	}

	public BranchNodeData setOutputKey(String outputKey) {
		this.outputKey = outputKey;
		return this;
	}

}
