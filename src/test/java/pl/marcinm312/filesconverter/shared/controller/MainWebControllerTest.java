package pl.marcinm312.filesconverter.shared.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getMainPage_simpleCase_success() throws Exception {
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("mainPage"));
	}

	@ParameterizedTest
	@MethodSource("examplesOfStaticResources")
	void getStaticResource_simpleCase_success(String url, String contentType) throws Exception {

		mockMvc.perform(
						get(url))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType));
	}

	private static Stream<Arguments> examplesOfStaticResources() {

		return Stream.of(
				Arguments.of("/css/style.css", "text/css"),
				Arguments.of("/webjars/bootstrap/css/bootstrap.min.css", "text/css"),
				Arguments.of("/js/clearResult.js", "text/javascript")
		);
	}
}
