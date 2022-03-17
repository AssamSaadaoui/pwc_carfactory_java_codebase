package be.kdg.java2.carfactory_application.domain.user;

public enum Flag {
    DISABLED("disabled"), ENABLED("enabled");

    private final String name;

    Flag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
