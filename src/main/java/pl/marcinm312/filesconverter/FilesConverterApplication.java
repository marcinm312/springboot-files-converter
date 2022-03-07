package pl.marcinm312.filesconverter;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FilesConverterApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FilesConverterApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FilesConverterApplication.class, args);
	}

	@Bean
	public TomcatServletWebServerFactory tomcatEmbedded() {

		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

		tomcat.addConnectorCustomizers(connector -> {
			if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
				//-1 means unlimited
				((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
			}
		});

		return tomcat;
	}
}
