package com.example.assignment2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class MainController {
    @FXML private VBox mainScene; // Main scene container
    @FXML private VBox detailsScene; // Details scene container
    @FXML private TextField searchField; // Field to input search query
    @FXML private ListView<String> gameListView; // List view to display games
    @FXML private Label nameLabel; // Label to display game name
    @FXML private Label descriptionLabel; // Label to display game description
    @FXML private ImageView imageView; // ImageView to display game image
    @FXML private ScrollPane descriptionScrollPane; // ScrollPane to hold game description

    private String[] gameIds; // Array to store game IDs for selected games

    @FXML
    public void searchGames() {
        // Method to search games using an API
        try {
            String apiKey = "025b98acedaa48c08c42eb290187e47b";
            String encodedQuery = URLEncoder.encode(searchField.getText(), StandardCharsets.UTF_8);
            URL url = new URL("https://api.rawg.io/api/games?key=" + apiKey + "&search=" + encodedQuery);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray searchResults = jsonObject.getAsJsonArray("results");
            gameIds = new String[searchResults.size()];
            gameListView.getItems().clear();
            for (int i = 0; i < searchResults.size(); i++) {
                String name = searchResults.get(i).getAsJsonObject().get("name").getAsString();
                String gameId = searchResults.get(i).getAsJsonObject().get("id").getAsString();
                gameIds[i] = gameId;
                gameListView.getItems().add("Name: " + name);
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showGameDetails(javafx.scene.input.MouseEvent event) {
        // Method to show game details when a game is selected from the list
        int selectedIndex = gameListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            showGameDetails(selectedIndex);
        }
    }

    private void showGameDetails(int selectedIndex) {
        // Private method to fetch and display game details based on the selected index
        try {
            String apiKey = "025b98acedaa48c08c42eb290187e47b";
            String gameId = gameIds[selectedIndex];
            URL url = new URL("https://api.rawg.io/api/games/" + gameId + "?key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String cleanedDescription = removeHTMLTags(description);
            nameLabel.setText("Name: " + name);
            descriptionLabel.setText("Description: " + cleanedDescription);
            descriptionLabel.setWrapText(true);

            descriptionScrollPane.setPrefHeight(mainScene.getHeight());
            String imageUrl = jsonObject.get("background_image").getAsString();

            Image image = new Image(imageUrl);
            imageView.setImage(image);

            mainScene.setVisible(false);
            detailsScene.setVisible(true);

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to remove HTML tags from a text using regular expression
    private String removeHTMLTags(String textWithHTML) {
        // Regular expression to match HTML tags
        Pattern htmlTagPattern = Pattern.compile("<.*?>");
        // Remove HTML tags from the input text
        String cleanedText = htmlTagPattern.matcher(textWithHTML).replaceAll("");
        return cleanedText;
    }

    @FXML
    public void goBack() {
        // Method to go back to the main scene from the details scene
        mainScene.setVisible(true);
        detailsScene.setVisible(false);
    }
}
