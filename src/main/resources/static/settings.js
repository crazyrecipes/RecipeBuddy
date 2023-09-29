/**
 * settings.js
 * Functionality for settings.html
 */

/* URL for allergens */
const ALLERGENS_URL = "api/allergens";

/* URL for backing up recipes */
const BACKUP_URL = "api/backup";

/* URL for restoring recipes */
const RESTORE_URL = "api/restore";

/**
 * Grab and display current user allergens 
 */
function displayAllergens() {
    console.log("Handle DISPLAY allergens...");
    /* Send request */
    fetch(ALLERGENS_URL, {
        method: "GET",
        headers: {
            "Accept": "application/json",
        },
    }).then(async response => {
        if(!response.ok) {
            throw new Error("GET request failed!");
        }
        const data = await response.json();
        //console.log(data);
        
        var allergens_inputs = document.getElementsByName("ALLERGENS_INPUT");
        for(let i in data) {
            for(j = 0; j < allergens_inputs.length; j++) {
                if(allergens_inputs[j].value === data[i]) {
                    allergens_inputs[j].checked = true;
                }
            }
        }
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong displaying your allergens.");
    });
}

/**
 * Push user allergen changes
 */
function updateAllergens() {
    showToast("Updating your allergens...");
    console.log("Handle UPDATE allergens...");
    var allergens_inputs = document.getElementsByName("ALLERGENS_INPUT");
    var user_allergens = [];
    for(i = 0; i < allergens_inputs.length; i++) {
        if(allergens_inputs[i].checked) {
            user_allergens.push(allergens_inputs[i].value);
        }
    }
    console.log(`Updated allergens: "${user_allergens}"`);
    
    /* Send request */
    fetch(ALLERGENS_URL, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(user_allergens),
    }).then(async response => {
        if(!response.ok) {
            throw new Error("POST request failed!");
        }
        const data = await response.json();
        //console.log(data);

        /* Update display */
        displayAllergens();
        showToast("Updated allergens.");
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong updating your allergens.");
    });
}

/**
 * Back up recipes to JSON
 */
function backupRecipes() {
    console.log("Handle BACKUP recipes...");
    showToast("Backing up your recipes...");
    /* Send request */
    fetch(BACKUP_URL, {
        method: "GET",
        headers: {
            "Accept": "application/json",
        },
    }).then(async response => {
        if(!response.ok) {
            throw new Error("GET request failed!");
        }
        const data = await response.json();
        //console.log(data);
        document.getElementById("RECIPE_BACKUP_TEXT").value = JSON.stringify(data);
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong backing up your recipes.");
    });
}

/**
 * Restore recipes from JSON
 */
function restoreRecipes() {
    console.log("Handle RESTORE recipes...");
    var recipes_json = document.getElementById("RECIPE_RESTORE_TEXT").value;

    showToast("Restoring your recipes...");
    /* Send request */
    fetch(RESTORE_URL, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: recipes_json,
    }).then(async response => {
        if(!response.ok) {
            throw new Error("GET request failed!");
        }
        //console.log(data);
        showToast("Restored your recipes successfully.");
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong restoring your recipes.");
    });
}

/* ----- On page load: ----- */
displayAllergens();
