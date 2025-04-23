
package com.alibaba.cloud.ai.toolcalling.amp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

/**
 * @author YunLong
 */
@ConfigurationProperties(prefix = AmapProperties.AMAP_PREFIX)
public class AmapProperties extends CommonToolCallProperties {

	protected static final String AMAP_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".amap";

	public AmapProperties() {
		super("https://restapi.amap.com/v3");
		this.setPropertiesFromEnv("GAODE_AMAP_API_KEY", null, null, null);
	}

	public String getWebApiKey() {
		return getApiKey();
	}

	@Deprecated
	public void setWebApiKey(String webApiKey) {
		this.setApiKey(webApiKey);
	}

}
