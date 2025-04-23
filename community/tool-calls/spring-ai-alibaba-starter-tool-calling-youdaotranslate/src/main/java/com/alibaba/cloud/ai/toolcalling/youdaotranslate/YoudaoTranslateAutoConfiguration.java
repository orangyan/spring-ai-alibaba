
package com.alibaba.cloud.ai.toolcalling.youdaotranslate;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author Yeaury
 */
@Configuration
@ConditionalOnClass(YoudaoTranslateService.class)
@EnableConfigurationProperties(YoudaoTranslateProperties.class)
@ConditionalOnProperty(prefix = YoudaoTranslateProperties.PREFIX, name = "enabled", havingValue = "true")
public class YoudaoTranslateAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("use youdao translation to achieve translation")
	public YoudaoTranslateService youdaoTranslate(YoudaoTranslateProperties properties, JsonParseTool jsonParseTool) {
		WebClientTool webClientTool = WebClientTool.builder(jsonParseTool, properties).build();
		return new YoudaoTranslateService(properties, jsonParseTool, webClientTool);
	}

}
