
package com.alibaba.cloud.ai.graph;

import java.util.Objects;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static java.lang.String.format;

/**
 * Represents the output of a node in a graph.
 *
 */
public class NodeOutput {

	public static NodeOutput of(String node, OverAllState state) {
		return new NodeOutput(node, state);
	}

	/**
	 * The identifier of the node.
	 */
	private final String node;

	/**
	 * The state associated with the node.
	 */
	private final OverAllState state;

	private boolean subGraph = false;

	/**
	 * Checks if the current node refers to the start of the graph processing.
	 * @return {@code true} if the current node refers to the start of the graph
	 * processing
	 */
	public boolean isSTART() {
		return Objects.equals(node(), START);
	}

	/**
	 * Checks if the current node refers to the end of the graph processing. useful to
	 * understand if the workflow has been interrupted.
	 * @return {@code true} if the current node refers to the end of the graph processing
	 */
	public boolean isEND() {
		return Objects.equals(node(), END);
	}

	public boolean isSubGraph() {
		return subGraph;
	}

	public NodeOutput setSubGraph(boolean subGraph) {
		this.subGraph = subGraph;
		return this;
	}

	public String node() {
		return node;
	}

	public OverAllState state() {
		return state;
	}

	protected NodeOutput(String node, OverAllState state) {
		this.node = node;
		this.state = state;
	}

	@Override
	public String toString() {
		return format("NodeOutput{node=%s, state=%s}", node(), state());
	}

}
