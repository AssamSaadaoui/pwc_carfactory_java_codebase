package be.kdg.java2.carfactory_application.presentation.controller.mvc;

import be.kdg.java2.carfactory_application.domain.user.Flag;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.LoginViewModel;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;
import be.kdg.java2.carfactory_application.security.CustomUserDetails;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login_success_handler")
    public String loginSuccessHandler(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      HttpServletRequest request, HttpServletResponse response
            , Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userService.findUser(userDetails.getUsername());
        if (user.getFlag() == Flag.DISABLED) {
            model.addAttribute("error", "Account is currently locked.");
            model.addAttribute("loginInfo", new LoginViewModel());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "/auth/login";
        }
        System.out.println("User login succeeded...");
        return "redirect:/?success&user=" + userDetails.getUsername() + "&new=false";
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
    public String registerUserAccount(@ModelAttribute("user") @Valid UserViewModel userViewModel,
                                      BindingResult result, HttpServletRequest request, Errors errors, Model model) {
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
        return "redirect:/?success=true&user=" + userViewModel.getUsername() + "&new=true";
    }
}
