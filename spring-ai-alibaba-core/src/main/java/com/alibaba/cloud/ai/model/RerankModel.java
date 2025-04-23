

package com.alibaba.cloud.ai.model;

import org.springframework.ai.model.Model;

/**
 * Title rerank model interface.<br>
 * Description Rerank model is used to calculate the semantic match between the list of
 * candidate documents and the user query .<br>
 *
 * @author yuanci.ytb
 * @since 1.0.0-M2
 */

public interface RerankModel extends Model<RerankRequest, RerankResponse> {

	@Override
	RerankResponse call(RerankRequest request);

}
