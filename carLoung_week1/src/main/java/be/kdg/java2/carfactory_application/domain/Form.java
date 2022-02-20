package be.kdg.java2.carfactory_application.domain;

import java.util.List;
import java.util.Map;

public class Form {
    String reference;
    Map<String, String> data;

    public Form(String reference, Map<String, String> data) {
        this.data = Map.of("text", "name", "email", "email", "password", "userPassword", "date", "dob");
        this.reference = reference;
    }
//    Form object is going to be persisted


}
