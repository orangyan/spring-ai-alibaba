
package com.alibaba.cloud.ai.param;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import io.swagger.v3.oas.annotations.media.Schema;

public class ClientRunActionParam {

	@Schema(description = "action key, bean name", examples = { "chatModel, chatClient" })
	private String key;

	@Schema(description = "user input")
	private String input;

	@Schema(description = "system prompt")
	private String prompt;

	@Schema(description = "chat id use for chat mode, if not set, server will set a new", nullable = true)
	private String chatID;

	@Schema(description = "use stream response", defaultValue = "false")
	private Boolean stream = Boolean.FALSE;

	@Schema(description = "chat model config", nullable = true)
	private DashScopeChatOptions chatOptions;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getChatID() {
		return chatID;
	}

	public void setChatID(String chatID) {
		this.chatID = chatID;
	}

	public Boolean getStream() {
		return stream;
	}

	public void setStream(Boolean stream) {
		this.stream = stream;
	}

	public DashScopeChatOptions getChatOptions() {
		return chatOptions;
	}

	public void setChatOptions(DashScopeChatOptions chatOptions) {
		this.chatOptions = chatOptions;
	}

	@Override
	public String toString() {
		return "ClientRunActionParam{" + "key='" + key + '\'' + ", input='" + input + '\'' + ", prompt='" + prompt
				+ '\'' + ", chatID='" + chatID + '\'' + ", stream=" + stream + ", chatOptions=" + chatOptions + '}';
	}

}
