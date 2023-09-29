/**
 * list.js
 * Functionality for list.html
 */

/* URL for list of recipes */
const RECIPELIST_URL = "api/recipes"

/* URL for search queries */
const SEARCH_URL = "api/search"

/**
 * Displays given recipes 
 * @param {Object} recipe_json - List of recipes to display
 */
function showRecipes(recipe_json) {
    let formatted_result = "";
    for(let i in recipe_json) {
        let recipe = recipe_json[i];
        //console.log(recipe);
        let rating_disp = "";
        for(i = 0; i < recipe.rating; i++) {
            rating_disp += "&#9733 ";
            if(i > 5) { break; }
        }
        let recipeHTML = `
        <div class="recipepreview_container">
            <img class="recipepreview_photo" loading="lazy" src="api/photo/${recipe.id}">
            <div class="recipepreview_content">
                <h1>${recipe.name}</h1>
                <p>${rating_disp} - Cooked ${recipe.cooked} times.</p>
                <p>${recipe.desc}</p>
                <a href="viewer.html?id=${recipe.id}"><div class="inline_button">&#128196; Make It</div></a>
                <a href="editor.html?id=${recipe.id}"><div class="inline_button">&#128221; Edit</div></a>
            </div>
        </div>
        `
        formatted_result += recipeHTML;
    }
    document.getElementById("RECIPE_LIST").innerHTML = formatted_result;
    if(recipe_json.length == 0) {
        showToast("Couldn't find any matching recipes.");
    }
}

/**
 * Update the greeting in the page based on the time of day.
 */
function displayGreeting() {
    const greeting = document.getElementById("GREETING");
    let d = new Date();
    let h = d.getHours();
    if(h < 12) {
        greeting.innerHTML = "&#127749; Good morning!";
    } else if(h < 17) {
        greeting.innerHTML = "&#9925; Good afternoon!";
    } else {
        greeting.innerHTML = "&#127769; Good evening!";
    }
}

/**
 * Get and display recommended recipes.
 * These are recipes that the user either has everything to make
 * or is only missing a couple things. They will not contain the user's allergens.
 * They are ranked by quality and relevance (what you have everything to make
 * and what is highly rated will be displayed first).
 */
function displayRelevantRecipes() {
    console.log("Handling DISPLAY recommended recipes...");
    const SEARCH_JSON = `
    {
        "query": "",
        "ingredients": "SOME",
        "utensils": "SOME",
        "allergens": "BLOCK"
    }
    `
    fetch(SEARCH_URL, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: SEARCH_JSON,
    }).then(async response => {
        const data = await response.json();
        //console.log(data);
        showToast("Showing recommended recipes.");
        showRecipes(data);
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong fetching your recipes.");
    });  
}

/**
 * Get and display all recipes if the user asks us to
 */
function displayAllRecipes() {
    console.log("Handling DISPLAY all recipes...");
    const SEARCH_JSON = `
    {
        "query": "",
        "ingredients": "NONE",
        "utensils": "NONE",
        "allergens": "SHOW"
    }
    `
    fetch(SEARCH_URL, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: SEARCH_JSON,
    }).then(async response => {
        const data = await response.json();
        //console.log(data);
        showRecipes(data);
        showToast("Showing all recipes.");
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong fetching your recipes.");
    });  
}

/* ----- On page load: ----- */
displayGreeting();
displayRelevantRecipes();