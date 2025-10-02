
package com.alibaba.cloud.ai.param;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import io.swagger.v3.oas.annotations.media.Schema;

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

	public DashScopeImageOptions getImageOptions() {
		return imageOptions;
	}

	public void setImageOptions(DashScopeImageOptions imageOptions) {
		this.imageOptions = imageOptions;
	}

	@Override
	public String toString() {
		return "ModelRunActionParam{" + "key='" + key + '\'' + ", input='" + input + '\'' + ", prompt='" + prompt + '\''
				+ ", stream=" + stream + ", chatOptions=" + chatOptions + ", imageOptions=" + imageOptions + '}';
	}

}
