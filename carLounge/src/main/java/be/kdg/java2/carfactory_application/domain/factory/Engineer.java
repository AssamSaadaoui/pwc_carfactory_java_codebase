package be.kdg.java2.carfactory_application.domain.factory;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "engineers")
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

    @Override
    public String toString() {
        return "Engineer{" +
                "name='" + name + '\'' +
                ", tenure=" + tenure +
                ", nationality='" + nationality;
    }
}
