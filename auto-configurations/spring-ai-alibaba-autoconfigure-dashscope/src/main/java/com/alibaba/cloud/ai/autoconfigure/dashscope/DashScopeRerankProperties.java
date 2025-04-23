

package com.alibaba.cloud.ai.autoconfigure.dashscope;

import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Title DashScope rerank properties.<br>
 * Description DashScope rerank properties.<br>
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */

@ConfigurationProperties(DashScopeRerankProperties.CONFIG_PREFIX)
public class DashScopeRerankProperties extends DashScopeParentProperties {

	/**
	 * Spring AI Alibaba configuration prefix.
	 */
	public static final String CONFIG_PREFIX = "spring.ai.dashscope.rerank";

	/**
	 * Default DashScope rerank model.
	 */
	public static final String DEFAULT_RERANK_MODEL = "gte-rerank";

	/**
	 * Top n rerank results.
	 */
	private Integer topN = 5;

	/**
	 * If need to return original documents.
	 */
	private Boolean returnDocuments = false;

	@NestedConfigurationProperty
	private DashScopeRerankOptions options = DashScopeRerankOptions.builder().withModel(DEFAULT_RERANK_MODEL).build();

	public DashScopeRerankOptions getOptions() {
		return this.options;
	}

	public void setOptions(DashScopeRerankOptions options) {
		this.options = options;
	}

	public Integer getTopN() {
		return topN;
	}

	public void setTopN(Integer topN) {
		this.topN = topN;
	}

	public Boolean getReturnDocuments() {
		return returnDocuments;
	}

	public void setReturnDocuments(Boolean returnDocuments) {
		this.returnDocuments = returnDocuments;
	}

}
