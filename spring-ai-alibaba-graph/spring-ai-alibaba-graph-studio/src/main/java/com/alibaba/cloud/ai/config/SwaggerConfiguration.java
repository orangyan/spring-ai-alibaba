
package com.alibaba.cloud.ai.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder()
			.group("spring-ai-alibaba-graph-studio api docs")
			.pathsToMatch("/graph-studio/**")
			.build();
	}

}
