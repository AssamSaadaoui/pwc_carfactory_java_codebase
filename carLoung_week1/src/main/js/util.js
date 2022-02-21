//Window variables
const currentLocation = window.location.href;
const language = window.navigator.language;
//Checking for current url
const currentPath = `${currentLocation.includes('engineers') ? 'engineers' : 'cars'}`;


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


export {htmlToElement, redirect, language, currentPath}