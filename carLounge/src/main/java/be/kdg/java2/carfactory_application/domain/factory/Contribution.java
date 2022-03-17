package be.kdg.java2.carfactory_application.domain.factory;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"engineer_id", "car_id"})
})
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Enumerated(EnumType.STRING)
    private CarPart carPart;


    public Contribution(Car car, Engineer engineer) {
        this.car = car;
        this.engineer = engineer;
    }

    public Contribution() {

    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id")
    private Engineer engineer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    public int getId() {
        return id;
    }

    public Engineer getEngineer() {
        return engineer;
    }

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public CarPart getCarPart() {
        return carPart;
    }

    public void setCarPart(CarPart carPart) {
        this.carPart = carPart;
    }
}
