package com.leverx.certificationquizapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class CertificationQuizApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(CertificationQuizApp.class.getResource("controller/home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        stage.setTitle("Certification Quiz");
        stage.setScene(scene);
        stage.show();
    }
}