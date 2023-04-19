package org.crazyrecipes.recipebuddy.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceUpdateAdvice provides HTTP 500 responses to failed requests.
 */
@ControllerAdvice
public class ResourceUpdateAdvice {
    @ResponseBody
    @ExceptionHandler(ResourceUpdateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String resourceUpdateHandler(ResourceUpdateException e) {
        return e.getMessage();
    }
}
