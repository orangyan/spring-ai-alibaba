
package com.alibaba.cloud.ai.toolcalling.yuque;

import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 北极星
 */
@ConfigurationProperties(prefix = YuqueProperties.YUQUE_PREFIX)
public class YuqueProperties extends CommonToolCallProperties {

	protected static final String YUQUE_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".yuque";

	public YuqueProperties() {
		super("https://www.yuque.com/api/v2/repo");
	}

}
