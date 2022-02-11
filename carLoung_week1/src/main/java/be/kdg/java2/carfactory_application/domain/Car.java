package be.kdg.java2.carfactory_application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars", uniqueConstraints = @UniqueConstraint(columnNames = "model"))
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private int id;

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

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "cars_engineers",
            joinColumns = @JoinColumn(name = "car_id"),//owner table
            inverseJoinColumns = @JoinColumn(name = "engineer_id")
    )//target table
    //   link to engineers that worked on the car
    private List<Engineer> engineers = new ArrayList<>();

    public Car() {
    }

    public Car(String model, double engineSize, int price, LocalDate releaseDate, Color color) {
        this.model = model;
        this.engineSize = engineSize;
        this.releaseDate = releaseDate;
        this.color = color;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Engineer> getEngineers() {
        return engineers;
    }

    public TradeMark getTradeMark() {
        return this.tradeMark;
    }

    public int getPrice() {
        return price;
    }

    public Color getColor() {
        return color;
    }

    public void addEngineer(Engineer engineer) {
        engineers.add(engineer);
    }
    public void removeEngineer(Engineer engineer){engineers.remove(engineer);}

    public void setEngineers(List<Engineer> engineers) {
        this.engineers = engineers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Transient
    public String getImagePath() {
        if (image == null || Integer.valueOf(id) == null) return null;
        return "/car-images/" + image;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedDate = releaseDate.format(formatter);
        StringBuilder engineerNames = new StringBuilder();
        for (Engineer engineer : engineers) {
            engineerNames.append(engineer.getName()).append(", ");
        }
        engineerNames.delete(engineerNames.length() - 2, engineerNames.length());
        return String.format("Trademark: %s%n" +
                "Model: %s%n" +
                "Engine: %.1fL%n" +
                "Release date: %s%n" +
                "Color: %s%n" +
                "Price: %s$%n" +
                "Engineers: %s%n" +
                "------------------------------", tradeMark.getTitle(), model, engineSize, formattedDate, this.color.toString(), price, engineerNames);
    }

}
