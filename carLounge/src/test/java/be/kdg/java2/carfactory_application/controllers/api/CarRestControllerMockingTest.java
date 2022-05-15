package be.kdg.java2.carfactory_application.controllers.api;

import be.kdg.java2.carfactory_application.domain.factory.*;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.ContributionService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("seeding")
public class CarRestControllerMockingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private EngineerService engineerService;

    @MockBean
    private ContributionService contributionService;

    @Test
    @WithUserDetails("sami")
    public void creatingAContributionBetweenCarAndEngineerShouldReturnOk() throws Exception {
        //Arrange
        var classA = new Car("A-Class",
                2.0,
                32500,
                java.time.LocalDate.of(2019, 8, 31),
                Color.WHITE);
        given(carService.findById(1)).willReturn(classA);
        var engineer = new Engineer("Issam", 7, "Algerian");
        given(engineerService.findById(2)).willReturn(engineer);

        var contribution = new Contribution(classA, engineer);
        given(contributionService.findById(4))
                .willReturn(contribution);
        //Act
        mockMvc.perform(
                        post("/api/cars/{id}/engineers", 1)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(engineer))
                )
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("sami")
    public void creatingAContributionBetweenANullCarAndAnEngineerShouldReturnANotFound() throws Exception {
        //Arrange
        given(carService.findById(1)).willReturn(null);

        var engineer2 = new Engineer("Issam", 7, "Algerian");
        given(engineerService.findById(3)).willReturn(engineer2);

        //Act n Assert
        mockMvc.perform(
                        post("/api/cars/{id}/engineers", 1)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(engineer2))
                )
                .andExpect(status().isNotFound());
    }


    @Test
    @WithUserDetails("sami")
    public void deletingContributionBetweenCarAndEngineerShouldReturnOk() throws Exception {
        //Arrange
        var classA = new Car("A-Class",
                2.0,
                32500,
                LocalDate.of(2019, 8, 31),
                Color.WHITE);
        given(carService.findById(1)).willReturn(classA);

        var engineer = new Engineer("Issam", 7, "Algerian");
//        engineerService.addEngineer(engineer);
        given(engineerService.findById(2)).willReturn(engineer);

        var contribution = new Contribution(classA, engineer);
        given(contributionService
                .findContributionByEngIdandCarId(2, 1))
                .willReturn(contribution);
        //Act n Assert
        mockMvc.perform(
                        delete("/api/cars/{carId}/engineers/{enId}", 1, 2)
                                .with(csrf()))
                .andExpect(status().isOk());
        assertNull(contributionService
                .findContributionByEngIdandCarId(2, 1));

    }

    @Test
    @WithUserDetails("sami")
    public void deletingContributionBetweenANullCarAndEngineerShouldReturnNotFound() throws Exception {
        //Arrange
        given(carService.findById(1)).willReturn(null);
        var engineer = new Engineer("Issam", 7, "Algerian");
//        engineerService.addEngineer(engineer);
        given(engineerService.findById(2)).willReturn(engineer);

        var contribution = new Contribution(null, engineer);
        given(contributionService
                .findContributionByEngIdandCarId(2, 1))
                .willReturn(contribution);

        //Act n Assert
        mockMvc.perform(
                        delete("/api/cars/{carId}/engineers/{enId}", 1, 2)
                                .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithUserDetails("sami")
    public void deletingANonExistingContribution() throws Exception {
        //Arrange
        var classA = new Car("A-Class",
                2.0,
                32500,
                LocalDate.of(2019, 8, 31),
                Color.WHITE);
        given(carService.findById(1)).willReturn(classA);
        given(engineerService.findById(2)).willReturn(null);
        var contribution = new Contribution(classA, null);
        given(contributionService
                .findContributionByEngIdandCarId(2, 1))
                .willReturn(contribution);

        //Act n Assert
        mockMvc.perform(
                        delete("/api/cars/{carId}/engineers/{enId}", 1, 2)
                                .with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithUserDetails("sami")
    public void updatingACarShouldSucceedAndReturnGoodValues() throws Exception {
        // Arrange
        var carDto = new CarDTO();
        carDto.setId(111);
        carDto.setModel("A-Class");
        carDto.setEngineSize(2.0);
        carDto.setPrice(32500);
        carDto.setColorText("White");
        carDto.setReleaseDate(LocalDate.of(2019, 8, 31));
        carDto.setTitle("Mercedes");

        var carA = new Car("Bla Bla Bla",
                carDto.getEngineSize(),
                carDto.getPrice(),
                carDto.getReleaseDate(),
                carDto.getColor());
        carA.setTradeMark(new TradeMark("Benz", "Founder", 1992));

        given(carService.findCarWithTradeMarkById(111)).willReturn(carA);

        // Act & Assert
        mockMvc.perform(
                        put("/api/cars/{carId}", 111)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDto))
                )
                .andExpect(status().isOk());

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carService).update(captor.capture());
        var capturedCar = captor.getValue();

        assertEquals(carDto.getModel(), capturedCar.getModel());
        assertNotEquals(carDto.getColorText().toLowerCase(Locale.ROOT), "black".toLowerCase(Locale.ROOT));
//        assertNotEquals(carDto.getCreatedOn(), LocalDate.now());
    }

}

