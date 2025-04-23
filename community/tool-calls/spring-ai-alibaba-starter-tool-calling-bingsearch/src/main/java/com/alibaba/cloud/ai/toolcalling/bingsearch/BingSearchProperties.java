
package com.alibaba.cloud.ai.toolcalling.bingsearch;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

/**
 * @author KrakenZJC
 **/
@EnableConfigurationProperties
@ConfigurationProperties(prefix = BingSearchProperties.BING_SEARCH_PREFIX)
public class BingSearchProperties extends CommonToolCallProperties {

	public static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";

	protected static final String BING_SEARCH_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".bingsearch";

	public static final String BING_SEARCH_PATH = "/v7.0/search";

	public BingSearchProperties() {
		super("https://api.bing.microsoft.com");
		this.setPropertiesFromEnv(null, null, null, "BING_SEARCH_TOKEN");
	}

}
