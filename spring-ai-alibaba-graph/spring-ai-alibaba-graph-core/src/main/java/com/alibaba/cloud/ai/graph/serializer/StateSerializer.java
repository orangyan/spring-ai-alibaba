
package com.alibaba.cloud.ai.graph.serializer;

import com.alibaba.cloud.ai.graph.state.AgentStateFactory;

import java.io.IOException;
import java.util.Map;

public abstract class StateSerializer<T> implements Serializer<T> {

	private final AgentStateFactory<T> stateFactory;

	protected StateSerializer(AgentStateFactory<T> stateFactory) {
		this.stateFactory = stateFactory;
	}

	public final AgentStateFactory<T> stateFactory() {
		return stateFactory;
	}

	public final T stateOf(Map<String, Object> data) {
		return stateFactory.apply(data);
	}

	public final T cloneObject(Map<String, Object> data) throws IOException, ClassNotFoundException {
		return cloneObject(stateFactory().apply(data));
	}

}
