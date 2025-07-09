
package com.alibaba.cloud.ai.toolcalling.baidumap;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author Carbon
 * @author vlsmb
 */

@Configuration
@EnableConfigurationProperties(BaiDuMapProperties.class)
@ConditionalOnProperty(prefix = BaiduMapConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class BaiDuMapAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(BaiDuMapAutoConfiguration.class);

	@Bean(name = BaiduMapConstants.TOOL_NAME_GET_ADDRESS)
	@Description("Search for places using Baidu Maps API "
			+ "or Get detail information of a address and facility query with baidu map or "
			+ "Get address information of a place with baidu map or "
			+ "Get detailed information about a specific place with baidu map")
	public BaiduMapSearchInfoService baiduMapGetAddressInformation(BaiDuMapTools baiDuMapTools) {
		logger.debug("baiduMapSearchInfoService is enabled.");
		return new BaiduMapSearchInfoService(baiDuMapTools);
	}

	@Bean(name = BaiduMapConstants.TOOL_NAME_GET_WEATHER)
	@Description("Query the weather conditions of a specified location")
	public BaiDuMapWeatherService baiDuMapGetAddressWeatherInformation(JsonParseTool jsonParseTool,
			BaiDuMapTools baiDuMapTools) {
		logger.debug("baiDuMapWeatherService is enabled.");
		return new BaiDuMapWeatherService(jsonParseTool, baiDuMapTools);
	}

	@Bean
	public BaiDuMapTools baiDuMapTools(BaiDuMapProperties properties, JsonParseTool jsonParseTool) {
		return new BaiDuMapTools(properties, WebClientTool.builder(jsonParseTool, properties).build(), jsonParseTool);
	}

}
