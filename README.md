# RecipeBuddy
Simple, powerful recipe cataloguing for busy people.

## Features
 - Create, view, edit, and delete recipes
 - Rate recipes and keep track of how many times you've made them
 - Search recipes by keyword in name and tags
 - Filter search results by ingredients, utensils, and allergens
 - Rank search results by recipe rating, times cooked, and items missing
 - Timer in the recipe viewer (it works when you're not on the tab too!)
 - Automatically generated timer shortcuts for durations in recipes
 - Import and export recipes to/from JSON
 - Curated default view showing your most relevant and highly rated-recipes
 - Portable and cross-platform: Works on Windows, Linux, and MacOS
 - Free and open source

## How To Use
> RecipeBuddy requires Java 17 or newer. You can download it [here](https://www.oracle.com/java/technologies/downloads/#java17) if you don't already have it. (Linux users just need to install the package "openjdk-17-jre-headless")

To set up RecipeBuddy, just download the [latest release](https://github.com/crazyrecipes/RecipeBuddy/releases) and extract the .zip. The result should look something like this:
```
/ RecipeBuddy v1.0.0
  / RecipeBuddy-1.0.0.jar
  / recipebuddy-windows.bat
  / recipebuddy-linux.sh
  / data
    / ...
```
If you're on Windows, double-click recipebuddy-windows.bat to start the app. If you're on Linux or Mac, run recipebuddy-linux.sh.
A console should pop up with some log events showing the app has started. (you can also run the .jar directly with the following command from a terminal: "java -jar recipebuddy-1.0.0.jar").

Once the app has started, just open a browser to "http://localhost:8080", and you should see a welcome screen letting you know you're in the right place.
