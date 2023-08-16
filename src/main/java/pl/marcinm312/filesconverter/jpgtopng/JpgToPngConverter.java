package pl.marcinm312.filesconverter.jpgtopng;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class JpgToPngConverter implements Converter {

	@Getter
	private final List<String> allowedExtensions = List.of("jpg", "jpeg");

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {

		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		try (InputStream inputStream = file.getInputStream()) {

			BufferedImage bufferedImage = ImageIO.read(inputStream);

			log.info("Start to create PNG file");
			byte[] convertedFile = createPngFile(bufferedImage);
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "png");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku JPG do pliku PNG: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private byte[] createPngFile(BufferedImage bufferedImage) throws FileException {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			ImageIO.write(bufferedImage, "png", outputStream);
			return outputStream.toByteArray();

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas zapisu pliku PNG: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}
}
