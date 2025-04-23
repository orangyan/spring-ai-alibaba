
package com.alibaba.cloud.ai.evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.Evaluator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LaajEvaluator类是一个抽象类，实现了Evaluator接口。
 * 该类提供了一个框架，用于评估某些条件或表达式的结果。
 * 由于LaajEvaluator是一个抽象类，它不能直接被实例化，但可以作为子类的父类，提供通用的方法和属性。
 * 子类可以继承LaajEvaluator，并根据具体需求实现其抽象方法。
 */
public abstract class LaajEvaluator implements Evaluator {

	private ChatClient.Builder chatClientBuilder;

	private String evaluationPromptText;

	private ObjectMapper objectMapper;

	public LaajEvaluator(ChatClient.Builder chatClientBuilder) {
		this.chatClientBuilder = chatClientBuilder;
		this.evaluationPromptText = getDefaultEvaluationPrompt();
	}

	public LaajEvaluator(ChatClient.Builder chatClientBuilder, String evaluationPromptText) {
		this.chatClientBuilder = chatClientBuilder;
		this.evaluationPromptText = evaluationPromptText;
	}

	public LaajEvaluator(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
		this.chatClientBuilder = chatClientBuilder;
		this.objectMapper = objectMapper;
		this.evaluationPromptText = getDefaultEvaluationPrompt();
	}

	public LaajEvaluator(ChatClient.Builder chatClientBuilder, String evaluationPromptText, ObjectMapper objectMapper) {
		this.chatClientBuilder = chatClientBuilder;
		this.objectMapper = objectMapper;
		this.evaluationPromptText = evaluationPromptText;
	}

	protected String doGetResponse(EvaluationRequest evaluationRequest) {
		return evaluationRequest.getResponseContent();
	}

	@Override
	public String doGetSupportingData(EvaluationRequest evaluationRequest) {
		List<Document> data = evaluationRequest.getDataList();
		return data.stream()
			.filter(node -> node != null && node.getText() != null)
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
	}

	protected abstract String getDefaultEvaluationPrompt();

	public abstract String getName();

	public ChatClient.Builder getChatClientBuilder() {
		return chatClientBuilder;
	}

	public String getEvaluationPromptText() {
		return evaluationPromptText;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}
