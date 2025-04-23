
package com.alibaba.cloud.ai.toolcalling.youdaotranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants;
import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yeaury
 */
@ConfigurationProperties(prefix = YoudaoTranslateProperties.PREFIX)
public class YoudaoTranslateProperties extends CommonToolCallProperties {

	public static final String PREFIX = CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX + ".youdaotranslate";

	public static final String YOUDAO_TRANSLATE_BASE_URL = "https://openapi.youdao.com";

	/**
	 * @param appKey youdao AppId
	 * @deprecated use {@link #setAppId(String)} instead
	 */
	@Deprecated
	public void setAppKey(String appKey) {
		this.setAppId(appKey);
	}

	/**
	 * @param appSecret youdao AppSecret
	 * @deprecated use {@link #setSecretKey(String)} instead
	 */
	@Deprecated
	public void setAppSecret(String appSecret) {
		this.setSecretKey(appSecret);
	}

	public String getAppKey() {
		return getAppId();
	}

	public String getAppSecret() {
		return getSecretKey();
	}

	public YoudaoTranslateProperties() {
		super(YOUDAO_TRANSLATE_BASE_URL);
		setPropertiesFromEnv(null, "YOUDAO_APP_SECRET", "YOUDAO_APP_ID", null);
	}

}
