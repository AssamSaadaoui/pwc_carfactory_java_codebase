package be.kdg.java2.carfactory_application.domain.factory;

public enum Color {
    BLACK("Black"),
    BLUE("Blue"),
    RED("Red"),
    YELLOW("Yellow"),
    GREEN("Green"),
    GRAY("Gray"),
    CHROME("Chrome"),
    ORANGE("Orange"),
    PURPLE("Purple"),
    WHITE("White");

    private final String displayValue;

    private Color(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}