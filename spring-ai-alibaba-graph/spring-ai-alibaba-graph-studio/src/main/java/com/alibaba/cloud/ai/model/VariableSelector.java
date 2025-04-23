
package com.alibaba.cloud.ai.model;

/**
 * VariableSelector is the reference of a variable in State.
 */
public class VariableSelector {

	/**
	 * An isolation domain of the variable, Could be the node id.
	 */
	private String namespace;

	/**
	 * Name of the variable.
	 */
	private String name;

	/**
	 * Label of the variable.
	 */
	private String label;

	public VariableSelector() {
	}

	/**
	 * Only namespace and name is required for a valid selector.
	 * @param namespace An isolation domain of the variable
	 * @param name Name of the variable
	 */
	public VariableSelector(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}

	public VariableSelector(String namespace, String name, String label) {
		this.namespace = namespace;
		this.name = name;
		this.label = label;
	}

	public String getNamespace() {
		return namespace;
	}

	public VariableSelector setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	public String getName() {
		return name;
	}

	public VariableSelector setName(String name) {
		this.name = name;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public VariableSelector setLabel(String label) {
		this.label = label;
		return this;
	}

}
