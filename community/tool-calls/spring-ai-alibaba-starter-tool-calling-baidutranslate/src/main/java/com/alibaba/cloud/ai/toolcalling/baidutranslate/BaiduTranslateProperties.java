

package com.alibaba.cloud.ai.toolcalling.baidutranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author SCMRCORE
 */
@ConfigurationProperties(prefix = BaiduTranslateConstants.CONFIG_PREFIX)
public class BaiduTranslateProperties extends CommonToolCallProperties {

	private static final String TRANSLATE_HOST_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate/";

	public BaiduTranslateProperties() {
		super(TRANSLATE_HOST_URL);
		this.setPropertiesFromEnv(null, BaiduTranslateConstants.SECRET_KEY_ENV, BaiduTranslateConstants.APP_ID_ENV,
				null);
	}

}
