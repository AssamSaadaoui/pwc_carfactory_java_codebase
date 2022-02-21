//Imports
import {htmlToElement, redirect,language,currentPath} from "./util";
import validator from 'validator';
//Search cars elements
const searchFieldEl = document.getElementById("lookup");
const dataList = document.getElementById("carsList");
//Cars page
const toOrderCarList = document.getElementById("listOfCars")
const orderSelectEl = document.getElementById("order");



//Event listeners
//Cars page (ordering functionality related)
if (orderSelectEl) //check location (orderSelect element only exists in cars page)
    orderSelectEl.addEventListener("change", async () => orderCars())
//Search bar related
if (searchFieldEl)
    searchFieldEl.addEventListener("keyup", async () => search());
//

// GET section
//Search objects
const search = async function () {
    const lookupValue = searchFieldEl.value;
    if (lookupValue.length < 3) {
        dataList.innerHTML = ''
        return;
    }
    try {
        const response = await fetch(`/api/${currentPath}?lookup=${lookupValue}`,
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
           <option  value="${currentPath === 'engineers' ? obj.name : obj.model}">
        `;
        map.set(`${currentPath === 'engineers' ? obj.name : obj.model}`, obj.id)
    })
    searchFieldEl.addEventListener("change", () => {
        map.forEach((value, key) => {
            if (key === searchFieldEl.value) redirect(`/${currentPath}/${value}`)
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
        processOrderedCarsData(data)
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
}

//Process ordered data
function processOrderedCarsData(carsArray) {
    toOrderCarList.innerHTML = ''
    carsArray.forEach(car => {
        toOrderCarList.innerHTML += `
        <div class="col-md-4 item">
          <div class="card cardanim animated goUp" style="width: 18rem;">
                    <img id="car" src="${car.imagePath}"
                         class="card-img-top" alt-title="${car.model}" alt="">
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

