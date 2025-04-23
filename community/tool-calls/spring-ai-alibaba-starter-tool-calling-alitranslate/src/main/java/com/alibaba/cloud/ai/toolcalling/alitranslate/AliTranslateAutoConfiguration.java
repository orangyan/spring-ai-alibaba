
package com.alibaba.cloud.ai.toolcalling.alitranslate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author yunlong
 */
@Configuration
@ConditionalOnClass(AliTranslateService.class)
@EnableConfigurationProperties(AliTranslateProperties.class)
@ConditionalOnProperty(prefix = AliTranslateProperties.PREFIX, name = "enabled", havingValue = "true")
public class AliTranslateAutoConfiguration {

	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	@Description("Implement natural language translation capabilities.")
	public AliTranslateService aliTranslateService(AliTranslateProperties properties) {
		return new AliTranslateService(properties);
	}

}
