package org.crazyrecipes.recipebuddy.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NotFoundAdvice provides HTTP 404 responses when requests attempt to access
 *   or modify content that does not exist.
 */
@ControllerAdvice
public class NotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rateLimitHandler(RateLimitException e) {
        return e.getMessage();
    }
}
