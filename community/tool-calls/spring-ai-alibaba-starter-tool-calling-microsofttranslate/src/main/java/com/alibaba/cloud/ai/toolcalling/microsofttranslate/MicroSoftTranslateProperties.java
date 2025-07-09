
package com.alibaba.cloud.ai.toolcalling.microsofttranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 31445
 */
@ConfigurationProperties(prefix = MicroSoftTranslateConstants.CONFIG_PREFIX)
public class MicroSoftTranslateProperties extends CommonToolCallProperties {

	private String region;

	public MicroSoftTranslateProperties() {
		super("https://api.cognitive.microsofttranslator.com");
		this.setPropertiesFromEnv(MicroSoftTranslateConstants.API_KEY_ENV, null, null, null);
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(final String region) {
		this.region = region;
	}

}
