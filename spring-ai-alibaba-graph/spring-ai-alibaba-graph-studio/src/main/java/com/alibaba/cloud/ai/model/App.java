
package com.alibaba.cloud.ai.model;

import com.alibaba.cloud.ai.service.runner.RunnableModel;

public class App implements RunnableModel {

	private AppMetadata metadata;

	/**
	 * Spec has different implementations depending on the type of application. e.g.
	 * Workflow
	 */
	private Object spec;

	public App(AppMetadata metadata, Object spec) {
		this.metadata = metadata;
		this.spec = spec;
	}

	@Override
	public String id() {
		return metadata.getId();
	}

	public AppMetadata getMetadata() {
		return metadata;
	}

	public App setMetadata(AppMetadata metadata) {
		this.metadata = metadata;
		return this;
	}

	public Object getSpec() {
		return spec;
	}

	public App setSpec(Object spec) {
		this.spec = spec;
		return this;
	}

}
