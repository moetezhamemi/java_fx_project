package com.example.projet_restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/projet_restaurant/auth.fxml"));
            Parent root = loader.load();

            // Chargement du CSS
            Scene scene = new Scene(root, 1000, 650);
            String css = getClass().getResource("/com/example/projet_restaurant/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion de Restaurant");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}