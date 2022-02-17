package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.services.CarService;
import be.kdg.java2.carfactory_application.services.EngineerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/engineers")
public class EngineerRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final EngineerService engineerService;
    private final CarService carService;

    public EngineerRestController(EngineerService engineerService, CarService carService) {
        this.engineerService = engineerService;
        this.carService = carService;
    }

    @GetMapping
    ResponseEntity<List<EngineerDTO>> searchEngineers(@RequestParam String lookup) {
        logger.debug("Looking up " + lookup);
        var engineers = engineerService.findByNameContains(lookup);
        if (engineers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var engineerDTOS = engineers.stream().map(engineer -> {
                    var dto = new EngineerDTO();
                    dto.setId(engineer.getId());
                    dto.setName(engineer.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(engineerDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/{enId}/cars/{carId}")
    public ResponseEntity<Void> deleteContribution(@PathVariable int enId, @PathVariable int carId) {
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
}
