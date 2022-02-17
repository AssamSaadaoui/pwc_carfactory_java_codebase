package be.kdg.java2.carfactory_application.presentation.controller.api.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class EngineerDTO {
    private int id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 100, message = "Name should have length between 3 and 100")
    private String name;
    @Max(value = 30, message = "You can enter a maximum of 30 years")
    private int tenure;
    @NotBlank(message = "Nationality is mandatory")
    private String nationality;
    //    link to cars that worked on
    private List<Integer> contributionIds = new ArrayList<>();

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
