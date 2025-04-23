
package com.alibaba.cloud.ai.toolcalling.alitranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants;
import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Aliyun Translation Service Configuration Attributes Class
 * </p>
 * Fields that must be configured:<br>
 * - Aliyun accessKeyId: Set {@link #setAccessKeyId(String)} or the environment variable
 * {@code ALITRANSLATE_ACCESS_KEY_ID}.<br>
 * - Aliyun accessKeySecret: Set {@link #setSecretKey(String)} or the environment variable
 * {@code ALITRANSLATE_ACCESS_KEY_SECRET}.<br>
 *
 * @author yunlong
 * @author Allen Hu
 */
@ConfigurationProperties(prefix = AliTranslateProperties.PREFIX)
public class AliTranslateProperties extends CommonToolCallProperties {

	public static final String PREFIX = CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX + ".alitranslate";

	private String region;

	public AliTranslateProperties() {
		this.setPropertiesFromEnv(null, "ALITRANSLATE_ACCESS_KEY_SECRET", null, null);
		String accessKeyIdEnv = "ALITRANSLATE_ACCESS_KEY_ID";
		if (!StringUtils.hasText(this.getAccessKeyId())) {
			this.setAccessKeyId(System.getenv(accessKeyIdEnv));
		}
	}

	public String getAccessKeySecret() {
		return getSecretKey();
	}

	/**
	 * @param accessKeySecret AccessKeySecret
	 * @deprecated use {@link #setSecretKey(String)} instead
	 */
	@Deprecated
	public void setAccessKeySecret(String accessKeySecret) {
		this.setSecretKey(accessKeySecret);
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
