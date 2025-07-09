
package com.alibaba.cloud.ai.toolcalling.amp;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author YunLong
 */
@Configuration
@EnableConfigurationProperties(AmapProperties.class)
@ConditionalOnProperty(prefix = AmapConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class AmapAutoConfiguration {

	@Bean(name = AmapConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Get weather information according to address from Amap.")
	public WeatherSearchService gaoDeGetAddressWeather(JsonParseTool jsonParseTool, AmapProperties amapProperties) {
		return new WeatherSearchService(jsonParseTool, amapProperties,
				WebClientTool.builder(jsonParseTool, amapProperties).build());
	}

}
