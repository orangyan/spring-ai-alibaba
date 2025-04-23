
package com.alibaba.cloud.ai.toolcalling.time;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author chengle
 */
@Configuration
@ConditionalOnClass({ GetCurrentLocalTimeService.class, GetCurrentTimeByTimeZoneIdService.class })
@ConditionalOnProperty(prefix = "spring.ai.alibaba.toolcalling.time", name = "enabled", havingValue = "true")
public class TimeAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("Get the time of a specified city.")
	public GetCurrentTimeByTimeZoneIdService getCityTimeFunction() {
		return new GetCurrentTimeByTimeZoneIdService();
	}

}
