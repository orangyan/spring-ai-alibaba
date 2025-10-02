
package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.model.ChatClient;
import com.alibaba.cloud.ai.param.ClientRunActionParam;
import com.alibaba.cloud.ai.vo.ChatClientRunResult;
import java.util.ArrayList;
import java.util.List;

public interface ChatClientDelegate {

	default List<ChatClient> list() {
		return new ArrayList<>();
	}

	default ChatClient get(String clientName) {
		return null;
	}

	default ChatClientRunResult run(ClientRunActionParam runActionParam) {
		return null;
	}

}
