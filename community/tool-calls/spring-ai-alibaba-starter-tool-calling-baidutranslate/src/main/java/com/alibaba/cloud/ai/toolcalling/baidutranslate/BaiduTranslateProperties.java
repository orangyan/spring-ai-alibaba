

package com.alibaba.cloud.ai.toolcalling.baidutranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.alibaba.cloud.ai.toolcalling.baidutranslate.BaiduTranslateProperties.BaiDuTranslatePrefix;
import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

/**
 * @author SCMRCORE
 */
@ConfigurationProperties(prefix = BaiDuTranslatePrefix)
public class BaiduTranslateProperties extends CommonToolCallProperties {

	protected static final String BaiDuTranslatePrefix = TOOL_CALLING_CONFIG_PREFIX + ".baidu.translate";

	private static final String TRANSLATE_HOST_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate/";

	public BaiduTranslateProperties() {
		super(TRANSLATE_HOST_URL);
		this.setPropertiesFromEnv(null, "BAIDU_TRANSLATE_SECRET_KEY", "BAIDU_TRANSLATE_APP_ID", null);
	}

}
