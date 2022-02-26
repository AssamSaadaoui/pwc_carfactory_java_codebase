package be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel;

import be.kdg.java2.carfactory_application.domain.Color;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarViewModel {

    private int id;
    @NotBlank(message = "Trademark name is mandatory")
    private String title;
    @NotBlank(message = "Founder name is mandatory")
    private String founder;
    @NotNull(message = "Launch date is mandatory")
    private int launchYear;
    @NotBlank(message = "Model is mandatory")
    @Size(min = 3, max = 100, message = "Model should have length between 2 and 100")
    private String model;
    @DecimalMax(value = "5.0", message = "Maximum engine size is 5.0 Litr")
    private double engineSize;

    //    @Pattern(regexp = "^(0|[1-9][0-9]*)$")
    @Min(value = 1000, message = "Minimum price is 1000$")
    private int price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Release date is mandatory")
    private LocalDate releaseDate;
    private Color color;
    private String colorText;

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    //    link to engineers that worked on the car
    private List<Integer> engineersIds = new ArrayList<>();

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setLaunchYear(int launchYear) {
        this.launchYear = launchYear;
    }

    public List<Integer> getEngineersIds() {
        return engineersIds;
    }

    public void setEngineersIds(List<Integer> engineersIds) {
        this.engineersIds = engineersIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
