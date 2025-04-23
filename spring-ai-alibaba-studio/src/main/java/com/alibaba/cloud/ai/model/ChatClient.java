
package com.alibaba.cloud.ai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.ChatOptions;

@Data
@Builder
public class ChatClient {

	@Schema(description = "ChatClient bean name", examples = { "chatClient", "chatClient1" })
	private String name;

	@Schema(description = "Default System Text",
			examples = { "You are a friendly chat bot that answers question in the voice of a {voice}" })
	private String defaultSystemText;

	@Schema(description = "Default System Params")
	private Map<String, Object> defaultSystemParams;

	@Schema(description = "ChatModel of ChatClient")
	private ChatModelConfig chatModel;

	private ChatOptions chatOptions;

	private List<Advisor> advisors;

	private Boolean isMemoryEnabled;

}
