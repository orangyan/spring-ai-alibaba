
package com.alibaba.cloud.ai.service.dsl;

import com.alibaba.cloud.ai.model.Variable;
import com.alibaba.cloud.ai.model.workflow.NodeData;
import com.alibaba.cloud.ai.model.workflow.NodeType;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * NodeDataConverter defined the mutual conversion between specific DSL data and
 * {@link NodeData}
 */
public interface NodeDataConverter<T extends NodeData> {

	/**
	 * Judge if this converter support this node type
	 * @param nodeType {@link NodeType}
	 * @return true if support
	 */
	Boolean supportNodeType(NodeType nodeType);

	/**
	 * Parse DSL data to NodeData
	 * @param data DSL data
	 * @return converted {@link NodeData}
	 */
	T parseMapData(Map<String, Object> data, DSLDialectType dialectType);

	/**
	 * Dump NodeData to DSL map data
	 * @param nodeData {@link NodeData}
	 * @return converted DSL node data <strong>The returned Map must be
	 * modifiable</strong>
	 */
	Map<String, Object> dumpMapData(T nodeData, DSLDialectType dialectType);

	/**
	 * Generate a readable variable name prefix for this node, such as "http1", "llm2",
	 * and so on
	 * @param count
	 * @return friendly varName
	 */
	default String generateVarName(int count) {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " must implement generateVarName");
	}

	/**
	 * After parseMapData is complete and varName is injected, call: Used to override the
	 * default outputKey based on varName and refresh the list of outputs
	 * @param nodeData {@link NodeData}
	 * @param varName
	 */
	default void postProcess(T nodeData, String varName) {
	}

	/**
	 * Fetch the global state variable for this node (usually its list of outputs)
	 * @param nodeData {@link NodeData}
	 * @return Variable stream
	 */
	default Stream<Variable> extractWorkflowVars(T nodeData) {
		List<Variable> outs = nodeData.getOutputs();
		return outs == null ? Stream.empty() : outs.stream();
	}

}
