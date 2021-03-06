package pl.marcinm312.filesconverter.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void convertWordToPdf_postWithoutFile_badRequest() throws Exception {

		this.mockMvc.perform(post("/api/convertWordToPdf"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("multipartException"));
	}

	@ParameterizedTest(name = "{index} ''{1}''")
	@MethodSource("examplesOfGoodFiles")
	void convertWordToPdf_correctFiles_fileConverted(String fileName, String nameOfTestCase) throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + fileName;
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		this.mockMvc.perform(
						multipart("/api/convertWordToPdf")
								.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
	}

	private static Stream<Arguments> examplesOfGoodFiles() {

		return Stream.of(
				Arguments.of("Small_file.docx", "convertWordToPdf_smallDocxFile_fileConverted"),
				Arguments.of("Small_file.doc", "convertWordToPdf_smallDocFile_fileConverted")
		);
	}

	@Test
	void convertWordToPdf_incorrectFileFormat_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect.webp";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/convertWordToPdf")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nieprawid??owy format pliku", receivedErrorMessage);
	}

	@Test
	void convertWordToPdf_multipartWithoutFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/api/convertWordToPdf"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void convertWordToPdf_multipartWithUnselectedFile_errorMessage() throws Exception {

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/convertWordToPdf").file("file", new byte[0]))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nie wybrano pliku", receivedErrorMessage);
	}
}