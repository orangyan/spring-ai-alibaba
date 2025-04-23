

package com.alibaba.cloud.ai.model;

import org.springframework.ai.model.ModelOptions;
import org.springframework.lang.Nullable;

/**
 * Title rerank options.<br>
 * Description rerank options.<br>
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */

public interface RerankOptions extends ModelOptions {

	@Nullable
	String getModel();

	@Nullable
	Integer getTopN();

}
