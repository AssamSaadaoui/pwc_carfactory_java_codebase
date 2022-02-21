import {htmlToElement, redirect, language, currentPath} from "./util";
import validator from 'validator';
//Details page
const deleteButtons = document.querySelectorAll(".deleteBtn")
const addRelationButton = document.getElementById("addRelationButton")
const editButton = document.getElementById("editBtn")
const saveButton = document.getElementById("saveBtn")
const table = document.querySelector(".details table")


if (deleteButtons) {
    deleteButtons.forEach(btn => btn.addEventListener('click', async () => {
        //if current path is of cars detail page, hiddenInput would contain id of the engineer (contributor) and vice versa
        const hiddenInputContainingChildId = btn.nextElementSibling;
        if (currentPath === 'cars')
            await deleteContributor(hiddenInputContainingChildId)
        else if (currentPath === 'engineers')
            await deleteContribution(hiddenInputContainingChildId)
    }))
}
//Details page
if (addRelationButton)
    addRelationButton.addEventListener("click", async () => addContributor())
if (editButton)
    editButton.addEventListener("click", makeCarUpdatable)
if (saveButton) {
    saveButton.disabled = true
    saveButton.addEventListener("click", saveCarUpdate)
}


// DELETE section
//Delete an engineer from a car
const deleteContributor = async function (ele) {
    const carId = parseInt(document.getElementById("ownerEntityId").value)
    const engineerId = parseInt(ele.value)
    const titlesTd = document.getElementById("tdTitle")
    try {
        const response = await fetch(`/api/cars/${carId}/engineers/${engineerId}`, {
            headers: {
                Accept: "application/json"
            },
            method: 'DELETE'
        })
        if (!response.ok) {
            // get error message from body or default to response status
            const error = response.status;
            return Promise.reject(error);
        }
        if (response.status === 200) {
            // redirect(`/${objectStr}s?success=true`)
            ele.parentNode.parentNode.parentNode.parentNode.remove()
            if (titlesTd.nextElementSibling.children.length === 0)
                titlesTd.parentNode.remove()
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

//delete a car from an engineer's work
const deleteContribution = async function (ele) {
    const engineerId = parseInt(document.getElementById("ownerEntityId").value)
    const carId = parseInt(ele.value)
    const titlesTd = document.getElementById("tdTitle")
    try {
        const response = await fetch(`/api/engineers/${engineerId}/cars/${carId}`, {
            headers: {
                Accept: "application/json"
            },
            method: 'DELETE'
        })
        if (!response.ok) {
            // get error message from body or default to response status
            const error = response.status;
            return Promise.reject(error);
        }
        if (response.status === 200) {
            // redirect(`/${objectStr}s?success=true`)
            ele.parentNode.parentNode.parentNode.parentNode.remove()
            if (titlesTd.nextElementSibling.children.length === 0)
                titlesTd.parentNode.remove()
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}
const createHtml = (information, type) => `<div style="margin-top: 1rem" class="alert alert-${type} alert-dismissible fade show"
                 role="alert">
                ${information}
                <button style="padding: 0 5px;" type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>`

const createMessageElement = (responseMessage, type) => htmlToElement(createHtml(responseMessage, type))
// POST section
//Adding a contributor to a car (details page)
const addContributor = async function () {
    const name = document.getElementById("name").value
    const nationality = document.getElementById("nationality").value
    const tenureStr = document.getElementById("tenure").value
    const carId = parseInt(document.getElementById("ownerEntityId").value)
    //Data manipulation specific
    const miniFormOuterDivEl = document.querySelector(".addEngineer > p ~ div");
    const contributorsList = document.getElementById("contributors")
    //Response message

   // if(!validator.isNumeric(tenureStr))
   // {
   //     alert("Not a number")
   //     return
   // }else if(validator.isNumeric(nationality)){
   //     alert("Must be a string")
   //     return
   // }else if(validator.isNumeric(name)){
   //     alert()
   //     return
   // }

    const tenure = parseInt(tenureStr)
    try {
        let response = await fetch(`/api/cars/${carId}/engineers`, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                "Content-Type": 'application/json',
            },
            body: JSON.stringify({
                "name": name,
                "tenure": tenure,
                "nationality": nationality,
                "contributionIds": [carId]
            })
        })
        let data;
        if (response.status === 200) {
            //engineerDTOObject
            data = await response.json();
            contributorsList.innerHTML += `<li style="list-style: none;text-decoration: none;">
                                            <div class="row">
                                                <div class="col-md-6"><a href="${'/engineers/' + data.id}"
                                                    >- ${name}</a></div>
                                                <div class="col-md-6"><button style="padding: 0px 3px;"
                                                                         class="btn btn-outline-success bg-dark deleteBtn">Delete</button>
                                                    <input id="" type="hidden" value="${data.id}"></div>
                                            </div>
                                        </li>`
            miniFormOuterDivEl.parentElement.insertBefore(createMessageElement("Contributor successfully added.", "success"), miniFormOuterDivEl)
            // miniFormOuterDivEl.insertBefore(responseTextElement, miniFormOuterDivEl.firstChild);
            // window.top.location = window.top.location
        } else if (response.status === 409) {
            data = await response.text();
            // responseTextElement.innerText = `${data}`
            miniFormOuterDivEl.parentElement.insertBefore(createMessageElement(data, "danger"), miniFormOuterDivEl)
            // miniFormOuterDivEl.insertBefore(responseTextElement, miniFormOuterDivEl.firstChild);
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}
// -----------------------------------------------------------------------------------------------------------------------


//PUT section
function makeCarUpdatable(event) {
    const clickedButton = event.target;
    deleteButtons.forEach(btn => btn.disabled = true)
    clickedButton.disabled = true
    saveButton.disabled = false
    const rows = Array.from(table.rows);
    for (let i = 0; i < rows.length - 1; i++) {
        const td = rows[i].getElementsByTagName("td")[1]
        td.classList.add("form-control", "sample-code-frame")
        td.contentEditable = "true"
    }
    rows[0].getElementsByTagName("td")[1].focus();
}

async function saveCarUpdate(event) {
    const carId = parseInt(document.getElementById("ownerEntityId").value)
    const rows = Array.from(table.rows);
    const tds = (i) => rows[i].getElementsByTagName("td");
    //Parsing
    const engineSize = parseFloat(tds(1)[1].innerText.match(/([0-9]*[.])?[0-9]+/)[0]).toFixed(1)
    const price = parseInt(tds(5)[1].innerText.match(/\d+/)[0])
    let editedCarObj = {
        "model": `${tds(0)[1].innerText}`,
        "engineSize": engineSize,
        "title": `${tds(2)[1].innerText}`,
        "releaseDate": `${tds(3)[1].innerText}`,
        "price": price,
        "colorText": `${tds(4)[1].innerText}`
    }
    try {
        let response = await fetch(`/api/cars/${carId}`, {
            method: 'PUT',
            headers: {
                "Content-Type": 'application/json',
            },
            body: JSON.stringify(editedCarObj)
        })
        let data;
        if (response.status === 200) {
            rows.forEach(row => {
                let tData = row.getElementsByTagName("td")[1]
                tData.contentEditable = "false"
                tData.classList.remove("form-control", "sample-code-frame")
                tData.style.transition = "0.1s linear"
            })
            event.target.disabled = true
            editButton.disabled = false
            deleteButtons.forEach(btn => btn.disabled = false)
        } else if (response.status === 409) {
            data = await response.text();

        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}