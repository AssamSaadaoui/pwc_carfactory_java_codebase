package be.kdg.java2.carfactory_application.configuration;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Color;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.CarViewModel;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.EngineerViewModel;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        Converter<Engineer, EngineerDTO> engineerConverter = new AbstractConverter<>() {
            @Override
            protected EngineerDTO convert(Engineer engineerSrc) {
                if (engineerSrc == null)
                    return null;
                EngineerDTO engineerDTO = new EngineerDTO();
                engineerDTO.setId(engineerSrc.getId());
                engineerDTO.setName(engineerSrc.getName());
                engineerDTO.setTenure(engineerSrc.getTenure());
                engineerDTO.setNationality(engineerSrc.getNationality());
                return engineerDTO;
            }
        };

        Converter<Car, CarDTO> carConverter = new AbstractConverter<>() {
            @Override
            protected CarDTO convert(Car carSrc) {
                if (carSrc == null)
                    return null;
                CarDTO carDTO = new CarDTO();
                carDTO.setId(carSrc.getId());
                carDTO.setModel(carSrc.getModel());
                carDTO.setColor(carSrc.getColor());
                carDTO.setPrice(carSrc.getPrice());
                carDTO.setEngineSize(carSrc.getEngineSize());
                carDTO.setReleaseDate(carSrc.getReleaseDate());
                return carDTO;
            }
        };
        modelMapper.addConverter(carConverter);
        modelMapper.addConverter(engineerConverter);
        return modelMapper;
    }
}
