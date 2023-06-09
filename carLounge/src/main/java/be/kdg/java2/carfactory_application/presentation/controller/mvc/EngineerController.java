package be.kdg.java2.carfactory_application.presentation.controller.mvc;

import be.kdg.java2.carfactory_application.domain.factory.*;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.exception.InvalidImageException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.CarViewModel;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.EngineerViewModel;
import be.kdg.java2.carfactory_application.presentation.helper.ControllerHelper;
import be.kdg.java2.carfactory_application.repository.ContributionRepository;
import be.kdg.java2.carfactory_application.security.CustomUserDetails;
import be.kdg.java2.carfactory_application.service.CarService;
import be.kdg.java2.carfactory_application.service.ContributionService;
import be.kdg.java2.carfactory_application.service.EngineerService;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;


@Controller
@RequestMapping("/engineers")
public class EngineerController {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    private final CarService carService;
    private final EngineerService engineerService;
    private final UserService userService;
    private final ContributionService contributionService;


    public EngineerController(CarService carService,
                              EngineerService engineerService,
                              UserService userService,
                              ContributionService contributionService) {
        this.carService = carService;
        this.engineerService = engineerService;
        this.userService = userService;
        this.contributionService = contributionService;
    }

    //Read
    @GetMapping
    public String showAllEngineers(Model model) {
        model.addAttribute("engineers", engineerService.getAllEngineers());
        logger.info("displaying all engineers...");
        return "/engineers/engineers";
    }

    @GetMapping("/search")
    public String searchEngineers(Model model, @RequestParam("lookup") String lookup) {
        logger.debug("Looking up " + lookup);
        List<Engineer> engineers = engineerService.findByNameContains(lookup);
        model.addAttribute("engineers", engineers);
        return "/engineers/engineers";
    }

    @GetMapping("/filter")
    public String filterByTenure(Model model, @RequestParam("lookup") String lookup, @RequestParam("tenure") int tenure) {
        logger.debug("Looking up engineers by tenure :" + tenure);
        if (lookup.equals("eqt")) {
            List<Engineer> eqTenure = engineerService.findByTenure(tenure);
            model.addAttribute("engineers", eqTenure);
            return "/engineers/engineers";
        }
        List<Engineer> gtTenure = engineerService.findByTenureIsGreaterThanEqual(tenure);
        List<Engineer> lsTenure = engineerService.findByTenureIsLessThanEqual(tenure);
        model.addAttribute("engineers", (lookup.equals("grt")) ? gtTenure : lsTenure);
        return "/engineers/engineers";
    }


    @GetMapping("/{id}")
    public String engineerDetails(@PathVariable int id, Model model) {
        Engineer engineerById = engineerService.findEngineerWithContributionsAndAuthorById(id);
        model.addAttribute("engineer", engineerById);
        logger.debug("displaying " + engineerById.getName() + "'s details...");
        return "/engineers/engineerdetails";
    }


    //Update
    @GetMapping("/edit/{id}")
    public String editEngineerDetails(@PathVariable int id, Model model) {
        Engineer engineerById = engineerService.findEngineerWithContributionsAndAuthorById(id);
        List<Car> allCars = carService.getAllCars();
        model.addAttribute("engineer", engineerById);
        model.addAttribute("allCars", allCars);
        logger.info("showing edit form...");
        return "/engineers/engineereditform";
    }

