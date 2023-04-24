package org.crazyrecipes.recipebuddy.error;

/**
 * ResourceUpdateException should be thrown if an attempt to modify a resource
 * specified by a request fails.
 */
public class ResourceUpdateException extends RuntimeException {
    /**
     * Instantiates a ResourceUpdateException.
     */
    public ResourceUpdateException() {
        super("Failed to update resource.");
    }
}
