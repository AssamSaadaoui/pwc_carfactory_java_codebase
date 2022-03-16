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

//Util
function checkInput(username, found, element) {
    if (username === "") {
        element.innerText = ""
    } else if (!found) {
        element.innerText = "Username should be at least 4 characters long."
        element.style.color = "red"
    } else {
        element.innerText = ""
    }
}

const processUserName = () => {
    const username = usernameInput.value
    const found = namePattern.test(username)
    checkInput(username, found,userNameCheckMessageElement);
}
const processSecret = () => {
    const secret = secretInput.value
    const found = secretPattern.test(secret)
    checkInput(secret, found,secretCheckMessageElement);
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

// const saveData = (eve) => {
//     const namePass = namePattern.test(usernameInput.value)
//     const secretPass = secretPattern.test(secretInput.value)
//     const confirmationPass = secretConfirmation.value === ""
//     if((!namePass || !secretPass || !confirmationPass)){
//         eve.preventDefault()
//         submissionCheckMessageElement.innerText = "Please check your form before submission"
//         submissionCheckMessageElement.style.color="red"
//     }
// }
// submitBtn.addEventListener('click', saveData)

secretConfirmation.addEventListener('keyup', processConfirmation)
secretInput.addEventListener('keyup', processSecret)
usernameInput.addEventListener('keyup', processUserName)