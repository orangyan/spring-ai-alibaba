
package com.alibaba.cloud.ai.toolcalling.time;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author chengle
 */
@Configuration
@EnableConfigurationProperties(TimeProperties.class)
@ConditionalOnClass({ TimeService.class, GetTimeByZoneIdService.class })
@ConditionalOnProperty(prefix = TimeConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class TimeAutoConfiguration {

	@Bean(name = TimeConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Get the time of a specified city.")
	public GetTimeByZoneIdService getCityTimeFunction() {
		return new GetTimeByZoneIdService();
	}

}
