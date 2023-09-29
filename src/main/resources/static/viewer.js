/**
 * viewer.js
 * Functionality for viewer.html
 */

const params = new URLSearchParams(decodeURI(window.location.search));

/* API url for this recipe */
const RECIPE_URL = "api/recipe/" + params.get("id");

/* API url to increment cooked counter with */
const COOK_URL = "api/cook/" + params.get("id");

/* API url for this recipe's photo */
const PHOTO_URL = "api/photo/" + params.get("id");

/* Timer interval */
var timer_interval;

/* Timer value (in seconds) */
var timer_value;

/* ----- Timer ----- */

/**
 * HH:MM:SS to s
 * @param {string} hms - Time in HH:MM:SS
 */
function hms_to_s(hms) {
    let h = parseInt(hms.split(":")[0]);
    let m = parseInt(hms.split(":")[1]);
    let s = parseInt(hms.split(":")[2]);
    return h * 3600 + m * 60 + s;
}

/**
 * Seconds to HH:MM:SS
 * @param {number} ts - Time in seconds 
 * @returns {string} - Time in HH:MM:SS
 */
function s_to_hms(ts) {
    let h = Math.floor(ts / 3600).toString().padStart(2, '0');
    let m = Math.floor((ts % 3600) / 60).toString().padStart(2, '0');
    let s = Math.floor((ts % 3600) % 60).toString().padStart(2, '0');
    return `${h}:${m}:${s}`;
}

/**
 * MM:SS to seconds
 * @param {string} ms - Time in MM:SS 
 * @returns {number} - Time in seconds
 */
function ms_to_s(ms) {
    let m = parseInt(ms.split(":")[0]);
    let s = parseInt(ms.split(":")[1]);
    return m * 60 + s;
}

/**
 * Check if a word is a valid time in HH:MM:SS
 * @param {string} word - Word to check 
 * @returns {boolean} - True if word is a valid time in HH:MM:SS
 */
function is_hms(word) {
    s_word = word.replace(/[^a-zA-Z0-9:]/g, '');
    let r = new RegExp("^[0-9]?[0-9]:[0-5][0-9]:[0-5][0-9]$");
    return r.test(s_word);
}

/**
 * Check if a word is a valid time in MM:SS
 * @param {string} word - Word to check 
 * @returns - True if word is a valid time in MM:SS
 */
function is_ms(word) {
    s_word = word.replace(/[^a-zA-Z0-9:]/g, '');
    let r = new RegExp("^[0-5]?[0-9]:[0-5][0-9]$");
    return r.test(s_word);
}

/**
 * Check if a word is a valid time
 * @param {string} word - Word to check
 * @returns {boolean} - True if word is a valid time in HH:MM:SS or MM:SS
 */
function is_time(word) {
    return is_hms(word) || is_ms(word);
}

/**
 * Time (HH:MM:SS or MM:SS) to seconds
 * @param {string} ts - Time to convert 
 * @returns {number} - Time in seconds (0 if invalid)
 */
function time_to_s(ts) {
    if(is_hms(ts)) {
        return hms_to_s(ts);
    }
    if(is_ms(ts)) {
        return ms_to_s(ts);
    }
    return 0;
}

/**
 * Sets the in-page timer to a given time in HH:MM:SS or MM:SS
 * @param {string} ts - Time (in HH:MM:SS or MM:SS) to set the timer to
 */
function set_timer(ts) {
    console.log("Setting timer... to " + ts);
    timer_value = time_to_s(ts);
    display_timer();
    showToast(`Set timer to ${ts}`);
}

/**
 * Updates the shown value in the in-page timer
 */
function display_timer() {
    document.getElementById("RECIPE_TIMER").value = s_to_hms(timer_value);
}

/**
 * Start the in-page timer
 */
function start_timer() {
    set_timer(document.getElementById("RECIPE_TIMER").value);
    timer_interval = setInterval(tick_timer, 1000);
    showToast("Started timer.");
}

/**
 * Stop the in-page timer
 */
function stop_timer() {
    clearInterval(timer_interval);
    showToast("Stopped timer.");
}

/**
 * Tick the in-page timer
 * (Plays a chime when the timer finishes)
 */
function tick_timer() {
    timer_value -= 1;
    if(timer_value < 1) {
        timer_value = 0;
        var audio = new Audio("media/timer_done.mp3");
        audio.play();
        showToast("Time's up!");
        window.alert("Time's up!");
        clearInterval(timer_interval);
    }
    display_timer();
}

/* ----- Actions ----- */

/**
 * Increment the recipe's cooked counter
 */
function increment_cooked() {
    console.log(`Handle COOK for ${COOK_URL}...`);
    fetch(COOK_URL, {
        method: "POST",
    }).then(async response => {
        if(!response.ok) {
            throw new Error("POST request failed!");
        }
        display_recipe();
    }).catch(error => {
        console.log(error);
        window.alert("Something went wrong displaying your recipe.");
    });
}

/**
 * Grab a single recipe and display it
 */
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

        /* Look for times in recipe steps */
        for(i in data.steps) {
            j = data.steps[i].split(" ");
            for(k in j) {
                if(is_time(j[k])) {
                    //console.log("[DEBUG] Steps Parser: Found a time.");
                    // TODO cleanup timer button insertion
                    data.steps[i] += `&nbsp<button class="timer_set_button" onclick="set_timer('${j[k]}')">Set Timer</button>`;
                }
            }
        }

        recipe_ingredients = data.ingredients.join("<br>");
        recipe_utensils = data.utensils.join("<br>");
        recipe_steps = data.steps.join("<br>");
        recipe_allergens = data.allergens.join("<br>");
        recipe_tags = data.tags.join(",");
        let rating_disp = "";
        for(i = 0; i < data.rating; i++) {
            rating_disp += "&#9733 ";
            if(i > 5) { break; }
        }
        
        var recipeHTML = `
            <img class="recipeviewer_photo" src="${PHOTO_URL}">
            <h1>${data.name}</h1>
            <p>${data.desc}</p>
            <p>${rating_disp} - Cooked ${data.cooked} times.</p>
            <div class="spacer"></div>
            <h2>&#129365; Ingredients</h2>
            <p>${recipe_ingredients}</p>
            <h2>&#129379; Utensils</h2>
            <p>${recipe_utensils}</p>
            <h2>&#9201; Steps</h2>
            <p>${recipe_steps}</p>
            <h2>&#127991; Tags</h2>
            <p>${recipe_tags}</p>
            <h2>&#9940; Allergens</h2>
            <p>${recipe_allergens}</p>
            <a href="editor.html?id=${data.id}"><div class="inline_button">&#128221; Edit this recipe</div></a>
            <button class="inline_button" onclick="increment_cooked()">&#128077; I cooked this recipe</button>
        `
        document.getElementById("RECIPE_CONTENT").innerHTML = recipeHTML;
    }).catch(error => {
        console.log(error);
        window.alert("Something went wrong displaying your recipe.");
    });
}

/* ----- On page load ----- */
display_recipe();
