//Imports
import {redirect, language, currentPath, headers} from "./util";
import anime from "animejs";

//Constants
const DEBOUNCE_DELAY = 1000
//Search cars elements
const searchFieldEl = document.getElementById("lookup");
const dataList = document.getElementById("carsList");
//Cars page (to order)
const toOrderCarList = document.getElementById("listOfCars")
const orderSelectEl = document.getElementById("order");


//Event listeners
//Cars page (ordering functionality related)
if (orderSelectEl) //check location (orderSelect element only exists in cars page)
    orderSelectEl.addEventListener("change", async () => {
        await orderCars()

    })
//Search bar related
if (searchFieldEl)
    searchFieldEl.addEventListener("keyup", async () => {
        const lookupValue = searchFieldEl.value;
        if (lookupValue.length < 3) {
            dataList.innerHTML = ''
            return;
        }
        search(lookupValue)
    });
//

// GET section
//Debounce
const debounce = (callback, delay = DEBOUNCE_DELAY) => {
    let timeout
    return (lookup) => {
        clearTimeout(timeout)
        setTimeout(() => {  
            callback(lookup)
        }, delay)
    }
}

const search = debounce(async (lookupValue) => {
    try {
        const response = await fetch(`/api/${currentPath}?lookup=${lookupValue}`,
            {
                headers
            })
        let data;
        if (response.status === 200) {
            data = await response.json();
        } else if (response.status === 204) {
            data = []
        } else {
            alert(`Received status code ${response.status}`);
        }
        console.log(data)
        processSearchData(data)
    } catch (err) {
        // catches errors both in fetch and response.json
        alert(err);
    }
})

function processSearchData(objectsArray) {
    dataList.innerHTML = '';
    //A map used to store the model or name of engineer, as well as the id to be used for redirection upon clicking
    const map = new Map();
    objectsArray.forEach(obj => {
        dataList.innerHTML += `
           <option  value="${currentPath === 'engineers' ? obj.name : obj.model}">
        `;
        map.set(`${currentPath === 'engineers' ? obj.name : obj.model}`, obj.id)
    })
    searchFieldEl.addEventListener("change", () => {
        map.forEach((value, key) => {
            // (e.g. a-class with id 1)
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
                headers
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

const animate = anime({
    targets: toOrderCarList,
    translateY: 30,
    easing: 'easeInOutSine',
    duration: 400,
    autoplay: false,
})

//Process ordered data
function processOrderedCarsData(carsArray) {
    animate.play()
    toOrderCarList.innerHTML = ''
    carsArray.forEach(carDTO => {
        let section = ((carDTO.author.username === carDTO.currentUser.username)
            || carDTO.currentUser.role === 'MANAGER'
            || carDTO.currentUser.role === 'ADMING') ? `<section style="display: inline-flex">
                                <div class="col-sm-4">
                                    <a style="padding: 0 3px" th:utext="#{aedit}"
                                       href="${'/cars/edit/' + carDTO.id}"
                                       class="btn btn-outline-success bg-dark">Edit
                                    </a>

                                </div>
                                <div class="col-sm-4">
                                    <form action="/cars/delete/${carDTO.id}" method="post"
                                          style="margin: unset">
                                        <button style="padding: 0px 3px;"
                                                class="btn btn-outline-success bg-dark deleteBtn">Delete
                                        </button>
                                    </form>
                                </div>
                            </section>` : ''
        toOrderCarList.innerHTML += `
        <div class="col-md-4 item">
          <div class="card cardanim animated goUp" style="width: 18rem;">
                    <img id="car" src="${carDTO.imagePath}"
                         class="card-img-top" alt-title="${carDTO.model}" alt="">
                    <div class="card-body">
                        <h5 class="card-title">${carDTO.model}</h5>
                    </div>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">${language === 'fr' ? 'Mod\u00E8le' : 'Model'}: ${carDTO.model}</li>
                        <li class="list-group-item">${language === 'fr' ? 'Moteur' : 'Engine'}: ${carDTO.engineSize} litr</li>
                        <li class="list-group-item">${language === 'fr' ? 'Prix' : 'Price'}: ${carDTO.price}$</li>
                    </ul>
                     <div class="card-body">
                        <div class="row" style="flex-wrap: nowrap ">
                            <div class="col-sm-4">
                                <a style="padding: 0 3px"
                                   href="${'/cars/' + carDTO.id}"
                                   class="btn btn-outline-success bg-dark">
                                    Details
                                </a>
                            </div>
                            ${section}
                        </div>
                        <p>Created by: ${carDTO.author.username} on ${carDTO.createdOn}</p>
                    </div>
          </div>
        </div>
        `
    })
}
