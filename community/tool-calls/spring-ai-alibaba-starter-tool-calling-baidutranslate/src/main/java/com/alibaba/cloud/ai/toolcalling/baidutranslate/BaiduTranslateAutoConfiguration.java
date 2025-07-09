

package com.alibaba.cloud.ai.toolcalling.baidutranslate;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.RestClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author SCMRCORE
 */

@Configuration
@EnableConfigurationProperties(BaiduTranslateProperties.class)
@ConditionalOnProperty(prefix = BaiduTranslateConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class BaiduTranslateAutoConfiguration {

	@Bean(name = BaiduTranslateConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Baidu translation function for general text translation")
	public BaiduTranslateService baiduTranslate(BaiduTranslateProperties properties, JsonParseTool jsonParseTool) {

		return new BaiduTranslateService(properties, RestClientTool.builder(jsonParseTool, properties).build(),
				jsonParseTool);
	}

}
