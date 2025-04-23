

package com.alibaba.cloud.ai.toolcalling.sensitivefilter;

import com.alibaba.cloud.ai.toolcalling.common.CommonToolCallProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import static com.alibaba.cloud.ai.toolcalling.common.CommonToolCallConstants.TOOL_CALLING_CONFIG_PREFIX;
import static com.alibaba.cloud.ai.toolcalling.sensitivefilter.SensitiveFilterProperties.SENSITIVE_FILTER_PREFIX;

/**
 * Configuration properties for sensitive information filter
 *
 * @author Makoto
 */
@ConfigurationProperties(prefix = SensitiveFilterProperties.SENSITIVE_FILTER_PREFIX)
public class SensitiveFilterProperties extends CommonToolCallProperties {

	protected static final String SENSITIVE_FILTER_PREFIX = TOOL_CALLING_CONFIG_PREFIX + ".sensitivefilter";

	private String replacement = "***";

	private boolean filterPhoneNumber = true;

	private boolean filterIdCard = true;

	private boolean filterBankCard = true;

	private boolean filterEmail = true;

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public boolean isFilterPhoneNumber() {
		return filterPhoneNumber;
	}

	public void setFilterPhoneNumber(boolean filterPhoneNumber) {
		this.filterPhoneNumber = filterPhoneNumber;
	}

	public boolean isFilterIdCard() {
		return filterIdCard;
	}

	public void setFilterIdCard(boolean filterIdCard) {
		this.filterIdCard = filterIdCard;
	}

	public boolean isFilterBankCard() {
		return filterBankCard;
	}

	public void setFilterBankCard(boolean filterBankCard) {
		this.filterBankCard = filterBankCard;
	}

	public boolean isFilterEmail() {
		return filterEmail;
	}

	public void setFilterEmail(boolean filterEmail) {
		this.filterEmail = filterEmail;
	}

}
