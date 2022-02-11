package be.kdg.java2.carfactory_application.util;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.presentation.controller.EngineerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * This class handles the file uploads
 **/
public class FileUploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    /**
     * dir @Param can be path can be set based on what controller sends
     */

    public static void addImageToCar(Car car, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        getImage(file, "car");
        car.setImage(fileName);
    }

    public static void getImage(MultipartFile file, String dirInitial) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        CheckValidityUtil.checkImageFormat(fileName);
        logger.debug("Saving image to " + dirInitial + " directory");
        saveImage(dirInitial + "-images", fileName, file);
    }

    public static void saveImage(String uploadDir, String fileName,
                                 MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        logger.debug("Checking if upload path is valid");
        if (!Files.exists(uploadPath)) {
            logger.info("Creating directory" + uploadPath);
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }


}
