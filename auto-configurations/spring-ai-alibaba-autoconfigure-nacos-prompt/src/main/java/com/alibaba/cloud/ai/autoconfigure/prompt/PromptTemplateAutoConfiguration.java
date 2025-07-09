
package com.alibaba.cloud.ai.autoconfigure.prompt;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;

import com.alibaba.cloud.ai.prompt.PromptTemplateBuilderConfigure;
import com.alibaba.cloud.ai.prompt.PromptTemplateCustomizer;
import org.springframework.beans.factory.ObjectProvider;
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

	@Bean
	@ConditionalOnMissingBean
	public PromptTemplateBuilderConfigure promptTemplateBuilderConfigure(
			ObjectProvider<PromptTemplateCustomizer> customizerProvider) {
		PromptTemplateBuilderConfigure promptTemplateBuilderConfigure = new PromptTemplateBuilderConfigure();
		promptTemplateBuilderConfigure.setPromptTemplateBuilderCustomizers(customizerProvider.orderedStream().toList());
		return promptTemplateBuilderConfigure;
	}

	// @formatter:off
	@Bean
	@ConditionalOnMissingBean
	public ConfigurablePromptTemplateFactory configurablePromptTemplateFactory(PromptTemplateBuilderConfigure promptTemplateBuilderConfigure) {

		return new ConfigurablePromptTemplateFactory(promptTemplateBuilderConfigure);
	}
	// @formatter:on

}
