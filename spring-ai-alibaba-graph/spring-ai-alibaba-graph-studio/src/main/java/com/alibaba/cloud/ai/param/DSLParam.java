
package com.alibaba.cloud.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;

public class DSLParam {

	@Schema(description = "dsl raw content")
	private String content;

	@Schema(description = "dsl dialect", examples = { "dify", "flowise", "custom" })
	private String dialect;

	public String getContent() {
		return content;
	}

	public DSLParam setContent(String content) {
		this.content = content;
		return this;
	}

	public String getDialect() {
		return dialect;
	}

	public DSLParam setDialect(String dialect) {
		this.dialect = dialect;
		return this;
	}

}
