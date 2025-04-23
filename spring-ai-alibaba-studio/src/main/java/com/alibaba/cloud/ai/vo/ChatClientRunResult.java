
package com.alibaba.cloud.ai.vo;

import com.alibaba.cloud.ai.param.ClientRunActionParam;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatClientRunResult {

	private ClientRunActionParam input;

	private ActionResult result;

	private TelemetryResult telemetry;

	private String ChatID;

}
