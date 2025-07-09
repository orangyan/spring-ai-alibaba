

package com.alibaba.cloud.ai.graph.node.code.entity;

/**
 * @author HeYQ
 */
public class CodeExecutionConfig {

	private String workDir = "workspace";

	/**
	 * the docker image to use for code execution.
	 */
	private String docker;

	private String containerName = "spring-ai-alibaba-container";

	private String dockerHost = "unix:///var/run/docker.sock";

	private int timeout = 600;

	private int lastMessagesNumber = 1;

	private String classPath;

	private int maxConnections = 100;

	private int connectionTimeout = 30;

	private int responseTimeout = 50;

	public String getWorkDir() {
		return workDir;
	}

	public CodeExecutionConfig setWorkDir(String workDir) {
		this.workDir = workDir;
		return this;
	}

	public String getDocker() {
		return docker;
	}

	public CodeExecutionConfig setDocker(String docker) {
		this.docker = docker;
		return this;
	}

	public int getTimeout() {
		return timeout;
	}

	public CodeExecutionConfig setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public int getLastMessagesNumber() {
		return lastMessagesNumber;
	}

	public CodeExecutionConfig setLastMessagesNumber(int lastMessagesNumber) {
		this.lastMessagesNumber = lastMessagesNumber;
		return this;
	}

	public String getContainerName() {
		return containerName;
	}

	public CodeExecutionConfig setContainerName(String containerName) {
		this.containerName = containerName;
		return this;
	}

	public String getDockerHost() {
		return dockerHost;
	}

	public CodeExecutionConfig setDockerHost(String dockerHost) {
		this.dockerHost = dockerHost;
		return this;
	}

	public String getClassPath() {
		return classPath;
	}

	public CodeExecutionConfig setClassPath(String classPath) {
		this.classPath = classPath;
		return this;

	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(final int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(final int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getResponseTimeout() {
		return responseTimeout;
	}

	public void setResponseTimeout(final int responseTimeout) {
		this.responseTimeout = responseTimeout;
	}

}
