package be.kdg.java2.carfactory_application.domain;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class FactoryEntity {
    @Id
    @GeneratedValue
    @Column
    private int id;

    private LocalDateTime createdOn;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public FactoryEntity() {
        this.createdOn = LocalDateTime.now();
    }

    public FactoryEntity(User author) {
        this.author = author;
    }

    public LocalDate getCreatedOn() {
        return createdOn.toLocalDate();
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
