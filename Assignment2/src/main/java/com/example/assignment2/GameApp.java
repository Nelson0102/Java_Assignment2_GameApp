package com.example.assignment2;

import javafx.application.Application; // Importing Application class for JavaFX
import javafx.fxml.FXMLLoader; // Importing FXMLLoader to load FXML files
import javafx.scene.Scene; // Importing Scene class for JavaFX
import javafx.scene.image.Image; // Importing Image class for JavaFX
import javafx.stage.Stage; // Importing Stage class for JavaFX

import java.net.URL; // Importing URL class to handle URLs

public class GameApp extends Application {

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    // Override the start method to define the main logic of the application
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main scene FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment2/MainScene.fxml"));
        Scene mainScene = new Scene(loader.load(), 600, 600); // Create the main scene with dimensions 600x600

        // Load the stylesheet, if available
        URL cssUrl = getClass().getResource("/com/example/assignment2/styles.css");
        if (cssUrl != null) {
            mainScene.getStylesheets().add(cssUrl.toExternalForm()); // Add the stylesheet if found
        } else {
            System.err.println("Unable to load CSS file. Stylesheet not found."); // Error message if stylesheet is not found
        }

        // Load the application icon
        Image appIcon = new Image("file:icon/gaming.png");
        primaryStage.getIcons().add(appIcon); // Add the icon to the primary stage

        primaryStage.setTitle("Game Mania"); // Set the title of the application window
        primaryStage.setScene(mainScene); // Set the main scene to the primary stage
        primaryStage.show(); // Display the primary stage
    }
}
