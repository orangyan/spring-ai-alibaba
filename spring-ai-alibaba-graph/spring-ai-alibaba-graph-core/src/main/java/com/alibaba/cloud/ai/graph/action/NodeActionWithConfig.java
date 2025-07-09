
package com.alibaba.cloud.ai.graph.action;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;

import java.util.Map;

@FunctionalInterface
public interface NodeActionWithConfig {

	Map<String, Object> apply(OverAllState state, RunnableConfig config) throws Exception;

}
