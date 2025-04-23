

package com.alibaba.cloud.ai.example.graph;

import com.alibaba.cloud.ai.example.graph.react.tool.weather.function.WeatherProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({ WeatherProperties.class })
@ComponentScan(basePackages = { "com.alibaba.cloud.ai" })
public class GraphApplication {

	public static void main(String[] args) {

		SpringApplication.run(GraphApplication.class, args);
	}

}
