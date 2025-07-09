
package com.alibaba.cloud.ai.toolcalling.weather;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 31445
 */
@ConfigurationProperties(prefix = WeatherConstants.CONFIG_PREFIX)
public class WeatherProperties extends CommonToolCallProperties {

	public WeatherProperties() {
		super("https://api.weatherapi.com/");
		this.setPropertiesFromEnv(WeatherConstants.API_KEY_ENV, null, null, null);
	}

}
