package pl.marcinm312.filesconverter.converter;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.ToPdfParameterList;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.exception.BadRequestException;
import pl.marcinm312.filesconverter.utils.FileUtils;

import java.io.*;

@Component
public class WordToPdfConverter {

	private final ToPdfParameterList pdfParameterList;

	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

	public WordToPdfConverter() {

		pdfParameterList = new ToPdfParameterList();
		pdfParameterList.isEmbeddedAllFonts(true);
		pdfParameterList.setDisableLink(true);
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
		log.info("PDF file saved to stream");
		return outputStream.toByteArray();
	}

	private String getPdfFileName(String wordFileName) {
		String[] splitFileName = wordFileName.split("\\.");
		return splitFileName[0] + ".pdf";
	}
}
