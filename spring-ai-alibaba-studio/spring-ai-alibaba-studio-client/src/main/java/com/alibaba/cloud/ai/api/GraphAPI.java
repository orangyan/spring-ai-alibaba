
package com.alibaba.cloud.ai.api;

import java.util.List;
import java.util.Map;

import com.alibaba.cloud.ai.common.R;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.param.GraphStreamParam;
import com.alibaba.cloud.ai.service.GraphService;
import com.alibaba.cloud.ai.graph.GraphInitData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import reactor.core.publisher.Flux;

@Tag(name = "Graph", description = "the graph API")
public interface GraphAPI {

	GraphService graphService();

	@Operation(summary = "list graphs", description = "", tags = { "Graph" })
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	default R<List<String>> list() {
		Map<String, StateGraph> graphMap = graphService().getStateGraphs();
		List<String> graphNames = graphMap.keySet().stream().toList();
		return R.success(graphNames);
	}

	@Operation(summary = "init graph", description = "", tags = { "Graph" })
	@GetMapping(value = "init", produces = MediaType.APPLICATION_JSON_VALUE)
	default R<GraphInitData> init(String name) throws GraphStateException {
		return R.success(graphService().getPrintableGraphData(name));
	}

	@Operation(summary = "stream", description = "", tags = { "Graph" })
	@PostMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	default Flux<ServerSentEvent<String>> stream(HttpServletRequest request, String name, GraphStreamParam param)
			throws Exception {
		param.setSessionId(request.getSession().getId());
		return graphService().stream(name, param, request.getInputStream());
	}

}
