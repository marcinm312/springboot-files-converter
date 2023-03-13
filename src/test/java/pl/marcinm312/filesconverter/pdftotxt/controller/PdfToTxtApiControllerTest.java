package pl.marcinm312.filesconverter.pdftotxt.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PdfToTxtApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void convertMultipartFile_postWithoutFile_badRequest() throws Exception {

		this.mockMvc.perform(post("/api/pdfToTxt"))
				.andDo(print())
				.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	void convertMultipartFile_correctFile_fileConverted() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Pdf_file.pdf";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		String responseBody = this.mockMvc.perform(
						multipart("/api/pdfToTxt")
								.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andReturn().getResponse().getContentAsString();

		String textToCheck = "Lorem Ipsum is simply dummy text";
		Assertions.assertTrue(responseBody.contains(textToCheck));
	}

	@Test
	void convertMultipartFile_incorrectFileFormat_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect.webp";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/pdfToTxt")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nieprawidłowy format pliku. Dozwolone rozszerzenia: pdf", receivedErrorMessage);
	}

	@Test
	void convertMultipartFile_multipartWithoutFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/api/pdfToTxt"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void convertMultipartFile_multipartWithUnselectedFile_errorMessage() throws Exception {

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/pdfToTxt").file("file", new byte[0]))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nie wybrano pliku", receivedErrorMessage);
	}
}
