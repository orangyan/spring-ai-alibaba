
package com.alibaba.cloud.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateAppParam {

	@Schema(description = "app name", example = "rag-demo")
	private String name;

	@Schema(description = "app mode", example = "one of `chatbot`, `workflow`")
	private String mode;

	@Schema(description = "app description")
	private String description;

	public String getName() {
		return name;
	}

	public CreateAppParam setName(String name) {
		this.name = name;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public CreateAppParam setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public CreateAppParam setDescription(String description) {
		this.description = description;
		return this;
	}

}
