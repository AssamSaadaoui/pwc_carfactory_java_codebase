package be.kdg.java2.carfactory_application.presentation.dto;

import be.kdg.java2.carfactory_application.domain.Color;
import be.kdg.java2.carfactory_application.domain.TradeMark;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DTO {

    @NotBlank(message = "Trademark name is mandatory")
    private String title;
    @NotBlank(message = "Founder name is mandatory")
    private String founder;
    @NotNull(message = "Launch date is mandatory")
    private int launchYear;
    private String imageUrl;
    @NotBlank(message = "Model is mandatory")
    @Size(min = 3, max = 100, message = "Model should have length between 2 and 100")
    private String model;
    private TradeMark tradeMark;
    @DecimalMax(value = "5.0", message = "Maximum engine size is 5.0 Litr")
    private double engineSize;
    @Min(value = 1000, message = "Minimum price is 1000$")
    private int price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Release date is mandatory")
    private LocalDate releaseDate;
    private Color color;
    //    link to engineers that worked on the car
    private List<Integer> engineersIds = new ArrayList<>();
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 100, message = "Name should have length between 3 and 100")
    private String name;
    @Max(value = 30, message = "You can enter a maximum of 30 years")
    private int tenure;
    @NotBlank(message = "Nationality is mandatory")
    private String nationality;

    private List<Integer> contributionIds = new ArrayList<>();

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TradeMark getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(TradeMark tradeMark) {
        this.tradeMark = tradeMark;
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

    public List<Integer> getEngineersIds() {
        return engineersIds;
    }

    public void setEngineersIds(List<Integer> engineersIds) {
        this.engineersIds = engineersIds;
    }

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

    public List<Integer> getContributionIds() {
        return contributionIds;
    }

    public void setContributionIds(List<Integer> contributionIds) {
        this.contributionIds = contributionIds;
    }
}
