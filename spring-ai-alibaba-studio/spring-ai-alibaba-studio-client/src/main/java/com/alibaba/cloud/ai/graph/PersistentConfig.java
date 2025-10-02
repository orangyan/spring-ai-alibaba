
package com.alibaba.cloud.ai.graph;

import java.util.Objects;

public record PersistentConfig(String sessionId, String threadId) {
	public PersistentConfig {
		Objects.requireNonNull(sessionId);
	}
}
