
package com.alibaba.cloud.ai.toolcalling.weather;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants;
import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.alibaba.cloud.ai.toolcalling.weather.WeatherProperties.WEATHER_PREFIX;

/**
 * @author 31445
 */
@ConfigurationProperties(prefix = WEATHER_PREFIX)
public class WeatherProperties extends CommonToolCallProperties {

	protected static final String WEATHER_PREFIX = CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX + ".weather";

	public WeatherProperties() {
		super("https://api.weatherapi.com/");
		this.setPropertiesFromEnv("WEATHER_API_KEY", null, null, null);
	}

}
