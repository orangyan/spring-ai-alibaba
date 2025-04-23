package com.alibaba.cloud.ai.dashscope.audio.synthesis;

import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.MutableResponseMetadata;
import org.springframework.lang.Nullable;

/**
 * SpeechSynthesisResponseMetadata 类用于封装语音合成响应中的元数据。
 * 它继承自 MutableResponseMetadata，并添加了特定于语音合成的元数据信息。
 *
 * @author kevinlin09
 */
public class SpeechSynthesisResponseMetadata extends MutableResponseMetadata {

    // AI_METADATA_STRING 用于定义元数据字符串的格式。
    protected static final String AI_METADATA_STRING = "{ @type: %1$s, requestsLimit: %2$s }";

    // NULL 提供一个静态的 SpeechSynthesisResponseMetadata 实例，用于表示空值。
    public static final SpeechSynthesisResponseMetadata NULL = new SpeechSynthesisResponseMetadata() {
    };

    // rateLimit 用于存储速率限制信息，可能为 null。
    @Nullable
    private RateLimit rateLimit;

    /**
     * 默认构造函数，创建一个没有速率限制信息的 SpeechSynthesisResponseMetadata 实例。
     */
    public SpeechSynthesisResponseMetadata() {
        this(null);
    }

    /**
     * 构造函数，创建一个带有指定速率限制信息的 SpeechSynthesisResponseMetadata 实例。
     *
     * @param rateLimit 速率限制信息，可能为 null。
     */
    public SpeechSynthesisResponseMetadata(@Nullable RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    /**
     * 获取速率限制信息。
     * 如果没有设置速率限制信息，则返回一个空的速率限制对象。
     *
     * @return 速率限制信息，永远不会为 null。
     */
    @Nullable
    public RateLimit getRateLimit() {
        RateLimit rateLimit = this.rateLimit;
        return rateLimit != null ? rateLimit : new EmptyRateLimit();
    }

    /**
     * 设置速率限制信息。
     *
     * @param rateLimit 速率限制信息，可能为 null。
     * @return 当前实例，用于链式调用。
     */
    public SpeechSynthesisResponseMetadata withRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
        return this;
    }

    /**
     * 重写 toString 方法，以自定义格式输出元数据信息。
     *
     * @return 元数据的字符串表示。
     */
    @Override
    public String toString() {
        return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
    }

}
