const RECIPELIST_URL = "api/recipes"
const SEARCH_URL = "api/search"

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
    Update the greeting in the page based on the time of day.
*/
function greet() {
    const greeting = document.getElementById("GREETING");
    let d = new Date();
    let h = d.getHours();
    let intro;
    if(h < 12) {
        greeting.innerHTML = "&#127749; Good morning!";
    } else if(h < 17) {
        greeting.innerHTML = "&#9925; Good afternoon!";
    } else {
        greeting.innerHTML = "&#127769; Good evening!";
    }
}

/*
    Get and display recommended recipes.
    These are recipes that the user either has everything to make
    or is only missing a couple things. They will not contain the user's allergens.
    They are ranked by quality and relevance (what you have everything to make
    and what is highly rated will be displayed first).
*/
function display_relevant_recipes() {
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
        show_toast("Showing recommended recipes.");
        show_recipes(data);
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong fetching your recipes.");
    });  
}

/*
    Get and display all recipes if the user asks us to
*/
function display_all_recipes() {
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
        show_recipes(data);
        show_toast("Showing all recipes.");
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong fetching your recipes.");
    });  
}

/* ===== On page load: ===== */
greet();
display_relevant_recipes();