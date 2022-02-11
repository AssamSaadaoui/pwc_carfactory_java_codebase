package be.kdg.java2.carfactory_application.presentation.dto;

import be.kdg.java2.carfactory_application.domain.Car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeMarkDTO {
    @NotBlank(message = "Trademark name is mandatory")
    private String title;
    @NotBlank(message = "Founder name is mandatory")
    private String founder;
    @NotNull(message = "Launch date is mandatory")
    private int launchYear;
    private final transient List<Car> cars = new ArrayList<>();

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
}
