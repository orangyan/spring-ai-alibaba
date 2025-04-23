
package com.alibaba.cloud.ai.vo;

import com.alibaba.cloud.ai.param.ModelRunActionParam;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatModelRunResult {

	private ModelRunActionParam input;

	private ActionResult result;

	private TelemetryResult telemetry;

}
