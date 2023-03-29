package org.crazyrecipes.recipebuddy.error;

/**
 * When a RateLimitException is thrown, the controller will return a HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not Found");
    }
}
