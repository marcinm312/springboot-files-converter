package pl.marcinm312.filesconverter.wordtopdf;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.PrivateFontPath;
import com.spire.doc.ToPdfParameterList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class WordToPdfConverter implements Converter {

	@Getter
	private final List<String> allowedExtensions = List.of("doc", "docx");

	private final ToPdfParameterList pdfParameterList;

	public WordToPdfConverter(ResourcePatternResolver resourceResolver) throws IOException {

		Resource[] resources = resourceResolver.getResources("classpath:embeddedFonts/*");
		List<File> fontsDirectories = new ArrayList<>();

		if (resources.length > 0) {
			for (Resource resource : resources) {
				fontsDirectories.add(resource.getFile());
			}
		} else {
			File fontsMainDirectory = new File("embeddedFonts");
			File[] directoriesArray = fontsMainDirectory.listFiles(File::isDirectory);
			if (directoriesArray != null) {
				fontsDirectories = Arrays.asList(directoriesArray);
			}
		}

		List<String> fontNames = new ArrayList<>();
		List<PrivateFontPath> fontPaths = new ArrayList<>();

		for (File fontDirectory : fontsDirectories) {

			String fontName = fontDirectory.getName();
			fontNames.add(fontName);
			File[] fontFiles = fontDirectory.listFiles();

			if (fontFiles != null) {
				for (File fontFile : fontFiles) {
					fontPaths.add(new PrivateFontPath(fontName, fontFile.getPath()));
					log.info("Loaded font: \"{}\". Path: \"{}\"", fontName, fontFile.getPath());
				}
			}
		}

		pdfParameterList = new ToPdfParameterList();
		pdfParameterList.isEmbeddedAllFonts(true);
		pdfParameterList.setDisableLink(false);
		pdfParameterList.setEmbeddedFontNameList(fontNames);
		pdfParameterList.setPrivateFontPaths(fontPaths);
		pdfParameterList.setUsePSCoversion(true);
	}

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {

		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String fileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", fileName);

		Document doc = new Document();
		try (InputStream inputStream = file.getInputStream();
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			byte[] convertedFile = processInputStream(inputStream, outputStream, doc);
			String newFileName = FileUtils.getFileNameWithNewExtension(fileName, "pdf");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName, "application/pdf");

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku Word do formatu PDF: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		} finally {
			doc.close();
		}
	}

	private byte[] processInputStream(InputStream inputStream, ByteArrayOutputStream outputStream, Document doc) {

		doc.loadFromStream(inputStream, FileFormat.Auto);
		log.info("Input stream loaded");
		doc.setJPEGQuality(100);

		doc.saveToStream(outputStream, pdfParameterList);
		log.info("PDF file saved to stream");
		return outputStream.toByteArray();
	}
}
