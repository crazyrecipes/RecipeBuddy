/**
 * pantry.js
 * Functionality for pantry.html
 */

/* URL to get and post ingredients */
const INGREDIENTS_URL = "api/ingredients"

/* URL to get and post utensils */
const UTENSILS_URL = "api/utensils"

/**
 * Remove an ingredient from the pantry
 */
function removeIngredient(name) {
    showToast("Removing ingredient...");
    console.log(`Handling REMOVE ingredient "${name}"`);

    /* Get current ingredients */
    fetch(INGREDIENTS_URL, {
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
        current_ingredients = [];
        for(let i in data) {
            current_ingredients.push(data[i]);
        }
        new_ingredients = current_ingredients.filter(function (a) {
            return a !== name;
        });
        console.log(`[DEBUG] New ingredients list: ${new_ingredients}`);

        /* Push list with removed ingredient */
        fetch(INGREDIENTS_URL, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(new_ingredients),
        }).then(async response => {
            if(!response.ok) {
                throw new Error("POST request failed.");
            }
            //console.log(data);

            /* Refresh display */
            displayIngredients();
            showToast("Removed ingredient.");
        }).catch(error => {
            console.log(error);
            showToast("Something went wrong removing this ingredient.");
        });

    }).catch(error => {
        console.log(error);
        showToast("Something went wrong removing this ingredient.");
    });
}

/**
 * Remove a utensil from the pantry
 */
function removeUtensil(name) {
    showToast("Removing utensil...");
    console.log(`Handling REMOVE utensil "${name}"`);

    /* Get current utensils */
    fetch(UTENSILS_URL, {
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
        current_utensils = [];
        for(let i in data) {
            current_utensils.push(data[i]);
        }
        new_utensils = current_utensils.filter(function (a) {
            return a !== name;
        });
        console.log(`[DEBUG] New utensils list: ${new_utensils}`);

        /* Push list with removed utensil */
        fetch(UTENSILS_URL, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(new_utensils),
        }).then(async response => {
            if(!response.ok) {
                throw new Error("POST request failed.");
            }
            //console.log(data);

            /* Refresh display */
            displayUtensils();
            showToast("Removed utensil.");
        }).catch(error => {
            console.log(error);
            showToast("Something went wrong removing this utensil.");
        });

    }).catch(error => {
        console.log(error);
        showToast("Something went wrong removing this utensil.");
    });
}

/**
 * Add an ingredient to the pantry
 */
function addIngredient() {
    let ingredient_name = document.getElementById("INGREDIENT_ADD_INPUT").value;
    if(ingredient_name.length < 1) { return; }
    showToast("Adding ingredient...");
    console.log(`Handling ADD ingredient "${ingredient_name}"`);

    /* Get current ingredients */
    fetch(INGREDIENTS_URL, {
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
        new_ingredients = [];
        for(let i in data) {
            new_ingredients.push(data[i]);
        }
        new_ingredients.push(ingredient_name);
        console.log(`[DEBUG] New ingredients list: ${new_ingredients}`);

        /* Push list with added ingredient */
        fetch(INGREDIENTS_URL, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(new_ingredients),
        }).then(async response => {
            if(!response.ok) {
                throw new Error("POST request failed.");
            }
            //console.log(data);

            /* Refresh display */
            displayIngredients();
            document.getElementById('INGREDIENT_ADD_INPUT').value = "";
            showToast("Added ingredient.");
        }).catch(error => {
            console.log(error);
            showToast("Something went wrong adding this ingredient.");
        });
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong adding this ingredient.");
    });
}

/**
 * Add a utensil to the pantry
 */
function addUtensil() {
    let utensil_name = document.getElementById("UTENSIL_ADD_INPUT").value;
    if(utensil_name.length < 1) { return; }
    showToast("Adding utensil...");
    console.log(`Handling ADD utensil "${utensil_name}"`);

    /* Get current utensils */
    fetch(UTENSILS_URL, {
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
        current_utensils = [];
        for(let i in data) {
            current_utensils.push(data[i]);
        }
        current_utensils.push(utensil_name);
        console.log(`[DEBUG] New utensils list: ${current_utensils}`);

        /* Push list with added utensil */
        fetch(UTENSILS_URL, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(current_utensils),
        }).then(async response => {
            if(!response.ok) {
                throw new Error("POST request failed.");
            }
            //console.log(data);

            /* Refresh display */
            displayUtensils();
            document.getElementById('UTENSIL_ADD_INPUT').value = "";
            showToast("Added utensil.");
        }).catch(error => {
            console.log(error);
            showToast("Something went wrong adding this utensil.");
        });

    }).catch(error => {
        console.log(error);
        showToast("Something went wrong adding this utensil.");
    });
}

/**
 * Get and display all ingredients
 */
function displayIngredients() {
    console.log("Handling DISPLAY ingredients...");
    fetch(INGREDIENTS_URL, {
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
        formatted_result = "";
        for(let i in data) {
            let ingredient = data[i];
            //console.log(ingredient);
            var ingredientHTML = `
            <div class="pantry_entry">
                ${ingredient}
                <button class="pantry_button" onClick="removeIngredient('${ingredient}')">X</button>
            </div>
            `
            formatted_result += ingredientHTML;
        }

        document.getElementById("INGREDIENT_LIST").innerHTML = formatted_result;
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong displaying your ingredients.");
    });
}

/**
 * Get and display all utensils
 */
function displayUtensils() {
    console.log("Handling DISPLAY utensils...");
    fetch(UTENSILS_URL, {
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
        formatted_result = "";
        for(let i in data) {
            let utensil = data[i];
            //console.log(utensil);
            var utensilHTML = `
            <div class="pantry_entry">
                ${utensil}
                <button class="pantry_button" onClick="removeUtensil('${utensil}')">X</button>
            </div>
            `
            formatted_result += utensilHTML;
        }

        document.getElementById("UTENSIL_LIST").innerHTML = formatted_result;
    }).catch(error => {
        console.log(error);
        showToast("Something went wrong displaying your utensils.");
    })
}

/* ===== On page load: ===== */
displayIngredients();
displayUtensils();