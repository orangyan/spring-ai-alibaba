package com.alibaba.cloud.ai.model;

import java.util.Map;

import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.model.AbstractResponseMetadata;
import org.springframework.ai.model.ResponseMetadata;

/**
 * 继承自 AbstractResponseMetadata 并实现 ResponseMetadata 接口的类，
 * 用于表示重新排序响应的元数据
 */
public class RerankResponseMetadata extends AbstractResponseMetadata implements ResponseMetadata {

	private Usage usage = new EmptyUsage();

	public RerankResponseMetadata() {
	}

	public RerankResponseMetadata(Usage usage) {
		this.usage = usage;
	}

	public RerankResponseMetadata(Usage usage, Map<String, Object> metadata) {
		this.usage = usage;
		this.map.putAll(metadata);
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

}
