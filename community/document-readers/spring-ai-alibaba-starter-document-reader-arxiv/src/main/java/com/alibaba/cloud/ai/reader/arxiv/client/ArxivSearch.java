
package com.alibaba.cloud.ai.reader.arxiv.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * arXiv database search specification
 *
 * @see <a href="https://arxiv.org/help/api/user-manual#query_details">arXiv API User's
 * Manual: Details of Query Construction</a>
 * @author brianxiadong
 */
public class ArxivSearch {

	private String query; // Query string

	private List<String> idList; // List of article IDs to restrict search

	private Integer maxResults; // Maximum number of results to return

	private ArxivSortCriterion sortBy; // Sort criterion

	private ArxivSortOrder sortOrder; // Sort order

	public ArxivSearch() {
		this("", new ArrayList<>(), null, ArxivSortCriterion.RELEVANCE, ArxivSortOrder.DESCENDING);
	}

	public ArxivSearch(String query, List<String> idList, Integer maxResults, ArxivSortCriterion sortBy,
			ArxivSortOrder sortOrder) {
		this.query = query;
		this.idList = idList;
		this.maxResults = maxResults;
		this.sortBy = sortBy;
		this.sortOrder = sortOrder;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public ArxivSortCriterion getSortBy() {
		return sortBy;
	}

	public void setSortBy(ArxivSortCriterion sortBy) {
		this.sortBy = sortBy;
	}

	public ArxivSortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(ArxivSortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * Returns search parameters for API requests
	 */
	public Map<String, String> getUrlArgs() {
		Map<String, String> args = new HashMap<>();
		args.put("search_query", query);
		args.put("id_list", String.join(",", idList));
		args.put("sortBy", sortBy.getValue());
		args.put("sortOrder", sortOrder.getValue());
		return args;
	}

}
