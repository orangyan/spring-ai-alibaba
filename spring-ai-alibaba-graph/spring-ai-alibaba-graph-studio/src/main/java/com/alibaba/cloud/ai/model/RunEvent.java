
package com.alibaba.cloud.ai.model;

/**
 * RunEvent defines a single event that occurred during the run. TODO complement
 */
public class RunEvent {

	private String eventType;

	public String getEventType() {
		return eventType;
	}

	public RunEvent setEventType(String eventType) {
		this.eventType = eventType;
		return this;
	}

}
