# CarLounge spring application

---
> ### Course name:
>###### Programming 2.3

> ### Author
>###### Assam Saadaoui

> ### Email
>###### <assam.saadaoui@student.kdg.be>

---

## Build

> ### Settings:
>
> **JDK version used** : adopt-openjdk 11
>
>**Server port**: 8082
>
>**Database used**: HSQL
>### Instructions:
> 1- Make sure to have downloaded Node.js. _This application is goes the Node.js Package Manager to add dependencies to the project_.
>
>2- Run `npm install` to download the dependencies already included in the package.json file.
>
>3- Before running the application, run `npm run build` to execute the existent scripts. _under "script" in package.json file_
>
>4- To automate the `npm run build` command (execution of scripts after every update to the script or css files), in build.gradle file make sure to have compileJava.dependsOn npm_run_build. Then run the application using gradle.

## Project concept recap

> **Trademark**, **Car**, Mechanical **Engineer**
>
> **Car-Engineer** (a car can be worked on by multiple engineers, an **Engineer** can work on multiple cars)
>
> A **TradeMark** can have multiple cars, but a **Car** belongs to a single **TradeMark**

---

## Assignments related notes:

### Icon used:

**Magnifying glass** used with on the search button at the right corner of navbar 
``` 
Location url: `/cars` or `engineers`

Source file: `resources/templates/fragments/header.html`
```
### JavaScript packages used:

**1) validator** used for adding an engineer to a car. 
```
Location url `/cars/1` or any id 

HTML source file `resources/templates/cars/cardetails.html`

JavaScript source file `js/engineer.js` called from `js/detailspage.js` line `109`
```
**2) animejs** used for when ordering cars by price. 
```
Location url `/cars`

HTML source file `resources/templates/cars/cars`

JavaScript source file `js/search.js` line `98`
```
### Assignment REST specific implementations:
```
GET requests : 
          1) searching for a car or engineer from seach bar

Location url `/cars` or `/engineers`
JavaScript source file `js/search.js`

          2) ordering cars by price 
          
Location url `/cars`
JavaScript source file `js/search.js`
```
```
DELETE, POST and PUT requests

Location url `/cars`
HTML source file `resources/templates/cars/cardetails.html`
JavaScript source file `js/detailspage.js`
```
## Summary of the implemented HTTP requests
### GET section

---

#### *Fetching sorted cars by price in ascending order:*  [ OK - *code: 200* ]

***Request***

```
GET http://localhost:8081/api/cars/sort?order=asc
Accept: application/json
Content-type: application/json
```

***Response***

```
[
  {
    "id": 5,
    "title": null,
    "founder": null,
    "launchYear": 0,
    "model": "8 Series",
    "engineSize": 4.0,
    "price": 14000,
    "releaseDate": null,
    "color": null,
    "colorText": null,
    "engineersIds": [],
    "imagePath": "/car-images/bmw8.png"
  },
  {
    "id": 3,
    "title": null,
    "founder": null,
    "launchYear": 0,
    "model": "Fiesta SE",
    "engineSize": 1.2,
    "price": 16000,
    "releaseDate": null,
    "color": null,
    "colorText": null,
    "engineersIds": [],
    "imagePath": "/car-images/fordSE.jpg"
  }
```

#### *Fetching a car by an existent model name:*  [ OK - *code: 200* ]

***Request***

```
GET http://localhost:8081/api/cars?lookup=a-class
Accept: application/json
Content-type: application/json
```

***Response***

```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 21 Feb 2022 23:55:18 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
  {
    "id": 1,
    "title": null,
    "founder": null,
    "launchYear": 0,
    "model": "A-Class",
    "engineSize": 0.0,
    "price": 0,
    "releaseDate": null,
    "color": null,
    "colorText": null,
    "engineersIds": [],
    "imagePath": null
  }
]

Response code: 200; Time: 152ms; Content length: 182 bytes

```

#### *Fetching a car by a non-existent model name:*  [ NO_CONTENT - *code: 204* ]

***Request***

```
GET http://localhost:8081/api/cars?lookup=potato
Accept: application/json
Content-type: application/json
```

***Response***

```
HTTP/1.1 204 
Date: Tue, 22 Feb 2022 00:01:57 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>
```

#### *Fetching a car by model accepting a non valid format:* [ Not Acceptable - *code: 406* ]

***Request***

```
GET http://localhost:8081/api/cars?lookup=a-class
Accept: text/html
Content-type: application/json
```

