package pl.marcinm312.filesconverter.converter;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.PrivateFontPath;
import com.spire.doc.ToPdfParameterList;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.exception.BadRequestException;
import pl.marcinm312.filesconverter.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WordToPdfConverter {

	private final ToPdfParameterList pdfParameterList;

	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

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
		pdfParameterList.setDisableLink(true);
		pdfParameterList.setEmbeddedFontNameList(fontNames);
		pdfParameterList.setPrivateFontPaths(fontPaths);
		pdfParameterList.setUsePSCoversion(true);
	}

	public ResponseEntity<ByteArrayResource> validateAndConvertFile(MultipartFile file) throws IOException {

		if (file == null || file.isEmpty()) {
			log.error("No file selected");
			throw new BadRequestException("Nie wybrano pliku");
		}

		String fileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", fileName);

		if (!(fileName.endsWith(".doc") || fileName.endsWith(".docx"))) {
			log.error("Incorrect file format");
			throw new BadRequestException("Nieprawidłowy format pliku");
		}

		InputStream inputStream = file.getInputStream();
		log.info("Start to convert file: {}", fileName);
		byte[] convertedFile = convertFile(inputStream);
		String newFileName = getPdfFileName(fileName);
		log.info("Converted file: {}", newFileName);

		return FileUtils.generateResponseWithFile(convertedFile, newFileName);
	}

	private byte[] convertFile(InputStream inputStream) throws IOException {

		Document doc = new Document();
		doc.loadFromStream(inputStream, FileFormat.Auto);
		log.info("Input stream loaded");
		doc.setJPEGQuality(100);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		doc.saveToStream(outputStream, pdfParameterList);
		outputStream.close();
		doc.close();
		log.info("PDF file saved to stream");
		return outputStream.toByteArray();
	}

	private String getPdfFileName(String wordFileName) {
		String[] splitFileName = wordFileName.split("\\.");
		return splitFileName[0].replace("—", "-") + ".pdf";
	}
}
