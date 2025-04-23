
package com.alibaba.cloud.ai.toolcalling.serpapi;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ThreadLocalRandom;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.DEFAULT_USER_AGENTS;
import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

/**
 * @author 北极星
 * @author sixiyida
 */
@ConfigurationProperties(prefix = SerpApiProperties.SERP_API_PREFIX)
public class SerpApiProperties extends CommonToolCallProperties {

	protected static final String SERP_API_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".serpapi";

	public static final String USER_AGENT_VALUE = DEFAULT_USER_AGENTS[ThreadLocalRandom.current()
		.nextInt(DEFAULT_USER_AGENTS.length)];

	public SerpApiProperties() {
		super("https://serpapi.com/search");
		this.setPropertiesFromEnv("SERPAPI_KEY", null, null, null);
	}

	private String engine;

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

}
