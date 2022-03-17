package be.kdg.java2.carfactory_application.presentation.controller.api;

import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.presentation.controller.api.dto.UserDTO;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.CarController;
import be.kdg.java2.carfactory_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> setRole(@RequestBody UserDTO userDTO, @PathVariable long userId) {
        var user = userService.findUser(userId);
        Role role = Role.valueOf(userDTO.getRole());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setRole(role);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
