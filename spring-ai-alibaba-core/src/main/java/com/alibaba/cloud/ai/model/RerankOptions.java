package com.alibaba.cloud.ai.model;

import org.springframework.ai.model.ModelOptions;
import org.springframework.lang.Nullable;

/**
 * RerankOptions接口继承自ModelOptions，用于定义重新排序模型的配置选项.
 * 该接口主要作用是提供一个标准的配置框架，使得用户能够自定义重新排序模型的行为.
 * 通过实现这个接口，用户可以设置或获取模型的各种配置参数，以适应不同的应用场景.
 */
public interface RerankOptions extends ModelOptions {

	@Nullable
	String getModel();

	@Nullable
	Integer getTopN();

}
