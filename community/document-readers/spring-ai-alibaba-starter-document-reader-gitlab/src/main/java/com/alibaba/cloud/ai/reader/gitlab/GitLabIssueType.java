
package com.alibaba.cloud.ai.reader.gitlab;

/**
 * Issue type enum. Used to decide what issues to retrieve.
 *
 * @author brianxiadong
 */
public enum GitLabIssueType {

	/**
	 * Regular issues
	 */
	ISSUE("issue"),

	/**
	 * Incident issues
	 */
	INCIDENT("incident"),

	/**
	 * Test case issues
	 */
	TEST_CASE("test_case"),

	/**
	 * Task issues
	 */
	TASK("task");

	private final String value;

	GitLabIssueType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
