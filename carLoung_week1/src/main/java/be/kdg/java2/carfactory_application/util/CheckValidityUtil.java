package be.kdg.java2.carfactory_application.util;

import be.kdg.java2.carfactory_application.exceptions.InvalidImageException;
import be.kdg.java2.carfactory_application.presentation.controller.EngineerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


/**
 * This class checks the validity of any particular user entry.
 * If invalid, it throws a particular RuntimeException
 **/
public class CheckValidityUtil {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    public static void checkImageFormat(String fileName) {
        if (fileName.isEmpty()) {
            throw new InvalidImageException("Image is required if you'd like to add a new entity");
        }
        String fileNameLower = fileName.toLowerCase(Locale.ROOT);
        String format = fileName.substring(fileName.lastIndexOf("."));
        logger.debug("Checking the format of image");
        if (!(fileNameLower.endsWith(".png") || fileNameLower.endsWith(".jpeg") || fileName.endsWith(".jpg"))) {
            throw new InvalidImageException(format, "Invalid image format");
        }
    }
}
