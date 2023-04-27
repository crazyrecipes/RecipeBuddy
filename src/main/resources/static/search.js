/* API url to get search results from */
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

/* Show search results */
function show_results(results_json) {
    formatted_result = "";
    for(let i in results_json) {
        let recipe = results_json[i];
        //console.log(recipe);
        let rating_disp = "";
        for(i = 0; i < recipe.rating; i++) {
            rating_disp += "&#9733 ";
            if(i > 5) { break; }
        }
        var recipeHTML = `
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
    if(results_json.length > 0) {
        console.log("Displaying results...");
        show_toast(`Found ${results_json.length} results.`);
        document.getElementById("SEARCH_RESULTS").innerHTML = formatted_result;
    } else {
        console.log("No results!");
        show_toast("Your search didn't match any results.");
    }
}

/* Execute a search and display results */
function do_search() {
    console.log("Handling SEARCH...");

    /* Get search parameters */
    var search_text = document.getElementById("SEARCH_TEXT").value;
    console.log(`..for text "${search_text}"`)

    /* Get ingredients choice */
    var ingredients_inputs = document.getElementsByName("INGREDIENTS_INPUT");
    var search_ingredients = "SOME";
    for(i = 0; i < ingredients_inputs.length; i++) {
        if(ingredients_inputs[i].checked) {
            search_ingredients = ingredients_inputs[i].value;
            break;
        }
    }
    console.log(`..for ingredients "${search_ingredients}"`)

    /* Get allergens choice */
    var allergens_inputs = document.getElementsByName("ALLERGENS_INPUT");
    var search_allergens = "BLOCK";
    for(i = 0; i < allergens_inputs.length; i++) {
        if(allergens_inputs[i].checked) {
            search_allergens = allergens_inputs[i].value;
            break;
        }
    }
    console.log(`..for allergens "${search_allergens}"`)

    const SEARCH_JSON = `
    {
        "query": "${search_text}",
        "ingredients": "${search_ingredients}",
        "utensils": "${search_ingredients}",
        "allergens": "${search_allergens}"
    }
    `
    
    show_toast("Searching...");
    /* Send request and display results */
    console.log("Getting results...");
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
        show_results(data);
    }).catch(error => {
        console.log(error);
        show_toast("Something went wrong with your search.");
    });
}

/* ===== On page load: ===== */