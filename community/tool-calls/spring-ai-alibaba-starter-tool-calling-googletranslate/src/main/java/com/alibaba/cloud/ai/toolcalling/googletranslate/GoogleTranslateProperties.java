
package com.alibaba.cloud.ai.toolcalling.googletranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author erasernoob
 */
@ConfigurationProperties(prefix = GoogleTranslateConstants.CONFIG_PREFIX)
public class GoogleTranslateProperties extends CommonToolCallProperties {

	public GoogleTranslateProperties() {
		super("https://translation.googleapis.com/language/translate/v2");
		this.setPropertiesFromEnv(GoogleTranslateConstants.API_KEY_ENV, null, null, null);
	}

}
