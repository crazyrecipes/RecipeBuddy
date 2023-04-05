package org.crazyrecipes.recipebuddy;

public class RecipeBuddyMap {
    /**
     * Maximum amount of requests that the API will process per minute.
     * Additional requests will generate HTTP 429 responses.
     * Example: 2400 (~40 requests per second)
     */
    public static final int MAX_REQUESTS_PER_MINUTE = 2400;

    /**
     * Logging level
     * 0: Info
     * 1: Warn
     * 2: Error
     */
    public static final int LOG_LEVEL = 0;
}
