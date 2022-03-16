package be.kdg.java2.carfactory_application.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trademarks")
public class TradeMark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trademark_id", nullable = false)
    private int id;
    private String title;
    private String founder;
    private int launchYear;

    @OneToMany(mappedBy = "tradeMark",cascade = {CascadeType.ALL})
    private final List<Car> cars = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TradeMark() {

    }

    public TradeMark(String title, String founder, int launchDate) {
        this.title = title;
        this.founder = founder;
        this.launchYear = launchDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLaunchYear(int launchYear) {
        this.launchYear = launchYear;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public int getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(LocalDate launchYear) {
        launchYear = launchYear;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    @Override
    public String toString() {
        return "Trademark" + title + '\n' +
                "Founder='" + founder + '\n' +
                "LaunchDate=" + launchYear;
    }
}
