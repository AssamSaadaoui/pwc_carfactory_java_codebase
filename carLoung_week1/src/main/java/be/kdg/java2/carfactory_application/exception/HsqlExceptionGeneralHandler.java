package be.kdg.java2.carfactory_application.exception;

import org.hsqldb.HsqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HsqlExceptionGeneralHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = HsqlException.class)
    public ModelAndView
    defaultErrorHandler(HsqlException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception.getMessage());
        mav.setViewName("/errors/databaseerror");
        return mav;
    }
}

