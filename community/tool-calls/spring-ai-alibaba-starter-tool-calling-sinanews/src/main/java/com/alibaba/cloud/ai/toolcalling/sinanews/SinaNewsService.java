
package com.alibaba.cloud.ai.toolcalling.sinanews;

import com.alibaba.cloud.ai.toolcalling.common.JsonParseTool;
import com.alibaba.cloud.ai.toolcalling.common.WebClientTool;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author XiaoYunTao
 * @since 2024/12/18
 */
public class SinaNewsService implements Function<SinaNewsService.Request, SinaNewsService.Response> {

	private static final Logger logger = LoggerFactory.getLogger(SinaNewsService.class);

	private final WebClientTool webClientTool;

	private final JsonParseTool jsonParseTool;

	private final SinaNewsProperties properties;

	public SinaNewsService(JsonParseTool jsonParseTool, SinaNewsProperties properties, WebClientTool webClientTool) {
		this.webClientTool = webClientTool;
		this.properties = properties;
		this.jsonParseTool = jsonParseTool;
	}

	@Override
	public Response apply(Request request) {
		JsonNode rootNode = fetchDataFromApi();

		List<SinaNewsService.HotEvent> hotEvents = parseHotEvents(rootNode);

		logger.info("{} hotEvents: {}", this.getClass().getSimpleName(), hotEvents);
		return new Response(hotEvents);
	}

	protected JsonNode fetchDataFromApi() {
		try {
			String json = webClientTool.get("").block();

			return jsonParseTool.jsonToObject(json, JsonNode.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to fetch or parse Sinanews API data", e);
		}
	}

	protected List<HotEvent> parseHotEvents(JsonNode rootNode) {
		if (rootNode == null || !rootNode.has("data")) {
			throw new RuntimeException("Failed to retrieve or parse response data");
		}

		JsonNode hotList = rootNode.get("data").get("hotList");
		List<HotEvent> hotEvents = new ArrayList<>();

		for (JsonNode itemNode : hotList) {
			if (!itemNode.has("info")) {
				continue;
			}
			String title = itemNode.get("info").get("title").asText();
			hotEvents.add(new HotEvent(title));
		}

		return hotEvents;
	}

	public record HotEvent(String title) {
	}

	public record Request() {
	}

	public record Response(List<SinaNewsService.HotEvent> events) {
	}

}
