package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.domain.Color;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarsRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;
    private final EngineerService engineerService;
    private final ModelMapper modelMapper;

    public CarsRestController(CarService carService, EngineerService engineerService, ModelMapper modelMapper) {
        this.carService = carService;
        this.engineerService = engineerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String lookup) {
        logger.debug("Looking up " + lookup);
        var cars = carService.findByModel(lookup);
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var carsDTOS = cars.stream()
                .map(car -> modelMapper.map(car, CarDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(carsDTOS, HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<CarDTO>> orderByPrice(@RequestParam("order") String order) {
        logger.debug("Ordering by price: " + order);
        var carsByAscOrder = carService.orderByPriceAsc();
        var carsByDescOrder = carService.orderByPriceDesc();
        var cars = (order.equals("asc")) ? carsByAscOrder : carsByDescOrder;
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var carsDTOS = cars.stream()
                .map(car -> {
                    var dto = new CarDTO();
                    dto.setId(car.getId());
                    dto.setModel(car.getModel());
                    dto.setEngineSize(car.getEngineSize());
                    dto.setPrice(car.getPrice());
                    dto.setImagePath(car.getImagePath());
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(carsDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/{carId}/engineers/{enId}")
    public ResponseEntity<Void> deleteContributor(@PathVariable int carId, @PathVariable int enId) {
        logger.debug("Deleting relation between car and engineer");
        var contributor = engineerService.findById(enId);
        var contribution = carService.findById(carId);
        if (contributor == null || contribution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //      Deleting relationship
        carService.removeContributorFromCar(contribution, contributor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{carId}/engineers")
    public ResponseEntity<Object> addContributor(@Valid @RequestBody EngineerDTO dto, @PathVariable int carId) {
        var newEngineer = new Engineer(dto.getName(), dto.getTenure(), dto.getNationality());
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carService.addContributorToCar(newEngineer, carId);
        try {
            engineerService.addContributionToEngineer(car, newEngineer);
            engineerService.addEngineer(newEngineer);
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        logger.debug("Adding engineer " + dto.getName() + " as a contributor to " + car.getModel());
        dto.getContributionIds().add(carId);
        dto.setId(newEngineer.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{carId}")
    public ResponseEntity<String> editCar(@RequestBody CarDTO carDTO, @PathVariable int carId) {
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Editing car " + car.getModel());
        car.getTradeMark().setTitle(carDTO.getTitle());
        car.setModel(carDTO.getModel());
        car.setColor(Color.valueOf(carDTO.getColorText().toUpperCase()));
        car.setPrice(carDTO.getPrice());
        car.setEngineSize(carDTO.getEngineSize());
        car.setReleaseDate(carDTO.getReleaseDate());
        try {
            carService.update(car);
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}