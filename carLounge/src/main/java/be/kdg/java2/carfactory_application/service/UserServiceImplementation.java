package be.kdg.java2.carfactory_application.service;


import be.kdg.java2.carfactory_application.domain.user.Flag;
import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;
import be.kdg.java2.carfactory_application.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        user.setRole(Role.USER);
        user.setGender(userViewModel.getGender());
        user.setNationality(userViewModel.getNationality());
        user.setAge(userViewModel.getAge());
        user.setFlag(Flag.ENABLED);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void changeRole(long userId, Role role) {
//        var user = userRepository.findById(userId);

    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }


    private boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
