
package com.alibaba.cloud.ai.document.reader.es;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for Elasticsearch document reader. Contains all necessary settings
 * to connect to and query Elasticsearch.
 *
 * @author brianxiadong
 * @since 0.0.1
 */
public class ElasticsearchConfig {

	/**
	 * Elasticsearch host URL
	 */
	private String host = "localhost";

	/**
	 * Elasticsearch port
	 */
	private int port = 9200;

	/**
	 * List of cluster nodes in format: hostname:port
	 */
	private List<String> nodes = new ArrayList<>();

	/**
	 * Index name to query
	 */
	private String index;

	/**
	 * Query field to search in
	 */
	private String queryField = "content";

	/**
	 * Username for authentication (optional)
	 */
	private String username;

	/**
	 * Password for authentication (optional)
	 */
	private String password;

	/**
	 * Maximum number of documents to retrieve
	 */
	private int maxResults = 10;

	/**
	 * Connection scheme (http/https)
	 */
	private String scheme = "http";

	// Getters and Setters
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}
