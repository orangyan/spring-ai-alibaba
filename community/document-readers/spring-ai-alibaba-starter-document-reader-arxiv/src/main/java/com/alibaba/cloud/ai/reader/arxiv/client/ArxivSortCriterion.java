
package com.alibaba.cloud.ai.reader.arxiv.client;

/**
 * ArxivSortCriterion identifies the properties by which search results can be sorted
 *
 * @see <a href="https://arxiv.org/help/api/user-manual#sort">arXiv API User's Manual:
 * sort order</a>
 * @author brianxiadong
 */
public enum ArxivSortCriterion {

	/**
	 * Sort by relevance
	 */
	RELEVANCE("relevance"),

	/**
	 * Sort by last updated date
	 */
	LAST_UPDATED_DATE("lastUpdatedDate"),

	/**
	 * Sort by submitted date
	 */
	SUBMITTED_DATE("submittedDate");

	private final String value;

	ArxivSortCriterion(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
