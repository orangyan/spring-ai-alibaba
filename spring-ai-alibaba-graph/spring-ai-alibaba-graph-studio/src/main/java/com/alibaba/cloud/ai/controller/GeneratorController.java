
package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.service.generator.GraphProjectRequest;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.controller.ProjectGenerationController;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;

public class GeneratorController extends ProjectGenerationController<GraphProjectRequest> {

	public GeneratorController(InitializrMetadataProvider metadataProvider,
			ProjectGenerationInvoker<GraphProjectRequest> projectGenerationInvoker) {
		super(metadataProvider, projectGenerationInvoker);
	}

	@Override
	public GraphProjectRequest projectRequest(Map<String, String> headers) {
		GraphProjectRequest request = new GraphProjectRequest();
		BeanWrapperImpl bean = new BeanWrapperImpl(this);
		getMetadata().defaults().forEach((key, value) -> {
			if (bean.isWritableProperty(key)) {
				// We want to be able to infer a package name if none has been
				// explicitly set
				if (!key.equals("packageName")) {
					bean.setPropertyValue(key, value);
				}
			}
		});
		return request;
	}

}
