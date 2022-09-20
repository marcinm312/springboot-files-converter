package pl.marcinm312.filesconverter.pdftopng.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class PdfToPngWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getConverterPage_simpleCase_success() throws Exception {

		this.mockMvc.perform(get("/app/pdfToPng/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("converterPage"));
	}

	@Test
	void convertMultipartFile_postWithoutFile_badRequest() throws Exception {

		this.mockMvc.perform(post("/app/pdfToPng/"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("multipartException"));
	}

	@Test
	void convertMultipartFile_correctFile_fileConverted() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Pdf_file.pdf";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		this.mockMvc.perform(
						multipart("/app/pdfToPng/")
								.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
	}

	@Test
	void convertMultipartFile_incorrectFileFormat_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect.webp";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		this.mockMvc.perform(
						multipart("/app/pdfToPng/")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("converterPage"))
				.andExpect(model().attribute("result", "Nieprawidłowy format pliku"));
	}

	@Test
	void convertMultipartFile_multipartWithoutFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/app/pdfToPng/"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void convertMultipartFile_multipartWithUnselectedFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/app/pdfToPng/").file("file", new byte[0]))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("converterPage"))
				.andExpect(model().attribute("result", "Nie wybrano pliku"));
	}
}
