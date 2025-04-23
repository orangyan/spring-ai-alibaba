
package com.alibaba.cloud.ai.autoconfigure.prompt;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author KrakenZJC
 * @author yuluo
 */

@AutoConfiguration
@Conditional(PromptTmplNacosConfigCondition.class)
@EnableConfigurationProperties(NacosPromptTmplProperties.class)
public class PromptTemplateAutoConfiguration {

	// @formatter:off
	@Bean
	@ConditionalOnMissingBean
	public ConfigurablePromptTemplateFactory configurablePromptTemplateFactory() {

		return new ConfigurablePromptTemplateFactory();
	}
	// @formatter:on

}
