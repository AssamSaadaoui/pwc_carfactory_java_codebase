package be.kdg.java2.carfactory_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//400 client error
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid image format")
public class EntityAlreadyExistsException extends Error {

    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }

}
