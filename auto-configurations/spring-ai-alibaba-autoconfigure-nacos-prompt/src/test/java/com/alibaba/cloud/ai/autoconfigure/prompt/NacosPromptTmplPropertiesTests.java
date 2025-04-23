
package com.alibaba.cloud.ai.autoconfigure.prompt;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NacosPromptTmplPropertiesTests {

	@Autowired
	private NacosPromptTmplProperties properties;

	@Test
	void testDefaultEnabled() {
		assertFalse(properties.isEnabled(), "Default enabled should be false");
	}

	@Test
	void testSetEnabled() {
		properties.setEnabled(true);
		assertTrue(properties.isEnabled(), "Enabled should be true after setting to true");
	}

	@Configuration
	@EnableConfigurationProperties(NacosPromptTmplProperties.class)
	static class TestConfig {

	}

}
