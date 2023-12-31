package pl.marcinm312.filesconverter.imagestopdf.controller;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.marcinm312.filesconverter.shared.exception.FileException;

import java.io.ByteArrayInputStream;
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
class ImagesToPdfApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void convertMultipartFile_postWithoutFile_badRequest() throws Exception {

		this.mockMvc.perform(post("/api/imagesToPdf"))
				.andDo(print())
				.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	void convertMultipartFile_correctFile_fileConverted() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Images_pdfs_and_other_files.zip";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		byte[] responseBytes = this.mockMvc.perform(
						multipart("/api/imagesToPdf")
								.file(multipartFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(header().string("Content-Disposition", "attachment; filename=\"Images_pdfs_and_other_files.pdf\""))
				.andReturn().getResponse().getContentAsByteArray();

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBytes);
			 PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
			int receivedNumberOfPages = document.getNumberOfPages();
			int expectedNumberOfPages = 7;
			Assertions.assertEquals(expectedNumberOfPages, receivedNumberOfPages);
		} catch (Exception e) {
			throw new FileException(e.getMessage());
		}
	}

	@Test
	void convertMultipartFile_incorrectFileFormat_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect.webp";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/imagesToPdf")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nieprawidłowy format pliku. Dozwolone rozszerzenia: zip", receivedErrorMessage);
	}

	@Test
	void convertMultipartFile_incorrectZipFile_errorMessage() throws Exception {

		String path = "testfiles" + FileSystems.getDefault().getSeparator() + "Incorrect_files.zip";
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		File file = new File(path);
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), null, bytes);

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/imagesToPdf")
								.file(multipartFile))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Plik ZIP musi zawierać przynajmniej jeden plik o następującym rozszerzeniu: jpg, jpeg, tif, tiff, gif, bmp, png", receivedErrorMessage);
	}

	@Test
	void convertMultipartFile_multipartWithoutFile_errorMessage() throws Exception {

		this.mockMvc.perform(
						multipart("/api/imagesToPdf"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void convertMultipartFile_multipartWithUnselectedFile_errorMessage() throws Exception {

		String receivedErrorMessage = Objects.requireNonNull(this.mockMvc.perform(
						multipart("/api/imagesToPdf").file("file", new byte[0]))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResolvedException()).getMessage();

		Assertions.assertEquals("Nie wybrano pliku", receivedErrorMessage);
	}
}
