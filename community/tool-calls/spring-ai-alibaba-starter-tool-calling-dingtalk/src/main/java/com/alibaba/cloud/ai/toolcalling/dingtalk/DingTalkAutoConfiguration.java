
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
@ConditionalOnProperty(prefix = DingTalkConstants.CONFIG_PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class DingTalkAutoConfiguration {

	@Bean(name = DingTalkConstants.TOOL_NAME)
	@ConditionalOnMissingBean
	@Description("Send DingTalk group chat messages using a custom robot")
	public DingTalkRobotService dingTalkGroupSendMessageByCustomRobot(DingTalkProperties dingTalkProperties) {
		return new DingTalkRobotService(dingTalkProperties);
	}

}
