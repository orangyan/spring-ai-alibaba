
package com.alibaba.cloud.ai.toolcalling.kuaidi100;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <a href="https://api.kuaidi100.com/manager/v2/myinfo/enterprise">Obtain the
 * authorization key and customer value for kuaidi100.com</a>, They correspond to the
 * configuration items apiKey and appId respectively.<br>
 *
 * You can also set it through environment variables:<br>
 * KUAIDI100_KEY<br>
 * KUAIDI100_CUSTOMER<br>
 *
 * @author XiaoYunTao
 * @since 2024/12/25
 */
@ConfigurationProperties(prefix = Kuaidi100Constants.CONFIG_PREFIX)
public class Kuaidi100Properties extends CommonToolCallProperties {

	public static final String QUERY_BASE_URL = "https://www.kuaidi100.com/";

	public Kuaidi100Properties() {
		super(QUERY_BASE_URL);
		this.setPropertiesFromEnv(Kuaidi100Constants.API_KEY_ENV, null, Kuaidi100Constants.APP_ID_ENV, null);
	}

}
