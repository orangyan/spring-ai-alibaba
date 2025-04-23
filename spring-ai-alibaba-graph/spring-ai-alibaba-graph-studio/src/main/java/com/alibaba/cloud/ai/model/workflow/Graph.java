
package com.alibaba.cloud.ai.model.workflow;

import java.util.List;

public class Graph {

	private List<Edge> edges;

	private List<Node> nodes;

	public Graph() {
	}

	public Graph(List<Edge> edges, List<Node> nodes) {
		this.edges = edges;
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public Graph setEdges(List<Edge> edges) {
		this.edges = edges;
		return this;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Graph setNodes(List<Node> nodes) {
		this.nodes = nodes;
		return this;
	}

}
