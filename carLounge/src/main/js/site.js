//(s)css imports
import "../css/main.scss"
import "bootstrap-icons/font/bootstrap-icons.scss"

// Modal popup
// Get the modal
let modal = document.getElementById("myModal")
//
// // Get the button that opens the modal
let btn = document.getElementById("myBtn")
//
// // Get the <span> element that closes the modal
let span = document.getElementsByClassName("close")[0]


// When the user clicks the button, open the modal
btn.addEventListener("click", () => {
    modal.style.opacity="1"
    modal.style.display = "block"
})

// When the user clicks on <span> (x), close the modal
span.addEventListener("click", () => {
    modal.style.opacity="0"
    modal.style.display = "none"
})

// When the user clicks anywhere outside of the modal, close it
window.addEventListener("click", (e) => {
    if (e.target === modal) {
        modal.style.opacity="0"
        modal.style.transition= "opacity 0.6s;"
        modal.style.display = "none"
    }
})

