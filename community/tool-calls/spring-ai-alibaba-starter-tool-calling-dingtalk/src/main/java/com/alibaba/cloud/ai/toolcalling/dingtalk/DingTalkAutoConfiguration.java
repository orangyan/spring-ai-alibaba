
package com.alibaba.cloud.ai.toolcalling.dingtalk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * @author YunLong
 */
@Configuration
@EnableConfigurationProperties(DingTalkProperties.class)
@ConditionalOnProperty(prefix = DingTalkProperties.DING_TALK_PREFIX, name = "enabled", havingValue = "true")
public class DingTalkAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Description("Send DingTalk group chat messages using a custom robot")
	public DingTalkRobotService dingTalkGroupSendMessageByCustomRobot(DingTalkProperties dingTalkProperties) {
		return new DingTalkRobotService(dingTalkProperties);
	}

}
