package be.kdg.java2.carfactory_application.domain.user;

public enum Gender {
   MALE("Male"), FEMALE("Female");
   private final String displayValue;

   private Gender(String displayValue) {
      this.displayValue = displayValue;
   }

   public String getDisplayValue() {
      return displayValue;
   }
}
