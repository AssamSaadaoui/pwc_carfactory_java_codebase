//Window variables
const currentLocation = window.location.href;
const language = window.navigator.language;
const currentPath = `${currentLocation.includes('engineers') ? 'engineer' : 'car'}`;
//Search cars elements
const searchFieldEl = document.getElementById("lookup");
const dataList = document.getElementById("carsList");
//Cars page
const toOrderCarList = document.getElementById("listOfCars")
const orderSelectEl = document.getElementById("order");
//Details page
const deleteBtns = document.querySelectorAll(".deleteBtn")
const addRelationButton = document.getElementById("addRelationButton")
const editButton = document.getElementById("editBtn")
const saveButton = document.getElementById("saveBtn")
const table = document.querySelector(".details table")

//Event listeners
if (currentPath === 'car' && orderSelectEl) //check location (orderSelect element only exists in cars page)
    orderSelectEl.addEventListener("change", async () => orderCars())
//
if ((currentPath === 'car' || currentPath === 'engineer') && searchFieldEl)
    searchFieldEl.addEventListener("keyup", async () => search());
//
if (deleteBtns) {
    deleteBtns.forEach(btn => btn.addEventListener('click', async () => {
        const hiddenInputContainingChildId = btn.nextElementSibling;
        if (currentPath === 'car')
            await deleteContributor(hiddenInputContainingChildId)
        else if (currentPath === 'engineer')
            await deleteContribution(hiddenInputContainingChildId)
    }))
}
////Details page
if (addRelationButton)
    addRelationButton.addEventListener("click", async () => addContributor())
if (editButton)
    editButton.addEventListener("click", toggleEditableForCar)
if (saveButton) {
    saveButton.disabled = true
    saveButton.addEventListener("click", saveUpdate)
}

//Helper methods
//Redirect upon selecting an option
function redirect(route) {
    window.location.href = route;
}


// GET section
//Search objects
const search = async function () {
    const lookupValue = searchFieldEl.value;
    if (lookupValue.length < 3) {
        dataList.innerHTML = ''
        return;
    }
    try {
        const response = await fetch(`/api/${currentPath}s?lookup=${lookupValue}`,
            {
                headers: {
                    Accept: "application/json"
                }
            })
        let data;
        if (response.status === 200) {
            data = await response.json();
        } else if (response.status === 204) {
            data = []
        } else {
            alert(`Received status code ${response.status}`);
        }
        processSearchData(data)
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

function processSearchData(objectsArray) {
    dataList.innerHTML = '';
    const map = new Map();
    objectsArray.forEach(obj => {
        dataList.innerHTML += `
           <option  value="${currentPath === 'engineer' ? obj.name : obj.model}">
        `;
        map.set(`${currentPath === 'engineer' ? obj.name : obj.model}`, obj.id)
    })
    searchFieldEl.addEventListener("change", () => {
        map.forEach((value, key) => {
            if (key === searchFieldEl.value) redirect(`${currentPath}s/${value}`)
        })
    })
}


//Order cars by price
const orderCars = async function () {
    const orderValue = orderSelectEl.value;
    try {
        const response = await fetch(`/api/cars/sort?order=${orderValue}`,
            {
                headers: {
                    Accept: "application/json"
                }
            })
        let data;
        if (response.status === 200) {
            data = await response.json();
        } else if (response.status === 204) {
            data = []
        } else {
            alert(`Received status code ${response.status}`);
        }
        processOrderData(data)
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

//Process ordered data
function processOrderData(carsArray) {
    toOrderCarList.innerHTML = ''
    carsArray.forEach(car => {
        toOrderCarList.innerHTML += `
        <div class="col-md-4 item">
          <div class="card cardanim animated goUp" style="width: 18rem;">
                    <img id="car" src="${car.imagePath}""
                         class="card-img-top" th:alt-title="${car.model}" alt="">
                    <div class="card-body">
                        <h5 class="card-title">${car.model}</h5>
                    </div>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">${language === 'fr' ? 'Mod\u00E8le' : 'Model'}: ${car.model}</li>
                        <li class="list-group-item">${language === 'fr' ? 'Moteur' : 'Engine'}: ${car.engineSize} litr</li>
                        <li class="list-group-item">${language === 'fr' ? 'Prix' : 'Price'}: ${car.price}$</li>
                    </ul>
                    <div class="card-body">
                        <a href="/cars/${car.id}" class="card-link">${language === 'fr' ? 'D\u00E9tails' : 'Details'}</a>
                        <a href="/cars/edit/${car.id}">${language === 'fr' ? 'Modifier' : 'Edit'}</a>
                        <a href="/cars/delete/${car.id}">${language === 'fr' ? 'Supprimer' : 'Delete'}</a>
                    </div>
          </div>
        </div>
        `
    })
}

// -----------------------------------------------------------------------------------------------------------------------


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
// -----------------------------------------------------------------------------------------------------------------------


// POST section
//Adding a contributor to a car (details page)
const addContributor = async function () {
    const name = document.getElementById("name")
    const nationality = document.getElementById("nationality").value
    const tenure = parseInt(document.getElementById("tenure").value)
    const carId = parseInt(document.getElementById("ownerEntityId").value)
    //Data manipulation specific
    const miniFormOuterDivEl = name.parentElement.parentElement.parentElement;
    const responseTextElement = document.createElement("p");
    const contributorsList = document.getElementById("contributors")
    try {
        let response = await fetch(`/api/cars/${carId}/engineers`, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                "Content-Type": 'application/json',
            },
            body: JSON.stringify({
                "name": `${name.value}`,
                "tenure": tenure,
                "nationality": `${nationality}`,
                "contributionIds": [carId]
            })
        })
        let data;
        if (response.status === 200) {
            //engineerDTOObject
            data = await response.json();
            responseTextElement.innerText = "Contributor successfully added!"
            responseTextElement.style.color = "green"
            contributorsList.innerHTML += `<li style="list-style: none;text-decoration: none;">
                                            <a href="${`/engineers/${data.id}`}"
                                              >- ${name.value}</a> <a style="padding: 0px 3px;"
                                                                                       class="btn btn-outline-success bg-dark deleteBtn">Delete</a>
                                            <input id="" type="hidden" value="">
                                        </li>`
            miniFormOuterDivEl.insertBefore(responseTextElement, miniFormOuterDivEl.firstChild);
            // window.top.location = window.top.location
        } else if (response.status === 409) {
            data = await response.text();
            responseTextElement.innerText = `${data}`
            responseTextElement.style.color = "red"
            miniFormOuterDivEl.insertBefore(responseTextElement, miniFormOuterDivEl.firstChild);
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}
// -----------------------------------------------------------------------------------------------------------------------


//PUT section
function toggleEditableForCar(event) {
    const clickedButton = event.target;
    deleteBtns.forEach(btn => btn.disabled = true)
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

async function saveUpdate(event) {
    const carId = parseInt(document.getElementById("ownerEntityId").value)
    const rows = Array.from(table.rows);
    const tds = (i) => rows[i].getElementsByTagName("td");
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
            deleteBtns.forEach(btn => btn.disabled = false)
        } else if (response.status === 409) {
            data = await response.text();

        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}