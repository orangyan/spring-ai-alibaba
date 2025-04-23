
package com.alibaba.cloud.ai.toolcalling.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vlsmb
 */
@Configuration
public class CommonToolCallAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JsonParseTool jsonParseService() {
		return new JsonParseTool();
	}

}
