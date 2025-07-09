
package com.alibaba.cloud.ai.toolcalling.regex;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author 北极星
 */
@Configuration
@ConditionalOnClass(RegexService.class)
@ConditionalOnProperty(prefix = RegexConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class RegexAutoConfiguration {

	@Bean(name = RegexConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Use regex to find content based on the expression.")
	public RegexService regexFindAll() {
		return new RegexService();
	}

}
