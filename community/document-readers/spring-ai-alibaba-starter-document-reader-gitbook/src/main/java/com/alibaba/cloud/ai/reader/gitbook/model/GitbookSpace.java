
package com.alibaba.cloud.ai.reader.gitbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Represents a space in Gitbook. A space is a collection of pages and serves as the root
 * container.
 *
 * @author brianxiadong
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitbookSpace {

	private String id;

	private String title;

	private List<GitbookPage> pages;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<GitbookPage> getPages() {
		return pages;
	}

	public void setPages(List<GitbookPage> pages) {
		this.pages = pages;
	}

}
