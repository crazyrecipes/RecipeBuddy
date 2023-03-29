package org.crazyrecipes.recipebuddy.error;

/**
 * When a RateLimitException is thrown, the controller will return a HTTP 429.
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException() {
        super("Too Many Requests");
    }
}
