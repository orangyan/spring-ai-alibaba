package com.alibaba.cloud.ai.transformer.splitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.util.Assert;

/**
 * 句子分割器类，基于自然语言处理模型实现句子分割功能。
 * 该类继承自 TextSplitter，提供将文本分割为句子的能力，并支持根据编码令牌限制进行分块。
 *
 * 主要功能：
 * 1. 使用 OpenNLP 的 SentenceDetectorME 进行句子检测和分割。
 * 2. 根据指定的 chunkSize（块大小）对句子进行进一步分块，确保每个块的编码令牌数不超过限制。
 * 3. 内部使用 CL100K_BASE 编码类型对文本进行编码，计算令牌数量。
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */
public class SentenceSplitter extends TextSplitter {

	private final EncodingRegistry registry = Encodings.newLazyEncodingRegistry();

	private final Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);

	private static final int DEFAULT_CHUNK_SIZE = 1024;

	private final SentenceModel sentenceModel;

	private final int chunkSize;

	public SentenceSplitter() {
		this(DEFAULT_CHUNK_SIZE);
	}

	public SentenceSplitter(int chunkSize) {
		this.chunkSize = chunkSize;
		this.sentenceModel = getSentenceModel();
	}

	@Override
	protected List<String> splitText(String text) {
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
		String[] texts = sentenceDetector.sentDetect(text);
		if (texts == null || texts.length == 0) {
			return Collections.emptyList();
		}

		List<String> chunks = new ArrayList<>();
		StringBuilder chunk = new StringBuilder();
		for (int i = 0; i < texts.length; i++) {
			int currentChunkSize = getEncodedTokens(chunk.toString()).size();
			int textTokenSize = getEncodedTokens(texts[i]).size();
			if (currentChunkSize + textTokenSize > chunkSize) {
				chunks.add(chunk.toString());
				chunk = new StringBuilder(texts[i]);
			}
			else {
				chunk.append(texts[i]);
			}

			if (i == texts.length - 1) {
				chunks.add(chunk.toString());
			}
		}

		return chunks;
	}

	private SentenceModel getSentenceModel() {
		try (InputStream is = getClass().getResourceAsStream("/opennlp/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin")) {
			if (is == null) {
				throw new RuntimeException("sentence model is invalid");
			}

			return new SentenceModel(is);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Integer> getEncodedTokens(String text) {
		Assert.notNull(text, "Text must not be null");
		return this.encoding.encode(text).boxed();
	}

}
