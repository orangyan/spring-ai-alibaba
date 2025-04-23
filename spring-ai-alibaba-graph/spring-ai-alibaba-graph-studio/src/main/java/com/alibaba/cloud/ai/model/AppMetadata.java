
package com.alibaba.cloud.ai.model;

public class AppMetadata {

	public static final String CHATBOT_MODE = "chatbot";

	public static final String WORKFLOW_MODE = "workflow";

	public static final String[] SUPPORT_MODES = { CHATBOT_MODE, WORKFLOW_MODE };

	private String id;

	private String name;

	private String description;

	private String mode;

	public String getId() {
		return id;
	}

	public AppMetadata setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public AppMetadata setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public AppMetadata setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public AppMetadata setMode(String mode) {
		this.mode = mode;
		return this;
	}

}
