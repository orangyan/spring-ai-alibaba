
package com.alibaba.cloud.ai.toolcalling.githubtoolkit;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;

/**
 * @author Yeaury
 */
@Configuration
@EnableConfigurationProperties(GithubToolKitProperties.class)
@ConditionalOnClass(GithubToolKitProperties.class)
@ConditionalOnProperty(prefix = GithubToolKitProperties.GITHUB_TOOLKIT_PREFIX, name = "enabled", havingValue = "true")
public class GithubToolKitAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("implement the function of get a GitHub issue operation")
	public GetIssueService getIssue(GithubToolKitProperties properties, JsonParseTool jsonParseTool) {
		WebClientTool githubWebClientTool = githubWebClientTool(properties, jsonParseTool);
		return new GetIssueService(properties, githubWebClientTool, jsonParseTool);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("implement the function of create GitHub pull request operation")
	public CreatePullRequestService createPullRequest(GithubToolKitProperties properties, JsonParseTool jsonParseTool) {
		WebClientTool githubWebClientTool = githubWebClientTool(properties, jsonParseTool);
		return new CreatePullRequestService(properties, githubWebClientTool, jsonParseTool);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("implement the function of search the list of repositories operation")
	public SearchRepositoryService SearchRepository(GithubToolKitProperties properties, JsonParseTool jsonParseTool) {
		WebClientTool githubWebClientTool = githubWebClientTool(properties, jsonParseTool);
		return new SearchRepositoryService(githubWebClientTool, jsonParseTool);
	}

	public WebClientTool githubWebClientTool(GithubToolKitProperties properties, JsonParseTool jsonParseTool) {
		return WebClientTool.builder(jsonParseTool, properties).httpHeadersConsumer(headers -> {
			headers.set(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT);
			headers.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
			headers.set("X-GitHub-Api-Version", GithubToolKitProperties.X_GitHub_Api_Version);
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getToken());
		}).build();
	}

}
