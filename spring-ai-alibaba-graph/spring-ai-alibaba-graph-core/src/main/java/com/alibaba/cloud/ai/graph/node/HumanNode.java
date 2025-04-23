
package com.alibaba.cloud.ai.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphInterruptException;

import java.util.Map;
import java.util.function.Function;

public class HumanNode implements NodeAction {

	// always or conditioned
	private String interruptStrategy;

	private Function<OverAllState, Boolean> interruptCondition;

	private Function<OverAllState, Map<String, Object>> stateUpdateFunc;

	public HumanNode() {
		this.interruptStrategy = "always";
	}

	public HumanNode(String interruptStrategy, Function<OverAllState, Boolean> interruptCondition) {
		this.interruptStrategy = interruptStrategy;
		this.interruptCondition = interruptCondition;
	}

	public HumanNode(String interruptStrategy, Function<OverAllState, Boolean> interruptCondition,
			Function<OverAllState, Map<String, Object>> stateUpdateFunc) {
		this.interruptStrategy = interruptStrategy;
		this.interruptCondition = interruptCondition;
		this.stateUpdateFunc = stateUpdateFunc;
	}

	//
	@Override
	public Map<String, Object> apply(OverAllState state) throws GraphInterruptException {
		var shouldInterrupt = interruptStrategy.equals("always")
				|| (interruptStrategy.equals("conditioned") && interruptCondition.apply(state));
		if (shouldInterrupt) {
			interrupt(state);
			Map<String, Object> data = Map.of();
			if (state.humanFeedback() != null) {
				if (stateUpdateFunc != null) {
					data = stateUpdateFunc.apply(state);
				}
				else {
					// todo, check and only update keys defined in state.
					data = state.updateState(state.humanFeedback().data());
				}
			}

			state.withoutResume();
			return data;
		}
		return Map.of();
	}

	private void interrupt(OverAllState state) throws GraphInterruptException {
		if (state.humanFeedback() == null || !state.isResume()) {
			throw new GraphInterruptException("interrupt");
		}
	}

	public String think(OverAllState state) {
		return state.humanFeedback().nextNodeId();
	}

}
