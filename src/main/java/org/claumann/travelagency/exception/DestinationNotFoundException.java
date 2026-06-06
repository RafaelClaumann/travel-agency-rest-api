package org.claumann.travelagency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DestinationNotFoundException extends RuntimeException {

    public DestinationNotFoundException(Long id) {
        super("Destination with ID " + id + " not found.");
    }
}
