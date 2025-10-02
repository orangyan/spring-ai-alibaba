
package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.graph.GraphInitData;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.param.GraphStreamParam;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Map;

public interface GraphService {

	Map<String, StateGraph> getStateGraphs();

	/**
	 * @return Init printable graph data (of MERMAID or PlantUML format) using StateGraph
	 * definition
	 */
	GraphInitData getPrintableGraphData(String name) throws GraphStateException;

	Flux<ServerSentEvent<String>> stream(String name, GraphStreamParam param, InputStream inputStream) throws Exception;

}
