
package com.alibaba.cloud.ai.prompt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.cloud.nacos.annotation.NacosConfigListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
/**
 * ConfigurablePromptTemplateFactory是一个工厂类，用于生成可配置的提示模板。
 * 该类的设计目的是为了提供一个灵活的方式来创建提示模板，这些模板可以用于各种场景，如用户输入提示、错误消息等。
 * 它允许通过配置不同的参数来定制提示模板，而不是依赖于硬编码的字符串，从而提高了代码的可维护性和可重用性。
 */
public class ConfigurablePromptTemplateFactory {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurablePromptTemplateFactory.class);

	private final Map<String, ConfigurablePromptTemplate> templates = new ConcurrentHashMap<>();

	private final PromptTemplateBuilderConfigure promptTemplateBuilderConfigure;

	public ConfigurablePromptTemplateFactory(PromptTemplateBuilderConfigure promptTemplateBuilderConfigure) {
		this.promptTemplateBuilderConfigure = promptTemplateBuilderConfigure;
	}

	public ConfigurablePromptTemplate create(String name, Resource resource) {
		return templates.computeIfAbsent(name, k -> new ConfigurablePromptTemplate(name, resource));
	}

	public ConfigurablePromptTemplate create(String name, String template) {
		return templates.computeIfAbsent(name, k -> new ConfigurablePromptTemplate(name, template));
	}

	public ConfigurablePromptTemplate create(String name, String template, Map<String, Object> model) {
		return templates.computeIfAbsent(name, k -> new ConfigurablePromptTemplate(name, template, model));
	}

	public ConfigurablePromptTemplate create(String name, Resource resource, Map<String, Object> model) {
		return templates.computeIfAbsent(name, k -> new ConfigurablePromptTemplate(name, resource, model));
	}

	public ConfigurablePromptTemplate create(String name, PromptTemplate promptTemplate) {
		return templates.computeIfAbsent(name, k -> new ConfigurablePromptTemplate(name, promptTemplate));
	}

	@NacosConfigListener(dataId = "spring.ai.alibaba.configurable.prompt", group = "DEFAULT_GROUP", initNotify = true)
	protected void onConfigChange(List<ConfigurablePromptTemplateModel> configList) {
		if (CollectionUtils.isEmpty(configList)) {
			return;
		}
		for (ConfigurablePromptTemplateModel configuration : configList) {
			if (!StringUtils.hasText(configuration.name()) || !StringUtils.hasText(configuration.template())) {
				continue;
			}
			PromptTemplate.Builder promptTemplateBuilder = promptTemplateBuilderConfigure
				.configure(PromptTemplate.builder()
					.template(configuration.template())
					.variables(configuration.model() == null ? new HashMap<>() : configuration.model()));

			templates.put(configuration.name(),
					new ConfigurablePromptTemplate(configuration.name(), promptTemplateBuilder.build()));

			logger.info("OnPromptTemplateConfigChange,templateName:{},template:{},model:{}", configuration.name(),
					configuration.template(), configuration.model());
		}
	}

	public ConfigurablePromptTemplate getTemplate(String name) {
		return templates.get(name);
	}

	public record ConfigurablePromptTemplateModel(String name, String template, Map<String, Object> model) {

	}

}
