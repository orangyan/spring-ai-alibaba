
package com.alibaba.cloud.ai.model.workflow;

import com.alibaba.cloud.ai.model.Variable;

import java.util.List;

public class Workflow {

	private Graph graph;

	private List<Variable> workflowVars;

	private List<Variable> envVars;

	public Graph getGraph() {
		return graph;
	}

	public Workflow setGraph(Graph graph) {
		this.graph = graph;
		return this;
	}

	public List<Variable> getWorkflowVars() {
		return workflowVars;
	}

	public Workflow setWorkflowVars(List<Variable> workflowVars) {
		this.workflowVars = workflowVars;
		return this;
	}

	public List<Variable> getEnvVars() {
		return envVars;
	}

	public Workflow setEnvVars(List<Variable> envVars) {
		this.envVars = envVars;
		return this;
	}

}
