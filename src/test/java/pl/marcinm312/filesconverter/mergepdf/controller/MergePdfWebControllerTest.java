package pl.marcinm312.filesconverter.mergepdf.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
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
class MergePdfWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getConverterPage_simpleCase_success() throws Exception {

		this.mockMvc.perform(get("/app/mergePdf/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("converterPage"));
	}

	@Test
	void convertMultipartFile_postWithoutFile_badRequest() throws Exception {

		this.mockMvc.perform(post("/app/mergePdf/"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("multipartException"));
	}

	@Test
	void convertMultipartFile_correctFile_fileConverted() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Images_pdfs_and_other_files.zip";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		byte[] responseBytes = this.mockMvc.perform(
						multipart("/app/mergePdf/")
								.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(header().string("Content-Disposition", "attachment; filename=\"Images_pdfs_and_other_files.pdf\""))
				.andReturn().getResponse().getContentAsByteArray();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBytes);
		PDDocument document = PDDocument.load(inputStream);
		int receivedNumberOfPages = document.getNumberOfPages();
		int expectedNumberOfPages = 10;
		Assertions.assertEquals(expectedNumberOfPages, receivedNumberOfPages);
	}

	@Test
	void convertMultipartFile_incorrectFileFormat_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect.webp";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		this.mockMvc.perform(
						multipart("/app/mergePdf/")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("converterPage"))
				.andExpect(model().attribute("result", "Błąd podczas konwertowania pliku: Nieprawidłowy format pliku. Dozwolone rozszerzenia: zip"));
	}

	@Test
	void convertMultipartFile_incorrectZipFile_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect_files.zip";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		this.mockMvc.perform(
						multipart("/app/mergePdf/")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("converterPage"))
				.andExpect(model().attribute("result", "Błąd podczas konwertowania pliku: Plik ZIP musi zawierać przynajmniej jeden plik o następującym rozszerzeniu: pdf"));
	}

	@Test
	void convertMultipartFile_multipartWithoutFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/app/mergePdf/"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void convertMultipartFile_multipartWithUnselectedFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/app/mergePdf/").file("file", new byte[0]))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(view().name("converterPage"))
				.andExpect(model().attribute("result", "Błąd podczas konwertowania pliku: Nie wybrano pliku"));
	}
}
