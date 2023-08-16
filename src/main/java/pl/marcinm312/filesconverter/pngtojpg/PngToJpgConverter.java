package pl.marcinm312.filesconverter.pngtojpg;

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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class PngToJpgConverter implements Converter {

	@Getter
	private final List<String> allowedExtensions = List.of("png");

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {

		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		try (InputStream inputStream = file.getInputStream()) {

			BufferedImage image = ImageIO.read(inputStream);
			BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			newImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

			log.info("Start to create JPG file");
			byte[] convertedFile = createJpgFile(newImage);
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "jpg");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku PNG do pliku JPG: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private byte[] createJpgFile(BufferedImage bufferedImage) throws FileException {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			ImageIO.write(bufferedImage, "jpg", outputStream);
			return outputStream.toByteArray();

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas zapisu pliku PNG: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}
}
