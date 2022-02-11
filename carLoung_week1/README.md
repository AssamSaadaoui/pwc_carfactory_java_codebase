# CarLounge spring application

---
>### Course name:
>###### Programming 2.3

>### Author
>###### Assam Saadaoui

>### Email
>###### assam.saadaoui@student.kdg.be

---
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

### - Controller processAdd{Entity}() method a bit stacked:

#### I had to make multiple checks to ensure convenient experience to the user upon entity creation. However, i have another simpler version. (Will show if allowed)

- For example, in CarController, i had to make two checks and pass two boolean parameters to the form (isMainForm) and (
  isSubForm)
  , With those checks (will be explained with an example during exam if unclear,) the user will have the ability to
  either add a new engineer from existent (engineers list) or a new engineer via form. We first check if main form (new
  car form) contains any errors, if so, we pass isMainForm as true, indicating that main form contains error (isSubform
  set to false on first check.)
  Second check, we look if subForm contains any errors (and that when no contributors checked from existent list is
  empty && no new Contributor is added.) "No contributors."
  (A trial run would be more elaborative)

---
## Build
>JDK version used : adopt-openjdk 11


#Week 1
