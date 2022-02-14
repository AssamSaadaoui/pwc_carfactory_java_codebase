package be.kdg.java2.carfactory_application.presentation.api;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.exceptions.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.presentation.controller.CarController;
import be.kdg.java2.carfactory_application.presentation.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.presentation.helper.ControllerHelper;
import be.kdg.java2.carfactory_application.services.CarService;
import be.kdg.java2.carfactory_application.services.EngineerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        carService.update(contribution);
//        engineerService.update(contributor); persist has cascading type
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{carId}/engineers")
//    @ResponseBody implicit
    public ResponseEntity<Object> addContributor(@RequestBody EngineerDTO dto, @PathVariable int carId) {
        var newEngineer = new Engineer(dto.getName(), dto.getTenure(), dto.getNationality());
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        car.addEngineer(newEngineer);
        newEngineer.addCar(car);
        try {
            engineerService.addEngineer(newEngineer);
        } catch (IOException | EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        dto.setId(newEngineer.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
