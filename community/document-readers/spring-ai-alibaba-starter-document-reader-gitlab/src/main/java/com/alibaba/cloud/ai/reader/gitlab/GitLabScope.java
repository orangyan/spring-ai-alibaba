
package com.alibaba.cloud.ai.reader.gitlab;

/**
 * Scope enum. Used to determine the scope of the issue.
 *
 * @author brianxiadong
 */
public enum GitLabScope {

	/**
	 * Issues created by the authenticated user
	 */
	CREATED_BY_ME("created_by_me"),

	/**
	 * Issues assigned to the authenticated user
	 */
	ASSIGNED_TO_ME("assigned_to_me"),

	/**
	 * All issues
	 */
	ALL("all");

	private final String value;

	GitLabScope(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
