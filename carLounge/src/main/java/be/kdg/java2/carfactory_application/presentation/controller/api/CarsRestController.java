package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.domain.factory.Color;
import be.kdg.java2.carfactory_application.domain.factory.Contribution;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.SimpleCarDTO;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.security.CustomUserDetails;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.ContributionService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarsRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final ModelMapper modelMapper;

    private final CarService carService;
    private final EngineerService engineerService;
    private final UserService userService;
    private final ContributionService contributionService;

    public CarsRestController(CarService carService, EngineerService engineerService, UserService userService, ModelMapper modelMapper, ContributionService contributionService) {
        this.carService = carService;
        this.engineerService = engineerService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.contributionService = contributionService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String lookup) {
        logger.debug("Looking up " + lookup);
        var cars = carService.findAllByModel(lookup);
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var carsDTOS = cars.stream()
                .map(car -> modelMapper.map(car, CarDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(carsDTOS, HttpStatus.OK);
    }

    @PatchMapping("/{carId}")
    public ResponseEntity<String> partialUpdateName(
            @RequestBody SimpleCarDTO partialUpdateCarDTO, @PathVariable("carId") int carId) {
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        car.setModel(partialUpdateCarDTO.getModel());
        car.setEngineSize(partialUpdateCarDTO.getEngineSize());
        car.setPrice(partialUpdateCarDTO.getPrice());
        carService.saveCarWithPartialUpdate(car);
        return ResponseEntity.ok("Car updated");
    }

    @GetMapping("/sort")
    public ResponseEntity<List<CarDTO>> orderByPrice(@RequestParam("order") String order,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
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
                    dto.setCurrentUser(userService.findUser(userDetails.getId()));
                    dto.setCreatedOn(car.getCreatedOn());
                    dto.setAuthor(car.getAuthor());
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
        contributionService.deleteContributionByEngineerId(enId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{carId}/engineers")
    public ResponseEntity<Object> addContributor(@Valid @RequestBody EngineerDTO dto, @PathVariable int carId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        var user = userService.findUser(userDetails.getId());
        var newEngineer = new Engineer(dto.getName(), dto.getTenure(), dto.getNationality());
        newEngineer.setAuthor(user);
        var car = carService.findById(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            contributionService.addContribution(new Contribution(car, newEngineer));
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
        var car = carService.findCarWithTradeMarkById(carId);
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
