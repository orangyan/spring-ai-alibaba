
package com.alibaba.cloud.ai.toolcalling.amp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;

/**
 * @author YunLong
 */
@ConfigurationProperties(prefix = AmapConstants.CONFIG_PREFIX)
public class AmapProperties extends CommonToolCallProperties {

	public AmapProperties() {
		super("https://restapi.amap.com/v3");
		this.setPropertiesFromEnv(AmapConstants.API_KEY_ENV, null, null, null);
	}

}
