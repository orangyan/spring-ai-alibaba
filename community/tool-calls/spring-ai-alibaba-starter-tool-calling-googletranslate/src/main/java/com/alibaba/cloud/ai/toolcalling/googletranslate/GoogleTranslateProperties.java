
package com.alibaba.cloud.ai.toolcalling.googletranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;
import static com.alibaba.cloud.ai.toolcalling.googletranslate.GoogleTranslateProperties.GOOGLE_TRANSLATE_PREFIX;

/**
 * @author erasernoob
 */
@ConfigurationProperties(prefix = GOOGLE_TRANSLATE_PREFIX)
public class GoogleTranslateProperties extends CommonToolCallProperties {

	protected static final String GOOGLE_TRANSLATE_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".googletranslate";

	public GoogleTranslateProperties() {
		super("https://translation.googleapis.com/language/translate/v2");
		this.setPropertiesFromEnv("GOOGLE_TRANSLATE_APIKEY", null, null, null);
	}

}
