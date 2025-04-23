package com.alibaba.cloud.ai.dashscope.audio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;

/**
 * DashScope音频转录选项类
 * 用于配置音频转录的各种参数，如模型、词汇表ID、短语ID等
 *
 * @author xYLiu
 * @author yuluo
 * @author kevinlin09
 */
public class DashScopeAudioTranscriptionOptions implements AudioTranscriptionOptions {

	// @formatter:off
    /**
     * 音频转录模型
     * 默认使用 paraformer-v2 模型
     */
    @JsonProperty("model")
    private String model = "paraformer-v2";

    /**
     * 词汇表ID
     * 用于自定义词汇识别
     */
    @JsonProperty("vocabulary_id")
    private String vocabularyId;

    /**
     * 短语ID
     * 用于自定义短语识别
     */
    @JsonProperty("phrase_id")
    private String phraseId;

    /**
     * 音频采样率
     */
	@JsonProperty("sample_rate")
	private Integer sampleRate;

    /**
     * 音频格式
     */
	@JsonProperty("format")
	private AudioFormat format;

    /**
     * 音频通道ID列表
     * 默认使用通道0
     */
    @JsonProperty("channel_id")
    private List<Integer> channelId = List.of(0);

    /**
     * 是否启用不流畅语音移除
     * 默认为false
     */
    @JsonProperty("disfluency_removal_enabled")
    private Boolean disfluencyRemovalEnabled = false;

    /**
     * 语言提示列表
     * 默认支持中文和英文
     */
    @JsonProperty("language_hints")
    private List<String> languageHints = List.of("zh", "en");

    // @formatter:on
	@Override
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVocabularyId() {
		return vocabularyId;
	}

	public void setVocabularyId(String vocabularyId) {
		this.vocabularyId = vocabularyId;
	}

	public String getPhraseId() {
		return phraseId;
	}

	public Integer getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(Integer sampleRate) {
		this.sampleRate = sampleRate;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public void setPhraseId(String phraseId) {
		this.phraseId = phraseId;
	}

	public List<Integer> getChannelId() {
		return channelId;
	}

	public void setChannelId(List<Integer> channelId) {
		this.channelId = channelId;
	}

	public Boolean getDisfluencyRemovalEnabled() {
		return disfluencyRemovalEnabled;
	}

	public void setDisfluencyRemovalEnabled(Boolean disfluencyRemovalEnabled) {
		this.disfluencyRemovalEnabled = disfluencyRemovalEnabled;
	}

	public List<String> getLanguageHints() {
		return languageHints;
	}

	public void setLanguageHints(List<String> languageHints) {
		this.languageHints = languageHints;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final DashScopeAudioTranscriptionOptions options = new DashScopeAudioTranscriptionOptions();

		public Builder withModel(String model) {
			options.setModel(model);
			return this;
		}

		public Builder withVocabularyId(String vocabularyId) {
			options.setVocabularyId(vocabularyId);
			return this;
		}

		public Builder withPhraseId(String phraseId) {
			options.setPhraseId(phraseId);
			return this;
		}

		public Builder withSampleRate(Integer sampleRate) {
			options.setSampleRate(sampleRate);
			return this;
		}

		public Builder withFormat(AudioFormat format) {
			options.setFormat(format);
			return this;
		}

		public Builder withChannelId(List<Integer> channelId) {
			options.setChannelId(channelId);
			return this;
		}

		public Builder withDisfluencyRemovalEnabled(Boolean disfluencyRemovalEnabled) {
			options.setDisfluencyRemovalEnabled(disfluencyRemovalEnabled);
			return this;
		}

		public Builder withLanguageHints(List<String> languageHints) {
			options.setLanguageHints(languageHints);
			return this;
		}

		public DashScopeAudioTranscriptionOptions build() {
			return options;
		}

	}

	/**
	 * 音频格式枚举
	 * 支持多种常见的音频格式
	 */
	public enum AudioFormat {

		// @formatter:off
		/** PCM格式 */
		@JsonProperty("pcm") PCM("pcm"),
		/** WAV格式 */
		@JsonProperty("wav") WAV("wav"),
		/** MP3格式 */
		@JsonProperty("mp3") MP3("mp3"),
		/** OPUS格式 */
		@JsonProperty("opus") OPUS("opus"),
		/** SPEEX格式 */
		@JsonProperty("speex") SPEEX("speex"),
		/** AAC格式 */
		@JsonProperty("aac") AAC("aac"),
		/** AMR格式 */
		@JsonProperty("amr") AMR("amr");

		// @formatter:on

		/** 格式值 */
		public final String value;

		AudioFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

}
