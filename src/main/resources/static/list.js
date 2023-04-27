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

/* ===== GREETING ===== */
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
/* ===== END GREETING ===== */

function show_recipes(recipe_json) {
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
        show_toast("Couldn't find any matching recipes.");
    }
}

/* Get and display recommended recipes */
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

/* Get and display all recipes */
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