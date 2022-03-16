package be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel;

import be.kdg.java2.carfactory_application.security.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class UserViewModel {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;


    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
