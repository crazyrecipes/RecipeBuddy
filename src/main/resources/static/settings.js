const ALLERGENS_URL = "api/allergens";
const BACKUP_URL = "api/backup";
const RESTORE_URL = "api/restore";

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

/* Grab and display current user allergens */
function display_allergens() {
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
        show_toast("Something went wrong displaying your allergens.");
    });
}

/* Push user allergen changes */
function handle_allergen_change() {
    show_toast("Updating your allergens...");
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
        display_allergens();
        show_toast("Updated allergens.");
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong updating your allergens.");
    });
}

/* Back up recipes to JSON */
function handle_backup_recipes() {
    console.log("Handle BACKUP recipes...");
    show_toast("Backing up your recipes...");
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
        show_toast("Something went wrong backing up your recipes.");
    });
}

/* Restore recipes from JSON */
function handle_restore_recipes() {
    console.log("Handle RESTORE recipes...");
    var recipes_json = document.getElementById("RECIPE_RESTORE_TEXT").value;

    show_toast("Restoring your recipes...");
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
        show_toast("Restored your recipes successfully.");
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong restoring your recipes.");
    });
}

/* ===== On page load: ===== */
display_allergens();
