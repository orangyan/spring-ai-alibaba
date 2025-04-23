
package com.alibaba.cloud.ai.toolcalling.microsofttranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 31445
 */
@ConfigurationProperties(prefix = "spring.ai.alibaba.toolcalling.microsofttranslate")
public class MicroSoftTranslateProperties extends CommonToolCallProperties {

	private String region;

	public MicroSoftTranslateProperties() {
		super("https://api.cognitive.microsofttranslator.com");
		this.setPropertiesFromEnv("MICROSOFT_TRANSLATE_API_KEY", null, null, null);
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(final String region) {
		this.region = region;
	}

}
