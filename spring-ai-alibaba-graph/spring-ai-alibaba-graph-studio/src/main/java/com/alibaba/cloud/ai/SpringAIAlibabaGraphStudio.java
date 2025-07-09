
package com.alibaba.cloud.ai;

import com.alibaba.cloud.ai.config.GraphProjectGenerationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = { "com.alibaba.cloud.ai" },
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
				classes = GraphProjectGenerationConfiguration.class))
public class SpringAIAlibabaGraphStudio {

	public static void main(String[] args) {
		SpringApplication.run(SpringAIAlibabaGraphStudio.class, args);
	}

}
