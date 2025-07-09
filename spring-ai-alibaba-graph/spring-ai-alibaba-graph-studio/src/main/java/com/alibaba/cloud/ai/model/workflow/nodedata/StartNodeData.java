
package com.alibaba.cloud.ai.model.workflow.nodedata;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.VariableSelector;
import com.alibaba.cloud.ai.model.workflow.NodeData;

import java.util.List;

public class StartNodeData extends NodeData {

	private List<StartInput> startInputs;

	private String outputKey;

	public StartNodeData() {
	}

	public StartNodeData(List<VariableSelector> inputs, List<Variable> outputs) {
		super(inputs, outputs);
	}

	public List<StartInput> getStartInputs() {
		return startInputs;
	}

	public StartNodeData setStartInputs(List<StartInput> startInputs) {
		this.startInputs = startInputs;
		return this;
	}

	public String getOutputKey() {
		return outputKey;
	}

	public StartNodeData setOutputKey(String outputKey) {
		this.outputKey = outputKey;
		return this;
	}

	public static class StartInput {

		private String label;

		private String type;

		private String variable;

		private Integer maxLength;

		private List<String> options;

		private Boolean required;

		public String getLabel() {
			return label;
		}

		public StartInput setLabel(String label) {
			this.label = label;
			return this;
		}

		public String getType() {
			return type;
		}

		public StartInput setType(String type) {
			this.type = type;
			return this;
		}

		public String getVariable() {
			return variable;
		}

		public StartInput setVariable(String variable) {
			this.variable = variable;
			return this;
		}

		public Integer getMaxLength() {
			return maxLength;
		}

		public StartInput setMaxLength(Integer maxLength) {
			this.maxLength = maxLength;
			return this;
		}

		public List<String> getOptions() {
			return options;
		}

		public StartInput setOptions(List<String> options) {
			this.options = options;
			return this;
		}

		public Boolean getRequired() {
			return required;
		}

		public StartInput setRequired(Boolean required) {
			this.required = required;
			return this;
		}

	}

}
