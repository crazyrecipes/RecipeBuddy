package org.crazyrecipes.recipebuddy.error;

public class RateLimitException extends RuntimeException {
    public RateLimitException() {
        super("Connection throttled.");
    }
}
