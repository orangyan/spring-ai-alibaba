
package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.common.ModelType;
import com.alibaba.cloud.ai.model.ChatModelConfig;
import com.alibaba.cloud.ai.param.ModelRunActionParam;
import com.alibaba.cloud.ai.vo.ChatModelRunResult;
import java.util.ArrayList;
import java.util.List;

public interface ChatModelDelegate {

	default List<ChatModelConfig> list() {
		return new ArrayList<>();
	}

	default ChatModelConfig getByModelName(String modelName) {
		return null;
	}

	default ChatModelRunResult run(ModelRunActionParam runActionParam) {
		return null;
	}

	default String runImageGenTask(ModelRunActionParam modelRunActionParam) {
		return null;
	}

	default ChatModelRunResult runImageGenTaskAndGetUrl(ModelRunActionParam modelRunActionParam) {
		return null;
	}

	default List<String> listModelNames(ModelType modelType) {
		return new ArrayList<>();
	}

}
