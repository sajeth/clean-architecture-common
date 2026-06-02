package io.github.sajeth.infrastructre.exceptions;

import io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author sajethperli
 */
@ControllerAdvice
public class ControllerExceptionHandler extends LoggerAdapter {

    public ControllerExceptionHandler() {
        super(ControllerExceptionHandler.class);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(ConversionFailedException ex) {
        warn("Type conversion failed: " + ex.getMessage());
        return new ResponseEntity<>("Invalid request parameter format", HttpStatus.BAD_REQUEST);
    }
}
