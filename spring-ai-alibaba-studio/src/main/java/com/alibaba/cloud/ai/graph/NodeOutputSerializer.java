
package com.alibaba.cloud.ai.graph;

import java.io.IOException;

import com.alibaba.cloud.ai.graph.diagram.MermaidGenerator;
import com.alibaba.cloud.ai.graph.state.StateSnapshot;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeOutputSerializer extends StdSerializer<NodeOutput> {

	public NodeOutputSerializer() {
		super(NodeOutput.class);
	}

	/**
	 * Serializes a NodeOutput instance into JSON.
	 * @param nodeOutput the NodeOutput instance to serialize
	 * @param gen the JsonGenerator used to write JSON
	 * @param serializerProvider the provider that can be used to get serializers for
	 * other types
	 * @throws IOException if an I/O error occurs during serialization
	 */
	@Override
	public void serialize(NodeOutput nodeOutput, JsonGenerator gen, SerializerProvider serializerProvider)
			throws IOException {
		log.trace("NodeOutputSerializer start! {}", nodeOutput.getClass());
		gen.writeStartObject();
		if (nodeOutput instanceof StateSnapshot snapshot) {
			var checkpoint = snapshot.config().checkPointId();
			log.trace("checkpoint: {}", checkpoint);
			if (checkpoint.isPresent()) {
				gen.writeStringField("checkpoint", checkpoint.get());
			}
		}
		if (nodeOutput.isSubGraph()) {
			gen.writeStringField("node", MermaidGenerator.SUBGRAPH_PREFIX + nodeOutput.node());
		}
		else {
			gen.writeStringField("node", nodeOutput.node());

		}
		gen.writeObjectField("state", nodeOutput.state().data());
		gen.writeEndObject();
	}

}
