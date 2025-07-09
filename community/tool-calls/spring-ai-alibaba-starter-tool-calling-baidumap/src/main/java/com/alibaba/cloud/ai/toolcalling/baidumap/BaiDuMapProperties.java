
package com.alibaba.cloud.ai.toolcalling.baidumap;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Carbon
 * @author vlsmb
 */
@ConfigurationProperties(prefix = BaiduMapConstants.CONFIG_PREFIX)
public class BaiDuMapProperties extends CommonToolCallProperties {

	/**
	 * Official Document URL：
	 * <a href="https://lbs.baidu.com/faq/api?title=webapi/ROS2/prepare">...</a>
	 */
	public BaiDuMapProperties() {
		super("https://api.map.baidu.com/");
		this.setPropertiesFromEnv(BaiduMapConstants.API_KEY_ENV, null, null, null);
	}

}
