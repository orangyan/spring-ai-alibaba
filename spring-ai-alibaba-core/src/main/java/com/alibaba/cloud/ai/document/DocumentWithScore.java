
package com.alibaba.cloud.ai.document;

import com.alibaba.cloud.ai.model.RerankResultMetadata;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.ModelResult;
import org.springframework.ai.model.ResultMetadata;

import java.util.Objects;

/**
 * DocumentWithScore 类用于表示带有评分的文档对象。
 * 该类实现了 ModelResult<Document> 接口，主要用于封装文档及其相关评分和元数据信息。
 *
 * <p>主要功能包括：
 * <ul>
 *   <li>存储文档对象（Document）</li>
 *   <li>存储文档评分（score）</li>
 *   <li>存储重排序结果元数据（RerankResultMetadata）</li>
 *   <li>提供构建器模式（Builder）以简化对象创建过程</li>
 * </ul>
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */
public class DocumentWithScore implements ModelResult<Document> {

	/**
	 * Score of document
	 */
	private Double score;

	/**
	 * document information
	 */
	private Document document;

	private RerankResultMetadata metadata;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setMetadata(RerankResultMetadata metadata) {
		this.metadata = metadata;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public Document getOutput() {
		return this.document;
	}

	@Override
	public ResultMetadata getMetadata() {
		return this.metadata;
	}

	public static final class Builder {

		private final DocumentWithScore documentWithScore;

		private Builder() {
			this.documentWithScore = new DocumentWithScore();
		}

		public Builder withScore(Double score) {
			this.documentWithScore.setScore(score);
			return this;
		}

		public Builder withDocument(Document document) {
			this.documentWithScore.setDocument(document);
			return this;
		}

		public Builder withMetadata(RerankResultMetadata metadata) {
			this.documentWithScore.setMetadata(metadata);
			return this;
		}

		public DocumentWithScore build() {
			return documentWithScore;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DocumentWithScore that = (DocumentWithScore) o;
		return Objects.equals(score, that.score) && Objects.equals(document, that.document);
	}

	@Override
	public int hashCode() {
		return Objects.hash(score, document);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DocumentWithScore{");
		sb.append("score=").append(score);
		sb.append(", document=").append(document);
		sb.append('}');
		return sb.toString();
	}

}
