//Window variables
const currentLocation = window.location.href;
const language = window.navigator.language;
const objectStr = `${currentLocation.includes('engineers') ? 'engineer' : 'car'}`;
//Search cars elements
const searchFieldEl = document.getElementById("lookup");
const dataList = document.getElementById("carsList");
//Order cars elements
const toOrderCarList = document.getElementById("listOfCars")
const orderSelectEl = document.getElementById("order");
//Details page delete button
const deleteBtns = document.querySelectorAll(".deleteBtn")
let addRelation = document.getElementById("addrelation")


//Event listeners
if (objectStr === 'car' && orderSelectEl) //check location (orderSelect element only exists in cars page)
    orderSelectEl.addEventListener("change", async () => orderCars())
//
if ((objectStr === 'car' || objectStr === 'engineer') && searchFieldEl)
    searchFieldEl.addEventListener("keyup", async () => search());
//
if (deleteBtns)
    deleteBtns.forEach(btn => btn.addEventListener('click', async () => deleteObject(btn.nextElementSibling)))
//
addRelation.addEventListener("click", async () => addContributor())

//Redirect upon selecting an option
function redirect(route) {
    window.location.href = route;
}

//Search objects
const search = async function () {
    let lookupValue = searchFieldEl.value;
    if (lookupValue.length < 3) {
        dataList.innerHTML = ''
        return;
    }
    try {
        let response = await fetch(`/api/${objectStr}s?lookup=${lookupValue}`,
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
           <option  value="${objectStr === 'engineer' ? obj.name : obj.model}">
        `;
        map.set(`${objectStr === 'engineer' ? obj.name : obj.model}`, obj.id)
    })
    searchFieldEl.addEventListener("change", () => {
        map.forEach((value, key) => {
            if (key === searchFieldEl.value) redirect(`${objectStr}s/${value}`)
        })
    })
}


//Order cars by price
const orderCars = async function () {
    let orderValue = orderSelectEl.value;
    try {
        let response = await fetch(`/api/cars/order?order=${orderValue}`,
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

// alert(elsContainingId[0])
const deleteObject = async function (ele) {
    let ownerEntityId = parseInt(document.getElementById("ownerEntityId").value)
    let id = parseInt(ele.value)
    let titlesTd = ele.parentElement.parentElement.parentElement.parentElement.children[0]
    try {
        let response = await fetch(`/api/${objectStr}s/${ownerEntityId}/delete?child=${id}`, {
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
            ele.parentNode.parentNode.remove()
            if (titlesTd.nextElementSibling.children.length === 0)
                titlesTd.parentNode.remove()
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}


const addContributor = async function () {
    let name = document.getElementById("name")
    let nationality = document.getElementById("nationality").value
    let tenure = parseInt(document.getElementById("tenure").value)
    let miniFormOuterDivEl = name.parentElement.parentElement.parentElement;
    let messageP = document.createElement("p");
    let ownerEntityId = parseInt(document.getElementById("ownerEntityId").value)
    let contributorsList = document.getElementById("entities")
    try {
        let response = await fetch('/api/cars/addcontributor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "name": `${name.value}`,
                "tenure": tenure,
                "nationality": `${nationality}`,
                "contributionIds": [ownerEntityId]
            })
        })
        let data = response.json();
        if (response.status === 200) {
            messageP.innerText = "Contributor successfully added!"
            messageP.style.color = "green"
            contributorsList.innerHTML += `<li style="list-style: none;text-decoration: none;">
                                            <a href="${'/engineers/'}"
                                              >${name.value}</a> <a style="padding: 0px 3px;"
                                                                                       class="btn btn-outline-success bg-dark deleteBtn">Delete</a>
                                            <input id="" type="hidden" value="">
                                        </li>`
            miniFormOuterDivEl.insertBefore(messageP, miniFormOuterDivEl.firstChild);
            // window.top.location = window.top.location
        } else if (response.status === 409) {
            messageP.innerText = "Contributor couldn't be added :'("
            messageP.style.color = "red"
            miniFormOuterDivEl.insertBefore(messageP, miniFormOuterDivEl.firstChild);
        }
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

