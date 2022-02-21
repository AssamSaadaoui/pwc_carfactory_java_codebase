# CarLounge spring application

---
>### Course name:
>###### Programming 2.3

>### Author
>###### Assam Saadaoui

>### Email
>###### <assam.saadaoui@student.kdg.be>

---

## Build
>**JDK version used** : adopt-openjdk 11
>
>**Server port**: 8082
>
>**Database used**: HSQL
> 
## Project concept recap

Trademark, Car, Mechanical Engineer

Car-Engineer (a car can be worked on by multiple engineers, an Engineer can work on multiple cars)

A TradeMark can have multiple Cars, but a Car belongs to a single TradeMark

---
## Features:

#### There is a lot them that it makes it difficult to detail them. I would like to demonstrate them exam day.

#### However, I'll label some of them:

- File upload utility
- Ability to edit, delete car
- Flexibility of entity addition (user experience)
- Ability to search for entities anywhere from routes indicating entity (navbar search form)
- Extensive use of thymeleaf features
- Use of utility classes
- Use of helper classes
- Good styling

## Notes:

### 1. Controller processAdd{Entity}():

### I had to make multiple checks to ensure convenient experience to the user upon entity creation. However, i have another simpler version. (Will show if allowed)

- For example, in **CarController**, i had to make two checks and pass two boolean parameters to the form (*isMainForm*) and 
(*isSubForm*), With those checks (***will be explained with an example if unclear***,) the user will have the ability to
  either add a new engineer from existent (**engineers list**) or a *new engineer* via form. 
  - **First check** if main form (**new car form**) contains any errors, if so, we pass ***isMainForm*** as true, indicating that main form contains error (*isSubform
    set to false on first check.*)
  - **Second check**, we look if **subForm** contains any errors (and that when no contributors checked from existent list is
    empty && no new *Contributor* is added.) "**No contributors.**"
  

---



#Week 1

---
###GET section
#### Fetching sorted cars by price in ascending order:  [ OK - *code: 200* ]
```
GET http://localhost:8081/api/cars/sort?order=asc
Accept: application/json
Content-type: application/json
```

#### Fetching a car by an existent model name:  [ OK - *code: 200* ]
```
GET http://localhost:8081/api/cars?lookup=a-class
Accept: application/json
Content-type: application/json
```

#### Fetching a car by a non-existent model name:  [ NO_CONTENT - *code: 204* ]
```
GET http://localhost:8081/api/cars?lookup=potato
Accept: application/json
Content-type: application/json
```

#### Fetching a car by an existent model name: [ Not Acceptable - *code: 406* ]
```
GET http://localhost:8081/api/cars?lookup=a-class
Accept: text/html
Content-type: application/json
```

#### Fetching an existent engineer by name: [ OK - *code: 200*  ]
```
GET http://localhost:8081/api/engineers?lookup=Winter Korn
Accept: application/xml
```



---
###DELETE section
#### Deleting contribution with id {4} from engineer with id {5}: [ OK - *code: 200* ]
```
DELETE http://localhost:8081/api/engineers/5/cars/4
Accept: application/json
```

#### Deleting contributor with id {3} from car with id {3}: [ OK - *code: 200* ]
```
DELETE http://localhost:8081/api/cars/3/engineers/3
Accept: application/json
```

#### Deleting a non-existent contribution with id {30} from engineer with id {5}: [ Internal Server Error - *code: 500* ]
```
DELETE http://localhost:8081/api/engineers/5/cars/30
Accept: application/json
```






