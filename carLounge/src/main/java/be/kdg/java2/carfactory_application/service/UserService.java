package be.kdg.java2.carfactory_application.service;

import be.kdg.java2.carfactory_application.domain.User;
import be.kdg.java2.carfactory_application.exception.UserAlreadyExistException;
import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;

public interface UserService {
    User findUser(String username);

    User findUser(long userId);

    User registerNewUserAccount(UserViewModel userViewModel) throws UserAlreadyExistException;
}
