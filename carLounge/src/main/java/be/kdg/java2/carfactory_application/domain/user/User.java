package be.kdg.java2.carfactory_application.domain.user;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
public class User {
    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Flag flag;


    public User(String username, String password, Gender gender, String nationality, int age, Role role) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.nationality = nationality;
        this.age = age;
        this.role = role;
        this.flag = Flag.ENABLED;
    }

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() {

    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
