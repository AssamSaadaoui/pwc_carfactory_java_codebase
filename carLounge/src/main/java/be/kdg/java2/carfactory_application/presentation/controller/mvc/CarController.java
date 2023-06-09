package be.kdg.java2.carfactory_application.presentation.controller.mvc;

import be.kdg.java2.carfactory_application.domain.factory.*;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.exception.InvalidImageException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.EngineerViewModel;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.CarViewModel;
import be.kdg.java2.carfactory_application.repository.ContributionRepository;
import be.kdg.java2.carfactory_application.security.CustomUserDetails;
import be.kdg.java2.carfactory_application.security.isAuthenticatedAsAdminOrManagerOrUserIsUserId;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.ContributionService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/cars")
public class CarController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final EngineerService engineerService;
    private final CarService carService;
    private final UserService userService;
    private final ContributionService contributionService;

    @Autowired
    public CarController(EngineerService engineerService,
                         CarService carService,
                         UserService userService,
                         ContributionService contributionService) {
        this.engineerService = engineerService;
        this.carService = carService;
        this.userService = userService;
        this.contributionService = contributionService;
    }

    //Read
    @GetMapping
    public String showAllCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        logger.info("displaying all cars...");
        return "/cars/cars";
    }

    @GetMapping("/search")
    public String searchCars(Model model, @RequestParam("lookup") String lookup) {
        logger.debug("Looking up " + lookup);
        var cars = new ArrayList<>(List.of(carService.findByModel(lookup)));
        model.addAttribute("cars", cars);
        return "/cars/cars";
    }

    @GetMapping("/order")
    public String orderByPrice(Model model, @RequestParam("order") String order) {
        logger.debug("Ordering by price: " + order);
        List<Car> carsByAscOrder = carService.orderByPriceAsc();
        List<Car> carsByDescOrder = carService.orderByPriceDesc();
        model.addAttribute("cars", ((order.equals("asc")) ? carsByAscOrder : carsByDescOrder));
        return "/cars/cars";
    }

    @GetMapping("/{id}")
    public String carDetails(@PathVariable int id, Model model) {
        Car carById = carService.findCarWithTradeMarkAndContributionsAndAuthorById(id);
        model.addAttribute("car", carById);
        logger.info("displaying " + carById.getModel() + "'s details...");
        return "/cars/cardetails";
    }

    //Update
    @GetMapping("/edit/{id}")
    public String editCarDetails(@PathVariable int id, Model model) {
        Car carById = carService.findCarWithTradeMarkAndContributionsById(id);
        model.addAttribute("colors", Color.values());
        model.addAttribute("car", carById);
        model.addAttribute("tradeMark", carById.getTradeMark());
        logger.debug("updating car with " + id);
        return "/cars/editcar";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable int id,
                            @Valid @ModelAttribute("car") CarViewModel carViewModel, BindingResult result,
                            @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        if (result.hasErrors()) {
            logger.error(result.toString());
            model.addAttribute("colors", Color.values());
            return "/cars/editcar";
        }
        Car changedCar = carService.findCarWithTradeMarkById(id);
        changedCar.setModel(carViewModel.getModel());
        changedCar.setColor(carViewModel.getColor());
        changedCar.setPrice(carViewModel.getPrice());
        changedCar.setReleaseDate(carViewModel.getReleaseDate());
        changedCar.setTradeMark(new TradeMark(carViewModel.getTitle(), carViewModel.getFounder(), carViewModel.getLaunchYear()));
        logger.debug("editing the car with id: " + id);
        carService.update(changedCar, multipartFile);
        return "redirect:/cars/" + id;
    }

    //Create
    @GetMapping("/new")
    public String newCarForm(Model model) {
        model.addAttribute("carDTO", new CarViewModel());
        model.addAttribute("engineerDTO", new EngineerViewModel());
        model.addAttribute("colors", Color.values());
        model.addAttribute("engineers", engineerService.getAllEngineers());
        logger.info("show add car form...");
        return "/cars/carform";
    }

    //    A lot of the checks here i wanted to make at the service layer, but i wasn't so sure.
    @PostMapping("/new")
    public String processAddCar(@Valid @ModelAttribute("carDTO") CarViewModel carViewModel, BindingResult result,
                                @Valid @ModelAttribute("engineerDTO") EngineerViewModel engineerViewModel,
                                BindingResult enResult, Model model, @RequestParam("image") MultipartFile multipartFile,
                                @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        var user = userService.findUser(userDetails.getId());
        if (result.hasErrors()) {
            handleMainFormValidation(result, model);
            return "/cars/carform";
        }
        //The user has to at least add an engineer via form or existent engineers
        if (enResult.hasErrors() && (carViewModel.getEngineersIds().isEmpty())) {
            handleMissingContributors(engineerViewModel, model);
            return "/cars/carform";
        }
        //Initializing the new car
        var tradeMark = new TradeMark(carViewModel.getTitle(), carViewModel.getFounder(), carViewModel.getLaunchYear());
        var car = new Car(carViewModel.getModel(), carViewModel.getEngineSize(), carViewModel.getPrice(),
                carViewModel.getReleaseDate(), carViewModel.getColor());
        //creating relation between td n car
        car.setTradeMark(tradeMark);
        tradeMark.addCar(car);
        car.setAuthor(user);
        carService.addCar(car, multipartFile);
        //Add engineers from checkbox (contributors)
        carViewModel.getEngineersIds().forEach(engineerId -> {
            contributionService.addContribution(new Contribution(car, engineerService.findById(engineerId)));
            logger.debug("add the engineer to the car from checkbox");
        });
        //Engineer form would be discarded if wrong data was passed in at the same time with a contributor (engineer) being picked from checkbox
        if (!enResult.hasErrors()) {
            handleNewEngineerCreation(engineerViewModel, user, car);
        }
        logger.debug("processing the new car item creation...");
        return "redirect:/cars/" + car.getId() + "?success=true";
    }

    @PostMapping("/delete/{id}")
    @isAuthenticatedAsAdminOrManagerOrUserIsUserId
    public String deleteCar(@PathVariable int id, @RequestParam("currentUserName") String currentUserName) {
        carService.deleteCar(carService.findById(id));
        logger.debug("deleting car with id: " + id);
        return "redirect:/cars?success=true";
    }

    @ExceptionHandler(InvalidImageException.class)
    public ModelAndView handleError(HttpServletRequest req, InvalidImageException exception) {
        logger.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("/errors/invalidimageformaterror");
        return modelAndView;
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ModelAndView handleError(HttpServletRequest req, EntityAlreadyExistsException exception) {
        logger.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("/errors/entityalreadyexists");
        return modelAndView;
    }

    //    Helper methods to increase readability
    private void handleNewEngineerCreation(EngineerViewModel engineerViewModel, User user, Car car) {
        Engineer engineer = new Engineer(engineerViewModel.getName(), engineerViewModel.getTenure(), engineerViewModel.getNationality());
        engineer.setAuthor(user);
        engineerService.addEngineer(engineer);
        contributionService.addContribution(new Contribution(car, engineer));
        logger.debug("Adding new car from form to the engineer");
    }

    private void handleMainFormValidation(BindingResult result, Model model) {
        logger.error(result.toString());
        model.addAttribute("colors", Color.values());
        model.addAttribute("engineers", engineerService.getAllEngineers());
        model.addAttribute("isMainForm", true);
        //By default, the errors in the subform (engineer form) are muted (unless they occur)
        model.addAttribute("isSubForm", false);
    }

    private void handleMissingContributors(EngineerViewModel engineerViewModel, Model model) {
        logger.error(engineerViewModel.toString());
        logger.debug("There wasn't any contributor/engineer added to the new car.");
        model.addAttribute("colors", Color.values());
        model.addAttribute("engineers", engineerService.getAllEngineers());
        //  Here errors in the subform occur, so we set it to true.
        model.addAttribute("isSubForm", true);
    }
}