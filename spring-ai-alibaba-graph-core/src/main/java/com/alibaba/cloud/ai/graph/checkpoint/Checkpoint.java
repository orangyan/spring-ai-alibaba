
package com.alibaba.cloud.ai.graph.checkpoint;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class Checkpoint {

	private final String id;

	private Map<String, Object> state = null;

	private String nodeId = null;

	private String nextNodeId = null;

	public String getId() {
		return id;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public String getNodeId() {
		return nodeId;
	}

	public String getNextNodeId() {
		return nextNodeId;
	}

	/**
	 * create a copy of given checkpoint with a new id
	 * @param checkpoint value from which copy is created
	 * @return new copy with different id
	 */
	public static Checkpoint copyOf(Checkpoint checkpoint) {
		requireNonNull(checkpoint, "checkpoint cannot be null");
		return new Checkpoint(UUID.randomUUID().toString(), checkpoint.state, checkpoint.nodeId, checkpoint.nextNodeId);
	}

	@JsonCreator
	private Checkpoint(@JsonProperty("id") String id, @JsonProperty("state") Map<String, Object> state,
			@JsonProperty("nodeId") String nodeId, @JsonProperty("nextNodeId") String nextNodeId) {

		this.id = requireNonNull(id, "id cannot be null");
		this.state = requireNonNull(state, "state cannot be null");
		this.nodeId = requireNonNull(nodeId, "nodeId cannot be null");
		this.nextNodeId = requireNonNull(nextNodeId, "Checkpoint.nextNodeId cannot be null");

	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String id = UUID.randomUUID().toString();

		private Map<String, Object> state = null;

		private String nodeId = null;

		private String nextNodeId = null;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder state(OverAllState state) {
			this.state = state.data();
			return this;
		}

		public Builder state(Map<String, Object> state) {
			this.state = state;
			return this;
		}

		public Builder nodeId(String nodeId) {
			this.nodeId = nodeId;
			return this;
		}

		public Builder nextNodeId(String nextNodeId) {
			this.nextNodeId = nextNodeId;
			return this;
		}

		public Checkpoint build() {
			return new Checkpoint(id, state, nodeId, nextNodeId);
		}

	}

	public Checkpoint updateState(Map<String, Object> values, Map<String, KeyStrategy> channels) {

		return new Checkpoint(this.id, OverAllState.updateState(this.state, values, channels), this.nodeId,
				this.nextNodeId);
	}

	@Override
	public String toString() {
		return format("Checkpoint{ id=%s, nodeId=%s, nextNodeId=%s, state=%s }", id, nodeId, nextNodeId, state);
	}

}
