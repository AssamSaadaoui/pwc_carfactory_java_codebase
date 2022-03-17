package be.kdg.java2.carfactory_application.domain.factory;

public enum CarPart {
    ENGINE("Engine"), TIRES("Tires"), WHEEL("Wheel"), BREAKS("Breaks");

    private final String name;

    CarPart(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
