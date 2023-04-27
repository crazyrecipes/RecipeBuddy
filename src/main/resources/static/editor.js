/* API url to post recipe to */
const RECIPES_URL = "api/recipes"

/* API url to get single recipe from */
const params = new URLSearchParams(decodeURI(window.location.search));
const RECIPE_URL = "api/recipe/" + params.get("id");
const PHOTO_URL = "api/photo/" + params.get("id");
const VIEW_URL = "viewer.html?id=" + params.get("id");
const MAX_UPLOAD_SIZE = 1024 * 1024;
var UPLOADED_PHOTO = "";

/* ===== TOAST MESSAGE ===== */
var toast_timeout;
/* Show toast message */
function show_toast(message) {
    clearTimeout(toast_timeout);
    var td = document.getElementById("TOAST_MESSAGE");
    td.innerHTML = message;
    td.className = "show";
    toast_timeout = setTimeout(hide_toast, 3000);
}
/* Hide toast message */
function hide_toast() {
    var td = document.getElementById("TOAST_MESSAGE");
    td.className = td.className.replace("show", "hide");
}
/* ===== END TOAST MESSAGE ===== */

/* Handle user upload of photos */
function handle_photo_upload(input) {
    if(input.files[0].size < MAX_UPLOAD_SIZE) {
        show_toast("Uploading photo...");
        const fr = new FileReader();
        fr.addEventListener("load", function(event) {
            fetch(PHOTO_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain",
                },
                body: event.target.result,
            }).then(async response => {
                if(!response.ok) {
                    throw new Error("POST request failed!");
                }
                show_toast("Photo uploaded.");
                document.getElementById("RECIPE_PHOTO").src = event.target.result;
            }).catch(error => {
                console.log(error);
                show_toast("Something went wrong uploading your photo.");
            });
        });
        fr.readAsDataURL(input.files[0]);
    } else {
        show_toast("Your photo is too large!");
    }
}

/* Fetch single recipe and display data in editor */
function display_recipe() {
    console.log(`Handle DISPLAY recipe ${RECIPE_URL}...`);
    fetch(RECIPE_URL, {
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

        recipe_ingredients = data.ingredients.join("\n");
        recipe_utensils = data.utensils.join("\n");
        recipe_steps = data.steps.join("\n");
        recipe_allergens = data.allergens.join("\n");
        recipe_tags = data.tags.join(",");

        document.getElementById("RECIPE_NAME").value = data.name;
        document.getElementById("RECIPE_DESC").value = data.desc;
        document.getElementById("RECIPE_PHOTO").src = PHOTO_URL;
        document.getElementById("RECIPE_COOKED").value = data.cooked;
        document.getElementById("RECIPE_INGREDIENTS").value = recipe_ingredients;
        document.getElementById("RECIPE_UTENSILS").value = recipe_utensils;
        document.getElementById("RECIPE_STEPS").value = recipe_steps;
        document.getElementById("RECIPE_TAGS").value = recipe_tags;
        
        document.getElementById("RECIPE_RATING_INPUT").value = data.rating;
        let rating_disp = "";
        for(i = 0; i < data.rating; i++) {
            rating_disp += "&#9733 ";
        }
        document.getElementById("RECIPE_RATING").innerHTML = rating_disp;
        
        var allergens_inputs = document.getElementsByName("ALLERGENS_INPUT");
        for(let i in data.allergens) {
            for(j = 0; j < allergens_inputs.length; j++) {
                if(allergens_inputs[j].value === data.allergens[i]) {
                    allergens_inputs[j].checked = true;
                }
            }
        }
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong displaying your recipe.");
    });
}

/* Save user edits to the recipe */
function do_edit() {
    show_toast("Saving your edits...");
    console.log("Handling EDIT recipe...");
    /* Get parameters */
    var recipe_name = document.getElementById("RECIPE_NAME").value;
    console.log(`..with name "${recipe_name}"`);
    
    var recipe_desc = document.getElementById("RECIPE_DESC").value;
    console.log(`..with desc "${recipe_desc}"`);

    var recipe_rating = parseInt(document.getElementById("RECIPE_RATING_INPUT").value);
    console.log(`..with rating "${recipe_rating}"`);

    var recipe_cooked = document.getElementById("RECIPE_COOKED").value;
    console.log(`..with cooked "${recipe_cooked}"`);
    
    var recipe_ingredients = document.getElementById("RECIPE_INGREDIENTS").value;
    console.log(`..with ingredients "${recipe_ingredients}"`);
    
    var recipe_utensils = document.getElementById("RECIPE_UTENSILS").value;
    console.log(`..with utensils "${recipe_utensils}"`);

    var recipe_steps = document.getElementById("RECIPE_STEPS").value;
    console.log(`..with steps "${recipe_steps}"`);

    /* Get allergens */
    var allergens_inputs = document.getElementsByName("ALLERGENS_INPUT");
    var recipe_allergens = "";
    for(i = 0; i < allergens_inputs.length; i++) {
        if(allergens_inputs[i].checked) {
            recipe_allergens = recipe_allergens.concat(allergens_inputs[i].value, "\n");
        }
    }
    console.log(`..with allergens "${recipe_allergens}"`);

    var recipe_tags = document.getElementById("RECIPE_TAGS").value;
    console.log(`..with tags "${recipe_tags}"`);

    const RECIPE_JSON = {
            "id": "0",
            "name": recipe_name,
            "desc": recipe_desc,
            "rating": recipe_rating,
            "cooked": recipe_cooked,
            "ingredients": recipe_ingredients.split('\n'),
            "utensils": recipe_utensils.split('\n'),
            "steps": recipe_steps.split('\n'),
            "allergens": recipe_allergens.split('\n'),
            "tags": recipe_tags.split(','),
    }
    
    console.log("[DEBUG] New recipe:");
    console.log(JSON.stringify(RECIPE_JSON));
    
    fetch(RECIPE_URL, {
        method: "PUT",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(RECIPE_JSON),
    }).then(async response => {
        if(!response.ok) {
            throw new Error("PUT request failed!");
        }
        const data = await response.json();
        //console.log(data);
        show_toast("Updated recipe.");
        window.location.href = VIEW_URL;
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong saving your changes.");
    });
}

/* Delete this recipe */
function do_delete() {
    if(!confirm("Are you sure you want to delete this recipe?")) {
        return;
    }
    console.log("Handling DELETE recipe...");
    fetch(RECIPE_URL, {
        method: "DELETE",
    }).then(async response => {
        if(!response.ok) {
            throw new Error("DELETE request failed!");
        }
        window.location.href = "list.html";
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong deleting your recipe.");
    })
}

var ratingInput = document.getElementById("RECIPE_RATING_INPUT");
var ratingStatus = document.getElementById("RECIPE_RATING");
ratingInput.addEventListener("input", function() {
    var output = "";
    for(i = 0; i < ratingInput.value; i++) {
        output += "&#9733 ";
    }
    ratingStatus.innerHTML = output;
})

/* ===== On page load ===== */
display_recipe();