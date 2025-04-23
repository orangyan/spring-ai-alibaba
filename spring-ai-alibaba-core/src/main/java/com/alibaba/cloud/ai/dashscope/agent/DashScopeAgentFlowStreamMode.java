
package com.alibaba.cloud.ai.dashscope.agent;

/**
 * DashScope代理流式输出模式枚举
 * 定义了代理在流式输出时的不同模式
 */
public enum DashScopeAgentFlowStreamMode {

	/**
	 * 完整思维模式
	 * 所有节点的流式结果都会在thoughts字段中输出
	 */
	FULL_THOUGHTS("full_thoughts"),

	/**
	 * 代理格式模式
	 * 使用与代理应用相同的输出模式
	 */
	AGENT_FORMAT("agent_format");

	// 模式对应的字符串值
	private final String value;

	/**
	 * 构造函数
	 * @param value 模式对应的字符串值
	 */
	DashScopeAgentFlowStreamMode(String value) {
		this.value = value;
	}

	/**
	 * 获取模式对应的字符串值
	 * @return 模式对应的字符串值
	 */
	public String getValue() {
		return value;
	}

}
