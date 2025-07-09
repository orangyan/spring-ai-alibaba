
package com.alibaba.cloud.ai.model.workflow;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;

import java.util.List;

/**
 * NodeData defines the behavior of a node. Each subclass represents the behavior of the
 * node.
 */
public class NodeData {

	/**
	 * The inputs of the node is the output reference of the previous node
	 */
	protected List<VariableSelector> inputs;

	/**
	 * The output variables of a node
	 */
	protected List<Variable> outputs;

	protected String varName;

	public NodeData() {

	}

	protected NodeData(List<VariableSelector> inputs, List<Variable> outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public List<VariableSelector> getInputs() {
		return inputs;
	}

	public NodeData setInputs(List<VariableSelector> inputs) {
		this.inputs = inputs;
		return this;
	}

	public List<Variable> getOutputs() {
		return outputs;
	}

	public NodeData setOutputs(List<Variable> outputs) {
		this.outputs = outputs;
		return this;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public static String defaultOutputKey(String nodeId) {
		return nodeId + "_output";
	}

}
