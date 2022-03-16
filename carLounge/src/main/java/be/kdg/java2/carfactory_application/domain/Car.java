package be.kdg.java2.carfactory_application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars", uniqueConstraints = @UniqueConstraint(columnNames = "model"))
public class Car extends FactoryEntity {

    private String model;
    private double engineSize;
    private int price;
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(length = 64)
    private String image;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "trademark_id")//fk
    private TradeMark tradeMark;

    @OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE)
        private List<Contribution> contributors = new ArrayList<>();

    public List<Contribution> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contribution> contributors) {
        this.contributors = contributors;
    }

    public Car() {
    }

    public Car(String model, double engineSize, int price, LocalDate releaseDate, Color color) {
        this.model = model;
        this.engineSize = engineSize;
        this.releaseDate = releaseDate;
        this.color = color;
        this.price = price;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTradeMark(TradeMark tradeMark) {
        this.tradeMark = tradeMark;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setColor(Color color) {
        this.color = color;
    }

//    public List<Engineer> getEngineers() {
//        return engineers;
//    }

    public TradeMark getTradeMark() {
        return this.tradeMark;
    }

    public int getPrice() {
        return price;
    }

    public Color getColor() {
        return color;
    }

//    public void addEngineer(Engineer engineer) {
//        engineers.add(engineer);
//    }
//
//    public void removeEngineer(Engineer engineer) {
//        engineers.remove(engineer);
//    }
//
//    public void setEngineers(List<Engineer> engineers) {
//        this.engineers = engineers;
//    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Transient
    public String getImagePath() {
        if (image == null || Integer.valueOf(this.getId()) == null) return null;
        return "/car-images/" + image;
    }

//    public String toString() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
//        String formattedDate = releaseDate.format(formatter);
//        StringBuilder engineerNames = new StringBuilder();
////        for (Engineer engineer : contributors) {
////            engineerNames.append(engineer.getName()).append(", ");
////        }
//        engineerNames.delete(engineerNames.length() - 2, engineerNames.length());
//        return String.format("Trademark: %s%n" +
//                "Model: %s%n" +
//                "Engine: %.1fL%n" +
//                "Release date: %s%n" +
//                "Color: %s%n" +
//                "Price: %s$%n" +
//                "Engineers: %s%n" +
//                "------------------------------", tradeMark.getTitle(), model, engineSize, formattedDate, this.color.toString(), price, engineerNames);
//    }

}
