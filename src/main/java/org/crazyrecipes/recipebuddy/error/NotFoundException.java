package org.crazyrecipes.recipebuddy.error;

/**
 * NotFoundException should be thrown if a request attempts to access or
 * modify content that does not exist.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Instantiates a NotFoundException.
     */
    public NotFoundException() {
        super("Not Found");
    }
}
