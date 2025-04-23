
package com.alibaba.cloud.ai.dashscope.observation.conventions;

/**
 * AI服务提供商枚举类
 * 该类列出了可用的AI服务提供商，每个提供商都有其特定的属性和识别度
 */
public enum AiProvider {

	// @formatter:off

    // Please, keep the alphabetical sorting.
    DASHSCOPE("dashscope");

    private final String value;

    AiProvider(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    // @formatter:on

}
