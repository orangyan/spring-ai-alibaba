
package com.alibaba.cloud.ai.toolcalling.alitranslate;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Aliyun Translation Service Configuration Attributes Class
 *
 * @author Allen Hu
 */
@ConfigurationProperties(prefix = AliTranslateConstants.CONFIG_PREFIX)
public class AliTranslateProperties extends CommonToolCallProperties {

	public AliTranslateProperties() {
		this.setPropertiesFromEnv(null, AliTranslateConstants.ACCESS_KEY_SECRET_ENV, null, null);
		String accessKeyIdEnv = AliTranslateConstants.ACCESS_KEY_ID_ENV;
		if (!StringUtils.hasText(this.getAccessKeyId())) {
			this.setAccessKeyId(System.getenv(accessKeyIdEnv));
		}
	}

}
