package org.crazyrecipes.recipebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The SpringBootApplication class used to start RecipeBuddy.
 */
@SpringBootApplication
public class RecipeBuddyApplication {
	/**
	 * Starts RecipeBuddy.
	 * @param args Unused as of now
	 */
	public static void main(String[] args) {
		SpringApplication.run(RecipeBuddyApplication.class, args);
	}

}
