package be.kdg.java2.carfactory_application.domain.factory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car extends FactoryEntity {
    private String model;
    private double engineSize;
    private int price;
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(length = 64)
    private String image;

    //Lazy fetching because on /cars we only display data from Car
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "trademark_id")//fk
    private TradeMark tradeMark;

    @OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE)
    private List<Contribution> contributions = new ArrayList<>();

    public Car() {

    }

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributors) {
        this.contributions = contributors;
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


    public TradeMark getTradeMark() {
        return this.tradeMark;
    }

    public int getPrice() {
        return price;
    }

    public Color getColor() {
        return color;
    }


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



}
