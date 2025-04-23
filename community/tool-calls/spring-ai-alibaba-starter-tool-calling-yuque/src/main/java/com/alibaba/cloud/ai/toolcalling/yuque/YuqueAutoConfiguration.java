
package com.alibaba.cloud.ai.toolcalling.yuque;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author 北极星
 */
@Configuration
@ConditionalOnProperty(prefix = YuqueProperties.YUQUE_PREFIX, name = "enabled", havingValue = "true")
@ConditionalOnClass
@EnableConfigurationProperties(YuqueProperties.class)
public class YuqueAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("Use yuque api to invoke a http request to create a doc.")
	public YuqueQueryDocService createYuqueDoc(YuqueProperties yuqueProperties, JsonParseTool jsonParseTool) {
		return new YuqueQueryDocService(WebClientTool.builder(jsonParseTool, yuqueProperties)
			.httpHeadersConsumer(headers -> headers.set("X-Auth-Token", yuqueProperties.getToken()))
			.build(), jsonParseTool);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("Use yuque api to invoke a http request to create a book.")
	public YuqueQueryBookService createYuqueBook(YuqueProperties yuqueProperties, JsonParseTool jsonParseTool) {
		return new YuqueQueryBookService(WebClientTool.builder(jsonParseTool, yuqueProperties)
			.httpHeadersConsumer(headers -> headers.set("X-Auth-Token", yuqueProperties.getToken()))
			.build(), jsonParseTool);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("Use yuque api to invoke a http request to update your doc.")
	public YuqueUpdateDocService updateDocService(YuqueProperties yuqueProperties, JsonParseTool jsonParseTool) {
		return new YuqueUpdateDocService(WebClientTool.builder(jsonParseTool, yuqueProperties)
			.httpHeadersConsumer(headers -> headers.set("X-Auth-Token", yuqueProperties.getToken()))
			.build(), jsonParseTool);
	}

	@Bean
	@ConditionalOnMissingBean
	@Description("Use yuque api to invoke a http request to delete your doc.")
	public YuqueDeleteDocService deleteDocService(YuqueProperties yuqueProperties, JsonParseTool jsonParseTool) {
		return new YuqueDeleteDocService(WebClientTool.builder(jsonParseTool, yuqueProperties)
			.httpHeadersConsumer(headers -> headers.set("X-Auth-Token", yuqueProperties.getToken()))
			.build(), jsonParseTool);
	}

}
