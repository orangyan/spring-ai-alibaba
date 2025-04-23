
package com.alibaba.cloud.ai.param;

import lombok.Data;

@Data
public class GraphStreamParam {

	private String sessionId;

	private String thread;

	private boolean resume;

	private String checkpoint;

	private String node;

}
