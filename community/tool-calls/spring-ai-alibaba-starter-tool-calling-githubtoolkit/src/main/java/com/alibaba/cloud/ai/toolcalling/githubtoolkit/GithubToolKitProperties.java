
package com.alibaba.cloud.ai.toolcalling.githubtoolkit;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yeaury
 */
@ConfigurationProperties(prefix = GithubToolKitConstants.CONFIG_PREFIX)
public class GithubToolKitProperties extends CommonToolCallProperties {

	public static final String X_GitHub_Api_Version = "2022-11-28";

	private String owner;

	private String repository;

	public GithubToolKitProperties() {
		super("https://api.github.com");
		setPropertiesFromEnv(null, null, null, GithubToolKitConstants.TOKEN_ENV);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

}
