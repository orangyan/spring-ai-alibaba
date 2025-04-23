
package com.alibaba.cloud.ai.toolcalling.sinanews;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.DEFAULT_USER_AGENTS;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author XiaoYunTao
 * @since 2024/12/18
 */
@Configuration
@ConditionalOnClass(SinaNewsService.class)
@ConditionalOnProperty(prefix = SinaNewsProperties.SINA_NEWS_PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SinaNewsProperties.class)
public class SinaNewsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("Get the news from the Sina news (获取新浪新闻).")
	public SinaNewsService getSinaNews(JsonParseTool jsonParseTool, SinaNewsProperties properties) {
		Consumer<HttpHeaders> consumer = headers -> {
			headers.add(HttpHeaders.USER_AGENT,
					DEFAULT_USER_AGENTS[ThreadLocalRandom.current().nextInt(DEFAULT_USER_AGENTS.length)]);
			headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,ja;q=0.8");
			headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		};

		return new SinaNewsService(jsonParseTool, properties,
				WebClientTool.builder(jsonParseTool, properties).httpHeadersConsumer(consumer).build());
	}

}
