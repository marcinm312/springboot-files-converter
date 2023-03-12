package pl.marcinm312.filesconverter.shared.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi publicApi() {

		return GroupedOpenApi.builder()
				.group("1. public-apis")
				.pathsToMatch("/api/**")
				.build();
	}
}
