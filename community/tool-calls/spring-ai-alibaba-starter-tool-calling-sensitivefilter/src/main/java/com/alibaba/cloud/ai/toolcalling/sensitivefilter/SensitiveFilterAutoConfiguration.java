

package com.alibaba.cloud.ai.toolcalling.sensitivefilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author Makoto
 */
@Configuration
@ConditionalOnClass(SensitiveFilterService.class)
@ConditionalOnProperty(prefix = SensitiveFilterProperties.SENSITIVE_FILTER_PREFIX, name = "enabled",
		havingValue = "true")
@EnableConfigurationProperties(SensitiveFilterProperties.class)
public class SensitiveFilterAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("It is used to filter and replace sensitive information in text, "
			+ "such as mobile phone numbers, ID numbers, bank card numbers, etc")
	public SensitiveFilterService sensitiveFilter(SensitiveFilterProperties properties) {
		return new SensitiveFilterService(properties);
	}

}
