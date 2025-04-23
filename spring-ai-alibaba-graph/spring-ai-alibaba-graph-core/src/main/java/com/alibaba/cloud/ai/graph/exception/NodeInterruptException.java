
package com.alibaba.cloud.ai.graph.exception;

/**
 * Raised by a node to interrupt execution, suppressed by the current graph. Never raised
 * directly, or surfaced to the user.
 */
public class NodeInterruptException extends Exception {

	public NodeInterruptException(String message) {
		super(message);
	}

}
