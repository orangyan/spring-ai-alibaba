package com.alibaba.cloud.ai.model;

import org.springframework.ai.model.Model;

/**
 * RerankModel接口继承自Model接口，用于定义重新排序模型的通用行为和属性
 * 它特化了Model接口，指定了具体的请求和响应类型，即RerankRequest和RerankResponse
 * 这个接口的设计目的是为了提供一个标准的框架，用于处理重新排序任务，使得不同的重新排序模型能够以一致的方式被使用和管理
 *
 * @see Model
 * @see RerankRequest
 * @see RerankResponse
 */
public interface RerankModel extends Model<RerankRequest, RerankResponse> {

	@Override
	RerankResponse call(RerankRequest request);

}
