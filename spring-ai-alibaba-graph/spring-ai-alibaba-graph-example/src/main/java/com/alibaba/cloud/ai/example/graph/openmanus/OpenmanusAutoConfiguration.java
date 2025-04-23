

package com.alibaba.cloud.ai.example.graph.openmanus;

import com.alibaba.cloud.ai.example.graph.openmanus.tool.GoogleSearch;
import com.alibaba.cloud.ai.example.graph.openmanus.tool.PlanningTool;
import com.alibaba.cloud.ai.example.graph.openmanus.tool.PythonExecute;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * Tool configuration in this bean has not been enabled yet. Instead, we still as the
 * manual registration we do in the previous OpenManus implementation.
 */
@Configuration
public class OpenmanusAutoConfiguration {

	// @Bean(name = "browserUseFunction")
	// @ConditionalOnMissingBean
	// @Description(BrowserUseTool.description)
	// public BrowserUseTool browserUseFunction() {
	// return new BrowserUseTool();
	// }

	@Bean(name = "googleSearchFunction")
	@ConditionalOnMissingBean
	@Description(GoogleSearch.description)
	public GoogleSearch googleSearchFunction() {
		return new GoogleSearch();
	}

	@Bean(name = "planningToolFunction")
	@ConditionalOnMissingBean
	@Description(PlanningTool.description)
	public PlanningTool planningToolFunction() {
		return new PlanningTool();
	}

	@Bean(name = "pythonExecuteFunction")
	@ConditionalOnMissingBean
	@Description(PythonExecute.description)
	public PythonExecute pythonExecuteFunction() {
		return new PythonExecute();
	}

}
