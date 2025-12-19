module com.leverx.certificationquizapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.leverx.certificationquizapp to javafx.fxml;
    exports com.leverx.certificationquizapp;
    exports com.leverx.certificationquizapp.model;
    opens com.leverx.certificationquizapp.model to javafx.fxml;
    exports com.leverx.certificationquizapp.logic;
    opens com.leverx.certificationquizapp.logic to javafx.fxml;
    exports com.leverx.certificationquizapp.util;
    opens com.leverx.certificationquizapp.util to javafx.fxml;
    exports com.leverx.certificationquizapp.controller;
    opens com.leverx.certificationquizapp.controller to javafx.fxml;
}