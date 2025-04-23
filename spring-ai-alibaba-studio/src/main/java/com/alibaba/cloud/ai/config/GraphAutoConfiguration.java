
package com.alibaba.cloud.ai.config;

import com.alibaba.cloud.ai.graph.GraphInitData;
import com.alibaba.cloud.ai.graph.InitDataSerializer;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.NodeOutputSerializer;

import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphAutoConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customJackson() {
		return jackson2ObjectMapperBuilder -> jackson2ObjectMapperBuilder
			.modules(new SimpleModule().addSerializer(GraphInitData.class, new InitDataSerializer(GraphInitData.class))
				.addSerializer(NodeOutput.class, new NodeOutputSerializer()));
	}

}
