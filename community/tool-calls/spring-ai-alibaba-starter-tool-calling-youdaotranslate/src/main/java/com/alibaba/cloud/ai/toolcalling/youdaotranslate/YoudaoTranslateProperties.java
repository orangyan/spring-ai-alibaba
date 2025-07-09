
package com.alibaba.cloud.ai.toolcalling.youdaotranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yeaury
 */
@ConfigurationProperties(prefix = YoudaoTranslateConstants.CONFIG_PREFIX)
public class YoudaoTranslateProperties extends CommonToolCallProperties {

	public static final String YOUDAO_TRANSLATE_BASE_URL = "https://openapi.youdao.com";

	public YoudaoTranslateProperties() {
		super(YOUDAO_TRANSLATE_BASE_URL);
		setPropertiesFromEnv(null, YoudaoTranslateConstants.SECRET_KEY_ENV, YoudaoTranslateConstants.APP_ID_ENV, null);
	}

}
