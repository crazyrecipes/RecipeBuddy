/**
 * create.js
 * Functionality for create.html
 */

/* URL to fetch list of recipes */
const RECIPES_URL = "api/recipes"

/**
 * Creates a new, empty recipe and redirects to the editor. 
 */
function handleCreateRecipe() {
    showToast("Creating recipe...");
    console.log("Handling CREATE recipe...");
    
    /* Assemble recipe JSON */
    const RECIPE_JSON = {
            "id": "0",
            "name": "New Recipe",
            "desc": "",
            "rating": "3",
            "cooked": "0",
            "ingredients": [],
            "utensils": [],
            "allergens": [],
            "steps": [],
            "tags": [],
    }
    
    console.log("[DEBUG] New recipe:");
    console.log(JSON.stringify(RECIPE_JSON));

    /* Send request */
    fetch(RECIPES_URL, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(RECIPE_JSON),
    }).then(async response => {
        if(!response.ok) {
            throw new Error("POST request failed!")
        }
        const data = await response.json();
        //console.log(data);
        showToast("Recipe created. Taking you to the editor...");
        window.location.href = `editor.html?id=${data.id}`;
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong creating your recipe.");
    });
}

/* ----- On page load: ----- */
handleCreateRecipe();
