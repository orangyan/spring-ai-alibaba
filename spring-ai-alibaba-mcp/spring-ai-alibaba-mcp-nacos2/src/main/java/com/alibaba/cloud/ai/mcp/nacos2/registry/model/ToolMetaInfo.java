

package com.alibaba.cloud.ai.mcp.nacos2.registry.model;

import java.util.Objects;

/**
 * @author Sunrisea
 */
public class ToolMetaInfo {

	private Boolean enabled = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ToolMetaInfo that)) {
			return false;
		}
		return Objects.equals(enabled, that.enabled);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(enabled);
	}

}
