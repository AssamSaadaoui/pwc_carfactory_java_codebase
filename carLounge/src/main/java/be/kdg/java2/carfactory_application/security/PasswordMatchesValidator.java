package be.kdg.java2.carfactory_application.security;

import be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel.UserViewModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserViewModel> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserViewModel userViewModel, ConstraintValidatorContext context) {
        return userViewModel.getPassword().equals(userViewModel.getMatchingPassword());
    }
}