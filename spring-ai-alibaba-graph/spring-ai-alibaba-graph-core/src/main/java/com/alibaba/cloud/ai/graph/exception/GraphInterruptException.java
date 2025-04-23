
package com.alibaba.cloud.ai.graph.exception;

/**
 * Raised when a subgraph is interrupted, suppressed by the root graph. Never raised
 * directly, or surfaced to the user.
 */
public class GraphInterruptException extends Exception {

	public GraphInterruptException(String message) {
		super(message);
	}

}
