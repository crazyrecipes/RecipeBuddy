package org.crazyrecipes.recipebuddy.error;

/**
 * RateLimitException should be thrown if a request is throttled.
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException() {
        super("Too Many Requests");
    }
}
