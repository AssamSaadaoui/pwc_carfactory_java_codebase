package be.kdg.java2.carfactory_application.presentation.controller;

import be.kdg.java2.carfactory_application.domain.*;
import be.kdg.java2.carfactory_application.exceptions.EntityAlreadyExistsException;
import be.kdg.java2.carfactory_application.exceptions.InvalidImageException;
import be.kdg.java2.carfactory_application.presentation.dto.CarDTO;
import be.kdg.java2.carfactory_application.presentation.dto.EngineerDTO;
import be.kdg.java2.carfactory_application.services.CarService;
import be.kdg.java2.carfactory_application.services.EngineerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/cars")
public class CarController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final EngineerService engineerService;
    private final CarService carService;


    @Autowired
    public CarController(EngineerService engineerService, CarService carService) {
        this.engineerService = engineerService;
        this.carService = carService;
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
        List<Car> cars = carService.findByModel(lookup);
        model.addAttribute("cars", cars);
        return "/cars/cars";
    }

    @GetMapping("/look")
    public String lookUpCars() {
        logger.info("Search page");
        return "/cars/search";
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
        Car carById = carService.findById(id);
        model.addAttribute("car", carById);
        logger.info("displaying " + carById.getModel() + "'s details...");
        return "/cars/cardetails";
    }

    //Update
    @GetMapping("/edit/{id}")
    public String editCarDetails(@PathVariable int id, Model model) {
        Car carById = carService.findById(id);
        model.addAttribute("colors", Color.values());
        model.addAttribute("car", carById);
        model.addAttribute("tradeMark", carById.getTradeMark());
        logger.debug("updating car with " + id);
        return "/cars/editcar";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable int id,
                            @Valid @ModelAttribute("car") CarDTO dto, BindingResult result,
                            @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        if (result.hasErrors()) {
            logger.error(result.toString());
            model.addAttribute("colors", Color.values());
            return "/cars/editcar";
        }
        Car changedCar = carService.findById(id);
        changedCar.setModel(dto.getModel());
        changedCar.setColor(dto.getColor());
        changedCar.setPrice(dto.getPrice());
        changedCar.setReleaseDate(dto.getReleaseDate());
        changedCar.setTradeMark(new TradeMark(dto.getTitle(), dto.getFounder(), dto.getLaunchYear()));
        logger.debug("editing the car with id: " + id);
        carService.update(changedCar, multipartFile);
        return "redirect:/cars/" + id;
    }

    //Create
    @GetMapping("/new")
    public String newCarForm(Model model) {
        model.addAttribute("carDTO", new CarDTO());
        model.addAttribute("engineerDTO", new EngineerDTO());
        model.addAttribute("colors", Color.values());
        model.addAttribute("engineers", engineerService.getAllEngineers());
        logger.info("show add car form...");
        return "/cars/carform";
    }

    //    A lot of the checks here i wanted to make at the service layer, but i wasn't so sure.
    @PostMapping("/new")
    public String processAddCar(@Valid @ModelAttribute("carDTO") CarDTO carDTO, BindingResult result,
                                @Valid @ModelAttribute("engineerDTO") EngineerDTO engineerDTO, BindingResult enResult, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (result.hasErrors()) {
            logger.error(result.toString());
            model.addAttribute("colors", Color.values());
            model.addAttribute("engineers", engineerService.getAllEngineers());
            model.addAttribute("isMainForm", true);
            //By default, the errors in the subform (engineer form) are muted (unless they occur)
            model.addAttribute("isSubForm", false);
            return "/cars/carform";
        }
        //The user has to at least add an engineer via form or existent engineers
        if (enResult.hasErrors() && (carDTO.getEngineersIds().isEmpty())) {
            logger.error(engineerDTO.toString());
            logger.debug("There wasn't any contributor/engineer added to the new car.");
            model.addAttribute("colors", Color.values());
            model.addAttribute("engineers", engineerService.getAllEngineers());
            //  Here errors in the subform occur, so we set it to true.
            model.addAttribute("isSubForm", true);
            return "/cars/carform";
        }

        TradeMark tradeMark = new TradeMark(carDTO.getTitle(), carDTO.getFounder(), carDTO.getLaunchYear());
        Car car = new Car(carDTO.getModel(), carDTO.getEngineSize(), carDTO.getPrice(), carDTO.getReleaseDate(), carDTO.getColor());
        //Add engineers from checkbox (contributors)
        carDTO.getEngineersIds().forEach(id -> {
            car.addEngineer(engineerService.findById(id));
            logger.debug("add the engineer to the car from checkbox");
        });
        //Engineer form would be discarded if wrong data was passed in at the same time with a contributor (engineer) being picked from checkbox
        if (!enResult.hasErrors()) {
            Engineer engineer = new Engineer(engineerDTO.getName(), engineerDTO.getTenure(), engineerDTO.getNationality());
            car.addEngineer(engineer);
            engineer.addCar(car);
            logger.debug("Adding new car from form to the engineer");
        }
        car.setTradeMark(tradeMark);
        tradeMark.addCar(car);
        logger.debug("processing the new car item creation...");
        carService.addCar(car, multipartFile);
        return "redirect:/cars/" + car.getId() + "?success=true";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCar(@PathVariable int id) {
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
}