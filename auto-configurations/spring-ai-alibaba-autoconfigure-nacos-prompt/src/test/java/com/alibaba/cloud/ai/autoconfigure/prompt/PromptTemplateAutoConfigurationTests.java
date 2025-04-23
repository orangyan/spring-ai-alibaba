
package com.alibaba.cloud.ai.autoconfigure.prompt;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@SpringBootTest(classes = PromptTemplateAutoConfiguration.class)
class PromptTemplateAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(PromptTemplateAutoConfiguration.class);

	@Test
	void whenEnabledPropertyIsTrue_thenBeanShouldBeCreated() {

		this.contextRunner.withPropertyValues("spring.ai.nacos.prompt.template.enabled=true")
			.run(context -> assertThat(context).hasSingleBean(ConfigurablePromptTemplateFactory.class));
	}

	/**
	 * Nacos autoconfigure default value is false.
	 */
	@Test
	void whenNoPropertiesConfigured_thenBeanShouldNotBeCreated() {

		this.contextRunner.run(context -> assertThat(context).doesNotHaveBean(ConfigurablePromptTemplateFactory.class));
	}

	@Test
	void whenEnabledPropertyIsFalse_thenBeanShouldNotBeCreated() {

		this.contextRunner.withPropertyValues("spring.ai.nacos.prompt.template.enabled=false")
			.run(context -> assertThat(context).doesNotHaveBean(ConfigurablePromptTemplateFactory.class));
	}

}
