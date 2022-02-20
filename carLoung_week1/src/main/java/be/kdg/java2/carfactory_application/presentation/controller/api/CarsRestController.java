package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.domain.Color;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.exceptions.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.services.CarService;
import be.kdg.java2.carfactory_application.services.EngineerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarsRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;
    private final EngineerService engineerService;

    public CarsRestController(CarService carService, EngineerService engineerService) {
        this.carService = carService;
        this.engineerService = engineerService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String lookup) {
        logger.debug("Looking up " + lookup);
        var cars = carService.findByModel(lookup);
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var carsDTOS = cars.stream()
                .map(car -> {
                    var dto = new CarDTO();
                    dto.setId(car.getId());
                    dto.setModel(car.getModel());
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(carsDTOS, HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<CarDTO>> orderByPrice(@RequestParam("order") String order) {
        logger.debug("Ordering by price: " + order);
        var carsByAscOrder = carService.orderByPriceAsc();
        var carsByDescOrder = carService.orderByPriceDesc();
        var cars = (order.equals("asc")) ? carsByAscOrder : carsByDescOrder;
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
        contributor.removeCar(contribution);
        contribution.removeEngineer(contributor);
        //      Updating the repository
        carService.update(contribution); //persist has cascading type (will cascade to engineer)
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{carId}/engineers")
    public ResponseEntity<Object> addContributor(@Valid @RequestBody EngineerDTO dto, @PathVariable int carId) {
        var newEngineer = new Engineer(dto.getName(), dto.getTenure(), dto.getNationality());
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        carService.addContributorToCar(newEngineer, carId);
        car.addEngineer(newEngineer);
        newEngineer.addCar(car);
        try {
            engineerService.addEngineer(newEngineer);
//            engineerService.addContributionToEngineer(car, newEngineer);
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        dto.getContributionIds().add(carId);
        dto.setId(newEngineer.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{carId}")
//    @ResponseBody implicit
    public ResponseEntity<Void> editCar(@RequestBody CarDTO carDTO, @PathVariable int carId) {
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        car.getTradeMark().setTitle(carDTO.getTitle());
        car.setModel(carDTO.getModel());
        car.setColor(Color.valueOf(carDTO.getColorText().toUpperCase()));
        car.setPrice(carDTO.getPrice());
        car.setEngineSize(carDTO.getEngineSize());
        car.setReleaseDate(carDTO.getReleaseDate());
        carService.update(car);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
