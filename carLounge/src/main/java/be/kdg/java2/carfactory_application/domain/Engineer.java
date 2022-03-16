package be.kdg.java2.carfactory_application.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "engineers", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Engineer extends FactoryEntity {

    private String name;
    private int tenure;
    private String nationality;


    //We only have to provide the name of the field that maps to the relationship
    @OneToMany(mappedBy = "engineer", cascade = CascadeType.REMOVE)
    private List<Contribution> contributions = new ArrayList<>();

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public Engineer() {
    }

    public Engineer(String name, int tenure, String nationality) {
        this.name = name;
        this.tenure = tenure;
        this.nationality = nationality;
    }


//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

//    public List<Car> getCars() {
//        return cars;
//    }

//    public StringBuilder getCarsNames() {
//        StringBuilder carsNames = new StringBuilder();
//        for (Car car : cars) {
//            carsNames.append(car.getModel()).append(", ");
//        }
//        carsNames.delete(carsNames.length() - 2, carsNames.length());
//        return carsNames;
//    }

//    public void setCars(List<Car> cars) {
//        this.cars = cars;
//    }
//
//    public void addCar(Car car) {
//        cars.add(car);
//    }
//
//    public void removeCar(Car car) {
//        cars.remove(car);
//    }
//
//    @Override
//    public String toString() {
//        return
//                "Name: " + name + '\n' +
//                        "Tenure: " + tenure + " years\n" +
//                        "Nationality: " +
//                        nationality + "\nWork: " + getCarsNames() + "\n------------------------------";
//    }
}
