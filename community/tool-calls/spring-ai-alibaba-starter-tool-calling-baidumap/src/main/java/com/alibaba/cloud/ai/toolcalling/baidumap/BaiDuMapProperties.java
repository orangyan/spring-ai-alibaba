
package com.alibaba.cloud.ai.toolcalling.baidumap;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.alibaba.cloud.ai.toolcalling.baidumap.BaiDuMapProperties.BaiDuMapPrefix;
import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;

/**
 * @author Carbon
 * @author vlsmb
 */
@ConfigurationProperties(prefix = BaiDuMapPrefix)
public class BaiDuMapProperties extends CommonToolCallProperties {

	protected static final String BaiDuMapPrefix = TOOL_CALLING_CONFIG_PREFIX + ".baidu.map";

	/**
	 * Official Document URL：
	 * <a href="https://lbs.baidu.com/faq/api?title=webapi/ROS2/prepare">...</a>
	 */
	public BaiDuMapProperties() {
		super("https://api.map.baidu.com/");
		this.setPropertiesFromEnv("BAIDU_MAP_API_KEY", null, null, null);
	}

}
