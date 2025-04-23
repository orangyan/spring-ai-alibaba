
package com.alibaba.cloud.ai.reader.arxiv.client;

/**
 * ArxivSortOrder indicates the order in which search results are sorted according to the
 * specified ArxivSortCriterion
 *
 * @see <a href="https://arxiv.org/help/api/user-manual#sort">arXiv API User's Manual:
 * sort order</a>
 * @author brianxiadong
 */
public enum ArxivSortOrder {

	/**
	 * Sort in ascending order
	 */
	ASCENDING("ascending"),

	/**
	 * Sort in descending order
	 */
	DESCENDING("descending");

	private final String value;

	ArxivSortOrder(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
