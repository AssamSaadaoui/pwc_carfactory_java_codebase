const usernameInput = document.getElementById("r-username")
const secretInput = document.getElementById("r-password")
const secretConfirmation = document.getElementById("confirmPassword")
const submitBtn = document.querySelector("button[type='submit']")

//ps for outputting feedback on inputs
const userNameCheckMessageElement = document.getElementById("username-check")
const secretCheckMessageElement = document.getElementById("password-check")
const confirmationCheckMessageElement = document.getElementById("pw-confirm-check")
const submissionCheckMessageElement = document.querySelectorAll("p")[0]

const namePattern = /^[A-z]+[A-z0-9]{3,}$/
// more than 5 chars
// at least one number
const secretPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,}$/

const processUserName = () => {
    const username = usernameInput.value
    const found = namePattern.test(username)
    if (username === "") {
        userNameCheckMessageElement.innerText = ""
    } else if (!found) {
        userNameCheckMessageElement.innerText = "Username should be at least 4 characters long."
        userNameCheckMessageElement.style.color = "red"
    } else {
        userNameCheckMessageElement.innerText = ""
    }
}

const processSecret = () => {

    const secret = secretInput.value;
    const found = secretPattern.test(secret);
    if (secret === "") {
        secretCheckMessageElement.innerText = ""
    } else if (!found) {
        secretCheckMessageElement.innerText = "Password should be at least 5 characters with one number and a capital letter"
        secretCheckMessageElement.style.color = "red"
    } else {
        secretCheckMessageElement.innerText = ""
    }
}

const processConfirmation = () => {
    const confirmation = secretConfirmation.value
    if (confirmation === "") {
        confirmationCheckMessageElement.innerText = ""
    } else if (secretInput.value !== confirmation) {
        confirmationCheckMessageElement.innerText = "Wrong password match"
        confirmationCheckMessageElement.style.color = "red"
    } else if (secretInput.value === confirmation) {
        confirmationCheckMessageElement.style.color = "green"
        confirmationCheckMessageElement.innerText = "Correct match"
    }
}


secretConfirmation.addEventListener('keyup', processConfirmation)
secretInput.addEventListener('keyup', processSecret)
usernameInput.addEventListener('keyup', processUserName)