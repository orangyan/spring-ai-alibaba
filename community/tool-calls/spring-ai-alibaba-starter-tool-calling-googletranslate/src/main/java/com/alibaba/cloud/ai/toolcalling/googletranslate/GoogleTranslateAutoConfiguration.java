
package com.alibaba.cloud.ai.toolcalling.googletranslate;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author erasernoob
 */
@Configuration
@EnableConfigurationProperties(GoogleTranslateProperties.class)
@ConditionalOnProperty(prefix = GoogleTranslateConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class GoogleTranslateAutoConfiguration {

	@Bean(name = GoogleTranslateConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Implement natural language translation capabilities.")
	public GoogleTranslateService googleTranslate(JsonParseTool jsonParseTool, GoogleTranslateProperties properties) {
		return new GoogleTranslateService(properties,
				WebClientTool.builder(jsonParseTool, properties).httpHeadersConsumer(headers -> {
					headers.add("Content-Type", "application/json");
				}).build(), jsonParseTool);
	}

}
