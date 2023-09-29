/*
 * toast.js
 * Functionality for showing and hiding toast messages
 */

var toast_timeout;

/* Show toast */
function showToast(message) {
    clearTimeout(toast_timeout);
    var td = document.getElementById("TOAST_MESSAGE");
    td.innerHTML = message;
    td.className = "show";
    toast_timeout = setTimeout(hideToast, 3000);
}

/* Hide toast */
function hideToast() {
    var td = document.getElementById("TOAST_MESSAGE");
    td.className = td.className.replace("show", "hide");
}
