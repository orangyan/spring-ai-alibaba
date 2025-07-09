
package com.alibaba.cloud.ai.toolcalling.time;

import java.util.TimeZone;

/**
 * @author chengle
 */
public class TimeService {

	public String getCurrentLocalTime() {
		TimeZone timeZone = TimeZone.getDefault();
		return String.format("The current local time is %s", TimeUtils.getTimeByZoneId(timeZone.getID()));
	}

}
