import {headers} from "./util";

const selectEls = document.querySelectorAll(".selectRole")
const displayEl = document.getElementById("roleDisplay")

async function changeRole(userId, editedUser, e) {
    try {
        let response = await fetch(`/api/users/${userId}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(editedUser)
        })
        let data;
        if (response.status === 200) {
            displayEl.innerText = e.target.value
        } else if (response.status === 409) {

        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

selectEls.forEach(el => el.addEventListener("change", async (e) => {
    const userId = e.target.previousElementSibling.value
    let editedUser = {
        "role": e.target?.value
    }
    await changeRole(userId, editedUser, e);
}))

