
package com.alibaba.cloud.ai.graph;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class InitDataSerializer extends StdSerializer<GraphInitData> {

	public InitDataSerializer(Class<GraphInitData> t) {
		super(t);
	}

	/**
	 * Serializes the InitData object to JSON.
	 * @param initData the InitData object to serialize.
	 * @param jsonGenerator the JSON generator.
	 * @param serializerProvider the serializer provider.
	 * @throws IOException if an I/O error occurs.
	 */
	@Override
	public void serialize(GraphInitData initData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeStartObject();

		jsonGenerator.writeStringField("graph", initData.graph());
		jsonGenerator.writeStringField("title", initData.title());
		jsonGenerator.writeObjectField("args", initData.args());

		jsonGenerator.writeArrayFieldStart("threads");
		for (var thread : initData.threads()) {
			jsonGenerator.writeStartArray();
			jsonGenerator.writeString(thread.id());
			jsonGenerator.writeStartArray(thread.entries());
			jsonGenerator.writeEndArray();
			jsonGenerator.writeEndArray();
		}
		jsonGenerator.writeEndArray();

		jsonGenerator.writeEndObject();
	}

}
