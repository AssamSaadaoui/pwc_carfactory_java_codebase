package be.kdg.java2.carfactory_application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//400 client error
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid image format")
public class InvalidImageException extends RuntimeException {

    public InvalidImageException(String fileFormat, String msg) {
        super(msg + ":" + fileFormat);
    }

    public InvalidImageException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }

}
