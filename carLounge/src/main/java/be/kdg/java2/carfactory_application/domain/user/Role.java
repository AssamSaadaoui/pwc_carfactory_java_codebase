package be.kdg.java2.carfactory_application.domain.user;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), MANAGER("ROLE_MANAGER");;

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
