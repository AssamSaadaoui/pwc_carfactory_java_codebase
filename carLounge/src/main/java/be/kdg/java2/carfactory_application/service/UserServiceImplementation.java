package be.kdg.java2.carfactory_application.service;


import be.kdg.java2.carfactory_application.domain.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;
import be.kdg.java2.carfactory_application.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User findUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findUser(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User registerNewUserAccount(UserViewModel userViewModel) throws UserAlreadyExistException {
        if (userExists(userViewModel.getUsername())) {
            throw new UserAlreadyExistException("There is an account with the username: "
                    + userViewModel.getUsername());
        }
        User user = new User();
        user.setUsername(userViewModel.getUsername());
        user.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
        return userRepository.save(user);
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
