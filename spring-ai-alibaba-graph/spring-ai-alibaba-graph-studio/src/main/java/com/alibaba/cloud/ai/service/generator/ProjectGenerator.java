
package com.alibaba.cloud.ai.service.generator;

import com.alibaba.cloud.ai.model.workflow.Workflow;
import com.alibaba.cloud.ai.model.chatbot.ChatBot;
import com.alibaba.cloud.ai.model.AppModeEnum;

import java.nio.file.Path;

/**
 * ProjectGenerator abstracts the project generation of a specific app type, e.g.
 * {@link Workflow}, {@link ChatBot}
 *
 * @author robocanic
 * @since 2025/05/18
 */
public interface ProjectGenerator {

	/**
	 * Whether the generator supports the given app mode
	 * @param appModeEnum see {@link AppModeEnum}
	 * @return true if supported
	 */
	Boolean supportAppMode(AppModeEnum appModeEnum);

	/**
	 * Generate the project, save into a local directory
	 * @param projectDescription see {@link GraphProjectDescription}
	 * @param projectRoot the path root which your file should generate
	 */
	void generate(GraphProjectDescription projectDescription, Path projectRoot);

}
