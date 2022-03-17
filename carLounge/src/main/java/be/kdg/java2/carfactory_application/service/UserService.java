package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.user.Role;
import be.kdg.java2.carfactory_application.domain.user.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;

import java.util.List;

public interface UserService {
    User findUser(String username);

    User findUser(long userId);

    User registerNewUserAccount(UserViewModel userViewModel) throws UserAlreadyExistException;

    List<User> findAll();

    void changeRole(long userId, Role role);

    void save(User user);

    User findById(long id);

    void deleteUserById(long userId);
}
