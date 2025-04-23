
package com.alibaba.cloud.ai.param;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ModelRunActionParam {

	@Schema(description = "action key, bean name", examples = { "chatModel, chatClient" })
	private String key;

	@Schema(description = "user input")
	private String input;

	@Schema(description = "system prompt")
	private String prompt;

	@Schema(description = "use stream response", defaultValue = "false")
	private Boolean stream = Boolean.FALSE;

	@Schema(description = "chat model config", nullable = true)
	private DashScopeChatOptions chatOptions;

	@Schema(description = "image model config", nullable = true)
	private DashScopeImageOptions imageOptions;

}
