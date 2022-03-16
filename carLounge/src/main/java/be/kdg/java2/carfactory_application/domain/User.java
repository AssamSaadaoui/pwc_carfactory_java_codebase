package be.kdg.java2.carfactory_application.domain;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
public class User {
    @Id
    @GeneratedValue
    @Column
    private long id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
