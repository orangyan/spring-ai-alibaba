
package com.alibaba.cloud.ai.reader.gitlab;

/**
 * Issue state enum. Used to decide what issues to retrieve.
 *
 * @author brianxiadong
 */
public enum GitLabIssueState {

	/**
	 * Issues that are open
	 */
	OPEN("opened"),

	/**
	 * Issues that are closed
	 */
	CLOSED("closed"),

	/**
	 * All issues, open and closed
	 */
	ALL("all");

	private final String value;

	GitLabIssueState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
