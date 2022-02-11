package be.kdg.java2.carfactory_application.presentation.helper;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import be.kdg.java2.carfactory_application.presentation.controller.EngineerController;
import be.kdg.java2.carfactory_application.services.CarService;
import be.kdg.java2.carfactory_application.services.EngineerService;
import be.kdg.java2.carfactory_application.util.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ControllerHelper {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    public static void addContribution(Engineer changedEngineer, Car contribution) {
        changedEngineer.addCar(contribution);
        logger.debug("Adding " + contribution.getModel() + " as a contribution to the engineer");
        contribution.addEngineer(changedEngineer);
        logger.debug("Adding engineer " + changedEngineer.getName() + " to " + contribution.getModel());
    }

    public static void deleteRelation(int carId, int enId, EngineerService engineerService, CarService carService) {
        var contributor = engineerService.findById(enId);
        var contribution = carService.findById(carId);
        //      Deleting relationship
        contributor.removeCar(contribution);
        contribution.removeEngineer(contributor);
        //      Updating the repository
        carService.update(contribution);
        engineerService.update(contributor);
    }
}
