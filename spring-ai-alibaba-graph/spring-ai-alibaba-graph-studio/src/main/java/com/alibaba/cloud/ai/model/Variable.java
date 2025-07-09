
package com.alibaba.cloud.ai.model;

import java.util.Map;

/**
 * Variable is often used to represent the output of a node, or pre-defined variables in
 * an App. todo: add a strategy field
 */
public class Variable {

	private String name;

	private String value;

	private String valueType;

	private String description;

	private Map<String, Object> extraProperties;

	public Variable() {
	}

	/**
	 * Only name and valueType is required
	 * @param name a valid variable name
	 * @param valueType a {@link VariableType} value
	 */
	public Variable(String name, String valueType) {
		this.name = name;
		this.valueType = valueType;
	}

	public String getName() {
		return name;
	}

	public Variable setName(String name) {
		this.name = name;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Variable setValue(String value) {
		this.value = value;
		return this;
	}

	public String getValueType() {
		return valueType;
	}

	public Variable setValueType(String valueType) {
		this.valueType = valueType;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Variable setDescription(String description) {
		this.description = description;
		return this;
	}

	public Map<String, Object> getExtraProperties() {
		return extraProperties;
	}

	public Variable setExtraProperties(Map<String, Object> extraProperties) {
		this.extraProperties = extraProperties;
		return this;
	}

}
