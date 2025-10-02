
package com.alibaba.cloud.ai.graph.state.strategy;

import com.alibaba.cloud.ai.graph.KeyStrategy;

public class ReplaceStrategy implements KeyStrategy {

	@Override
	public Object apply(Object oldValue, Object newValue) {
		return newValue;
	}

}
