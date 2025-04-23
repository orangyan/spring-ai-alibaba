
package com.alibaba.cloud.ai.evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;

import java.util.Collections;

/**
 * AnswerCorrectnessEvaluator类用于评估答案的正确性
 * 它继承自LaajEvaluator类，旨在通过特定的算法或逻辑判断答案的准确性
 * 此类主要作用于处理和评判用户答案，确定其是否符合预期的标准或结果
 */
public class AnswerCorrectnessEvaluator extends LaajEvaluator {

	private static final String DEFAULT_EVALUATION_PROMPT_TEXT = """
			你的任务是评估Query返回的Response是否符合提供的Context信息。
			你有两个选项来回答，要么是"YES"/"NO"。
			如果查询的响应与上下文信息一致，回答"YES"，否则回答"NO"。

			Query: {query}
			Response: {response}
			Context: {context}

			Answer: "
			""";

	public AnswerCorrectnessEvaluator(ChatClient.Builder chatClientBuilder) {
		super(chatClientBuilder);
	}

	public AnswerCorrectnessEvaluator(ChatClient.Builder chatClientBuilder, String evaluationPromptText) {
		super(chatClientBuilder, evaluationPromptText);
	}

	public AnswerCorrectnessEvaluator(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
		super(chatClientBuilder, objectMapper);
	}

	public AnswerCorrectnessEvaluator(ChatClient.Builder chatClientBuilder, String evaluationPromptText,
			ObjectMapper objectMapper) {
		super(chatClientBuilder, evaluationPromptText, objectMapper);
	}

	@Override
	public EvaluationResponse evaluate(EvaluationRequest evaluationRequest) {
		// Add parameter validation
		if (evaluationRequest == null) {
			throw new IllegalArgumentException("EvaluationRequest must not be null");
		}

		var response = doGetResponse(evaluationRequest);
		var context = doGetSupportingData(evaluationRequest);

		String evaluationResponse = getChatClientBuilder().build()
			.prompt()
			.user(userSpec -> userSpec.text(getEvaluationPromptText())
				.param("query", evaluationRequest.getUserText())
				.param("response", response)
				.param("context", context))
			.call()
			.content();

		boolean passing = false;
		float score = 0;
		if (evaluationResponse.toUpperCase().contains("YES")) {
			passing = true;
			score = 1;
		}

		return new EvaluationResponse(passing, score, "", Collections.emptyMap());
	}

	@Override
	protected String getDefaultEvaluationPrompt() {
		return DEFAULT_EVALUATION_PROMPT_TEXT;
	}

	@Override
	public String getName() {
		return "correctness";
	}

}
