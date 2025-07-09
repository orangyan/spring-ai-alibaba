
package com.alibaba.cloud.ai.prompt;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.PromptTemplateActions;
import org.springframework.ai.chat.prompt.PromptTemplateMessageActions;
import org.springframework.ai.content.Media;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public class ConfigurablePromptTemplate implements PromptTemplateActions, PromptTemplateMessageActions {

	private final PromptTemplate promptTemplate;

	private final String name;

	ConfigurablePromptTemplate(String name, Resource resource) {
		this(name, new PromptTemplate(resource));
	}

	ConfigurablePromptTemplate(String name, String template) {
		this(name, new PromptTemplate(template));
	}

	ConfigurablePromptTemplate(String name, String template, Map<String, Object> model) {
		this(name, PromptTemplate.builder().template(template).variables(model).build());
	}

	ConfigurablePromptTemplate(String name, Resource resource, Map<String, Object> model) {
		this(name, PromptTemplate.builder().resource(resource).variables(model).build());
	}

	ConfigurablePromptTemplate(String name, PromptTemplate promptTemplate) {
		this.promptTemplate = promptTemplate;
		this.name = name;
	}

	@Override
	public Prompt create() {
		return promptTemplate.create();
	}

	@Override
	public Prompt create(ChatOptions modelOptions) {
		return promptTemplate.create(modelOptions);
	}

	@Override
	public Prompt create(Map<String, Object> model) {
		return promptTemplate.create(model);
	}

	@Override
	public Prompt create(Map<String, Object> model, ChatOptions modelOptions) {
		return promptTemplate.create(model, modelOptions);
	}

	@Override
	public Message createMessage() {
		return promptTemplate.createMessage();
	}

	@Override
	public Message createMessage(List<Media> mediaList) {
		return promptTemplate.createMessage(mediaList);
	}

	@Override
	public Message createMessage(Map<String, Object> model) {
		return promptTemplate.createMessage(model);
	}

	@Override
	public String render() {
		return promptTemplate.render();
	}

	@Override
	public String render(Map<String, Object> model) {
		return promptTemplate.render(model);
	}

}
