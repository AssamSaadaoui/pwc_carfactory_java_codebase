package be.kdg.java2.carfactory_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User already exists")
public class UserAlreadyExistException extends Error {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
    @Override
    public String toString() {
        return super.getMessage();
    }
}
