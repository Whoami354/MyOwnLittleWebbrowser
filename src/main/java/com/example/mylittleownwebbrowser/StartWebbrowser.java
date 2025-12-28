package com.example.mylittleownwebbrowser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class StartWebbrowser extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mein eigener Webbrowser");
        primaryStage.setScene(new Scene(new Browser()));
        primaryStage.show();
    }
}
