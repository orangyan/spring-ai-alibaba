
package com.alibaba.cloud.ai.toolcalling.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.util.Assert;

/**
 * @author yuluo
 */
@Configuration
@EnableConfigurationProperties({ CrawlerJinaProperties.class, CrawlerFirecrawlProperties.class })
@ConditionalOnProperty(prefix = CrawlerJinaProperties.JINA_PROPERTIES_PREFIX, name = "enabled", havingValue = "true")
public class CrawlerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("Jina Reader Service Plugin.")
	public CrawlerJinaServiceImpl jinaFunction(CrawlerJinaProperties jinaProperties, ObjectMapper objectMapper) {

		Assert.notNull(jinaProperties, "Jina reader api token must not be empty");
		return new CrawlerJinaServiceImpl(jinaProperties, objectMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("Firecrawl Service Plugin.")
	public CrawlerFirecrawlServiceImpl firecrawlFunction(CrawlerFirecrawlProperties firecrawlProperties,
			ObjectMapper objectMapper) {

		Assert.notNull(firecrawlProperties.getToken(), "Firecrawl api token must not be empty");
		return new CrawlerFirecrawlServiceImpl(firecrawlProperties, objectMapper);
	}

}
