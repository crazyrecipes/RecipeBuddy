const RECIPES_URL = "api/recipes"
            
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

/* 
    Creates a new, empty recipe and redirects to the editor. 
*/
function do_create() {
    show_toast("Creating recipe...");
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
        show_toast("Recipe created. Taking you to the editor...");
        window.location.href = `editor.html?id=${data.id}`;
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong creating your recipe.");
    });
}

/* ===== ON PAGE LOAD ===== */
do_create();