    @PostMapping("/edit/{id}")
    public String updateEngineer(@PathVariable int id,
                                 @Valid @ModelAttribute("dto") EngineerViewModel engineerViewModel,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
            model.addAttribute("cars", carService.getAllCars());
            return "/engineers/engineereditform";
        }
        Engineer changedEngineer = engineerService.findById(id);
        engineerViewModel.getContributionIds().forEach((contributionId) -> {
            if (contributionId != null) {
                contributionService.addContribution(new Contribution(carService.findById(contributionId), changedEngineer));
            }
        });
        changedEngineer.setName(engineerViewModel.getName());
        changedEngineer.setTenure(engineerViewModel.getTenure());
        changedEngineer.setNationality(engineerViewModel.getNationality());
        logger.debug("editing the engineer with id: " + id);
        engineerService.update(changedEngineer);
        return "redirect:/engineers/" + id;
    }

    //Create
    @GetMapping("/new")
    public String newEngineerForm(Model model) {
        model.addAttribute("engineerDTO", new EngineerViewModel());
        model.addAttribute("carDTO", new CarViewModel());
//        to get a list of existent engineers n colors
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("colors", Color.values());
        logger.info("showing add engineer form...");
        return "/engineers/engineerform";
    }

    //    A lot of the checks here i wanted to make at the service layer, but i wasn't so sure.
    @PostMapping("/new")
    public String processAddEngineer(@Valid @ModelAttribute("engineerDTO") EngineerViewModel engineerViewModel,
                                     BindingResult result,
                                     @Valid @ModelAttribute("carDTO") CarViewModel carViewModel,
                                     BindingResult carResult,
                                     Model model,
                                     @RequestParam("image") MultipartFile multipartFile,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        User user = userService.findUser(userDetails.getId());
        if (result.hasErrors()) {
            handleMainFormValidation(result, model);
            return "/engineers/engineerform";
        }
        //The user has to at least add a contribution via form or existent cars (contributions has not to be empty)
        if (carResult.hasErrors() && (engineerViewModel.getContributionIds().isEmpty())) { //if contributions are empty
            handleMissingContributions(carResult, model);
            return "/engineers/engineerform";
        }
        Engineer engineer = new Engineer(engineerViewModel.getName(),
                engineerViewModel.getTenure(),
                engineerViewModel.getNationality());
        engineer.setAuthor(user);
        engineerService.addEngineer(engineer);
        //Add contributions from checkbox list
        engineerViewModel.getContributionIds().forEach((contributionId) -> {
            if (contributionId != null) {
                contributionService.addContribution(new Contribution(carService.findById(contributionId), engineer));
            }
        });
        //Add car to engineer if all attributes from form exist (because of image upload that i had to check)
        //Also, if Car form would be discarded if wrong data was passed in at the same time with a contribution (car) being picked from checkbox
        if (!carResult.hasErrors()) {
            handleNewCarCreation(carViewModel, multipartFile, user, engineer);
        }
        return "redirect:/engineers/" + engineer.getId() + "?success=true";
    }


    //Delete
    @PostMapping("/delete/{id}")
    public String deleteEngineer(@PathVariable int id) {
        engineerService.deleteEngineer(engineerService.findById(id));
        logger.debug("deleting engineer with id: " + id);
        return "redirect:/engineers?success=true";
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
    private void handleNewCarCreation(CarViewModel carViewModel, MultipartFile multipartFile, User user, Engineer engineer) throws IOException {
        //Car returned from the form (if engineer isn't persisted, it'd be cast away)
        TradeMark tradeMark = new TradeMark(carViewModel.getTitle(), carViewModel.getFounder(), carViewModel.getLaunchYear());
        Car car = new Car(carViewModel.getModel(),
                carViewModel.getEngineSize(), carViewModel.getPrice(),
                carViewModel.getReleaseDate(), carViewModel.getColor());
        car.setTradeMark(tradeMark);
        tradeMark.addCar(car);
        car.setAuthor(user);
        logger.debug("processing the new engineer item creation...");
        // When new car is added with image
        carService.addCar(car, multipartFile);
        // Then contribution is persisted (link between new car and existent engineer
        contributionService.addContribution(new Contribution(car, engineer));
    }

    private void handleMissingContributions(BindingResult carResult, Model model) {
        logger.error(carResult.toString());
        logger.debug("There wasn't any contribution add to the new engineer");
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("colors", Color.values());
        //Here errors in the subform occur, so we set it to true.
        model.addAttribute("isSubForm", true);
    }

    private void handleMainFormValidation(BindingResult result, Model model) {
        logger.error(result.toString());
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("colors", Color.values());
        model.addAttribute("isMainForm", true);
        //By default, the errors in the subform (car form) are muted (unless they occur)
        model.addAttribute("isSubForm", false);
    }
}
