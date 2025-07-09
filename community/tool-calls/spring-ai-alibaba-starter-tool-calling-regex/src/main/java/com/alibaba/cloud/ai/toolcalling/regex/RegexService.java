
package com.alibaba.cloud.ai.toolcalling.regex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author 北极星
 */
public class RegexService implements Function<RegexService.RegexRequest, Object> {

	/**
	 * Applies this function to the given argument.
	 * @param regexRequest the function argument
	 * @return the function result
	 */
	public java.lang.Object apply(RegexRequest regexRequest) {
		String content = regexRequest.content;
		Pattern expression = regexRequest.expression;
		int group = regexRequest.group;
		return RegexUtils.findAll(expression, content, group, new ArrayList<>());
	}

	public record RegexRequest(@JsonProperty("content") String content, @JsonProperty("expression") Pattern expression,
			@JsonProperty("group") int group) {
	}

}
