
package com.alibaba.cloud.ai.toolcalling.yuque;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 北极星
 */
@ConfigurationProperties(prefix = YuqueConstants.CONFIG_PREFIX)
public class YuqueProperties extends CommonToolCallProperties {

	public YuqueProperties() {
		super("https://www.yuque.com/api/v2");
		this.setPropertiesFromEnv(null, null, null, YuqueConstants.TOKEN_ENV);
	}

}
