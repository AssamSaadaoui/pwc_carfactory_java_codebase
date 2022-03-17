package be.kdg.java2.carfactory_application.presentation.controller.mvc;

import be.kdg.java2.carfactory_application.domain.user.Flag;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(EngineerController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        logger.info("displaying all engineers...");
        return "/user/users";
    }

    @PostMapping("/disable/{id}")
    public String lockUser(@PathVariable long id) {
        var user = userService.findUser(id);
        user.setFlag(Flag.DISABLED);
        userService.save(user);
        logger.debug("Locked user with id: " + id);
        return "redirect:/users?success=true&flag=locked";
    }

    @PostMapping("/enable/{id}")
    public String unlockUser(@PathVariable long id) {
        var user = userService.findUser(id);
        user.setFlag(Flag.ENABLED);
        userService.save(user);
        logger.debug("Unlocked user with id: " + id);
        return "redirect:/users?success=true&flag=unlocked";
    }
}
