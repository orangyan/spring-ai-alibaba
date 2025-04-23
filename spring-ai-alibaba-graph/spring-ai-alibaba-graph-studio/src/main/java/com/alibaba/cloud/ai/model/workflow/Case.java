
package com.alibaba.cloud.ai.model.workflow;

import com.alibaba.cloud.ai.model.VariableSelector;

import java.util.List;

public class Case {

	private String id;

	private String logicalOperator;

	private List<Condition> conditions;

	public String getId() {
		return id;
	}

	public Case setId(String id) {
		this.id = id;
		return this;
	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public Case setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
		return this;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public Case setConditions(List<Condition> conditions) {
		this.conditions = conditions;
		return this;
	}

	public static class Condition {

		private String value;

		private String varType;

		// TODO comparison operator enum
		private String comparisonOperator;

		private VariableSelector variableSelector;

		public String getValue() {
			return value;
		}

		public Condition setValue(String value) {
			this.value = value;
			return this;
		}

		public String getVarType() {
			return varType;
		}

		public Condition setVarType(String varType) {
			this.varType = varType;
			return this;
		}

		public String getComparisonOperator() {
			return comparisonOperator;
		}

		public Condition setComparisonOperator(String comparisonOperator) {
			this.comparisonOperator = comparisonOperator;
			return this;
		}

		public VariableSelector getVariableSelector() {
			return variableSelector;
		}

		public Condition setVariableSelector(VariableSelector variableSelector) {
			this.variableSelector = variableSelector;
			return this;
		}

	}

}
