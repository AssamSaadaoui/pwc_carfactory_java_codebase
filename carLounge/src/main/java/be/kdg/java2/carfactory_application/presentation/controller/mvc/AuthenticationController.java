package be.kdg.java2.carfactory_application.presentation.controller.mvc;

import be.kdg.java2.carfactory_application.domain.Color;
import be.kdg.java2.carfactory_application.domain.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.LoginViewModel;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(WebRequest request, Model model) {
        model.addAttribute("loginInfo", new LoginViewModel());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/auth/login";
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            UserViewModel userDto = new UserViewModel();
            model.addAttribute("user", userDto);
            return "/auth/register";
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserViewModel userViewModel, BindingResult result,
            HttpServletRequest request, Errors errors, Model model) {
        if (result.hasErrors()) {
            errors.getAllErrors().forEach(objectError -> {
                model.addAttribute("error", objectError.getDefaultMessage());
            });
            return "/auth/register";
        }
        try {
            User registered = userService.registerNewUserAccount(userViewModel);
            request.login(registered.getUsername(), userViewModel.getPassword());
        } catch (UserAlreadyExistException | ServletException exe) {
            model.addAttribute("message", exe.toString());
            return "/auth/register";
        }
        return "redirect:/?success=true&user=" + userViewModel.getUsername();
    }
}
