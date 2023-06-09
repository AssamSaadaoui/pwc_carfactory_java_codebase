package be.kdg.java2.carfactory_application.presentation.helper;

import be.kdg.java2.carfactory_application.domain.factory.Car;
import be.kdg.java2.carfactory_application.domain.factory.Engineer;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.EngineerController;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerHelper {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    public static void addContribution(Engineer changedEngineer, Car contribution) {
//        changedEngineer.addCar(contribution);
//        logger.debug("Adding " + contribution.getModel() + " as a contribution to the engineer");
//        contribution.addEngineer(changedEngineer);

        logger.debug("Adding engineer " + changedEngineer.getName() + " to " + contribution.getModel());
    }

    //REST specific
//    ctrl + q to get info of var
    public static ResponseEntity<Void> deleteRelation(int carId, int enId, EngineerService engineerService, CarService carService) {
        var contributor = engineerService.findById(enId);
        var contribution = carService.findById(carId);
        if (contributor == null || contribution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //      Deleting relationship
//        contributor.removeCar(contribution);
//        contribution.removeEngineer(contributor);
        //      Updating the repository
        carService.update(contribution);
//        engineerService.update(contributor); persist has cascading type
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
