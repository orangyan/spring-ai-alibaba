
package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.api.GraphAPI;
import com.alibaba.cloud.ai.service.GraphService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("studio/api/graphs")
public class GraphAPIController implements GraphAPI {

	private final GraphService graphService;

	public GraphAPIController(GraphService graphService) {
		this.graphService = graphService;
	}

	@Override
	public GraphService graphService() {
		return graphService;
	}

}
