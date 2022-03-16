package be.kdg.java2.carfactory_application.configuration;

import be.kdg.java2.carfactory_application.presentation.controller.converter.StringToLocalDateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("car-images", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir;
        uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:" + uploadPath + "/");
    }
}
