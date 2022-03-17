//Window variables
const currentLocation = window.location.href;
const language = window.navigator.language;
//Checking for current url
const currentPath = `${currentLocation.includes('engineers') ? 'engineers' : 'cars'}`;


const csrfToken = document.querySelector("meta[name=_csrf]").content;
const csrfHeader = document.querySelector("meta[name=_csrf_header]").content;

const headers = {
    Accept: "application/json",
    "Content-Type": "application/json"
};
headers[csrfHeader] = csrfToken;


function htmlToElement(html) {
    const template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

//Redirect upon selecting an option
function redirect(route) {
    window.location.href = route;
}


export {htmlToElement, redirect, language, currentPath, headers}