***Response***

```
HTTP/1.1 406 
Content-Type: text/html;charset=UTF-8
Content-Language: en-GB
Content-Length: 280
Date: Tue, 22 Feb 2022 00:04:53 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<html>
<body><h1>Whitelabel Error Page</h1>
<p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p>
<div id='created'>Tue Feb 22 01:04:53 CET 2022</div>
<div>There was an unexpected error (type=Not Acceptable, status=406).</div>
</body>
</html>

Response code: 406; Time: 148ms; Content length: 280 bytes
```

#### *Fetching an existent engineer with XML format*: [ OK - *code: 200*  ]

***Request***

```
GET http://localhost:8081/api/engineers?lookup=Winter Korn
Accept: application/xml
```

***Response***

```
HTTP/1.1 200
Content-Type: application/xml;charset=UTF-8

<List>
  <Item>
    <id>3</id>
    <name>Winter Korn</name>
    <tenure>0</tenure>
    <nationality/>
    <contributionIds/>
  </Item>
</List>
```

---

### DELETE section

#### *Deleting contribution with id {4} from engineer with id {5}:* [ OK - *code: 200* ]
***Request***
```
DELETE http://localhost:8081/api/engineers/5/cars/4
Accept: application/json
```

#### *Deleting contributor with id {3} from car with id {3}:* [ OK - *code: 200* ]
***Request***
```
DELETE http://localhost:8081/api/cars/3/engineers/3
Accept: application/json
```

#### *Deleting a non-existent contribution with id {30} from engineer with id {5}* [ Internal Server Error - *code: 500* ]
***Request***
```
DELETE http://localhost:8081/api/engineers/5/cars/30
Accept: application/json
```
***Response***
```
DELETE http://localhost:8081/api/engineers/5/cars/30

HTTP/1.1 500 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Feb 2022 18:38:52 GMT
Connection: close

{
  "timestamp": "2022-02-27T18:38:52.382+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/engineers/5/cars/30"
}
```
### POST section

#### _Adding an engineer to a car (a contributor)_  [ OK - *code: 200* ]
***Request***

```
POST http://localhost:8081/api/cars/1/engineers
Accept: application/json
Content-Type: application/json

{
  "name": "Issam",
  "tenure": 10,
  "nationality": "Algerian"
}
```

***Response***

```
POST http://localhost:8081/api/cars/1/engineers

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 26 Feb 2022 22:22:27 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 9,
  "name": "Issam",
  "tenure": 10,
  "nationality": "Algerian",
  "contributionIds": [
    1
  ]
}
```

#### _Adding the same engineer (same name) to a car (a contributor)_ [ CONFLICT - *code: 409* ]

***Note**: This was supposed to be handled in the middleware (if engineer already exists, just fetch it and add it to
the car. For the sake of a different error status, it goes this way*

***Request***

```
POST http://localhost:8081/api/cars/1/engineers
Accept: application/json
Content-Type: application/json

{
  "name": "Issam",
  "tenure": 10,
  "nationality": "Algerian"
}
```

***Response***

```
POST http://localhost:8081/api/cars/1/engineers

HTTP/1.1 409 
Content-Type: application/json
Content-Length: 21
Date: Sat, 26 Feb 2022 22:25:23 GMT
Keep-Alive: timeout=60
Connection: keep-alive

Issam already exists.
```

### PUT section

#### _Updating car with id {1}_ [ OK - *code: 200* ]
***Request***
```
PUT http://localhost:8081/api/cars/1
Accept: application/json
Content-Type: application/json

{
  "model": 111,
  "engineSize": 1.5,
  "title": "potato",
  "releaseDate": "1998-03-05",
  "price": 10000,
  "colorText": "Black"
}
```
***Response***
```
PUT http://localhost:8081/api/cars/1

HTTP/1.1 200 
Content-Length: 0
Date: Sat, 26 Feb 2022 22:30:20 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>
```

#### _Updating car while inserting wrong input, integer instead of int_ [ Bad Request - *code: 400* ]
***Request***
```
PUT http://localhost:8081/api/cars/1
Accept: application/json
Content-Type: application/json

{
  "model": 111,
  "engineSize": 1.5,
  "title": "potato",
  "releaseDate": "1998-03-05",
  "price": 10000,
  "colorText": "Black"
}
```
***Response***
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 26 Feb 2022 22:35:51 GMT
Connection: close

{
  "timestamp": "2022-02-26T22:35:51.252+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/cars/1"
}
```


