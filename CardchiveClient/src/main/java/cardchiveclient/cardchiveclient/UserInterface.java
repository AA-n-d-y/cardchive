/**
 * This class is used to create the user interface for the application
 * @author Andy Huang
 */

package cardchiveclient.cardchiveclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.controlsfx.control.spreadsheet.Grid;

public class UserInterface extends Application {

    // Function to display cards
    public static void displayCards(GridPane cardsLayout) {
        // Getting the cards to display them
        try {
            // Creating the url
            URI uri = new URI("http://localhost:8080/api/card/all");
            URL url = uri.toURL();

            // Creating the connection and setting up the request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            // Connecting
            connection.connect();

            // Handling status codes
            if (connection.getResponseCode() == 500) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Server Error");
                alert.setContentText("Server error");
                alert.show();
                return;
            }

            // Getting the response string
            String jsonStringCards = "";
            String serverResponse = "";
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((serverResponse = serverReader.readLine()) != null) { // Keep this looping if the server response is not null
                jsonStringCards += serverResponse;
            }

            // Disconnecting
            serverReader.close();
            connection.disconnect();

            // If the response is empty, return
            if (jsonStringCards.length() == 0) {
                // Displaying alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Card Collection is Empty");
                alert.setContentText("Card collection is empty");
                alert.show();
                return;
            }

            /// Else display the cards

            // Mapping the cards using the Gson library
            Gson gson = new Gson();
            Type clType = (new TypeToken<List<Card>>(){}).getType();
            List<Card> cards = gson.fromJson(jsonStringCards, clType);

            // Adding the cards to the cards layout
            int row = 0;
            int col = 0;

            for (Card card : cards) {
                // Setting the appearance
                Label cardName = new Label(card.getName());
                cardName.setStyle("-fx-text-fill: black; -fx-background-color: white; -fx-border-color: black; -fx-font-size: 15; -fx-max-width: 150;");
                ImageView cardImage = new ImageView(new Image("file:img/" + card.getPictureURL()));
                cardImage.setFitHeight(175);
                cardImage.setFitWidth(150);

                // Adding the items to the cards container
                VBox cardBox = new VBox(cardName, cardImage);
                cardBox.setSpacing(1);
                cardsLayout.add(cardBox, col, row);

                // Setting the columns and rows when needed
                col++;
                if (col == 5) {
                    row++;
                    col = 0;
                }


                // Details button
                Button cardDetailsBtn = new Button("View Details");
                cardDetailsBtn.setStyle("-fx-background-color: #7b75ff; -fx-border-color: black;-fx-pref-width: 150; -fx-min-width: 150;");
                cardDetailsBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            // Creating the url
                            URI uri = new URI("http://localhost:8080/api/card/" + card.getID());
                            URL url = uri.toURL();

                            // Creating the connection and setting up the request
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setDoOutput(true);

                            // Connecting
                            connection.connect();

                            // Handling status codes
                            if (connection.getResponseCode() == 500) {
                                // Displaying alert
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Server Error");
                                alert.setContentText("Server error");
                                alert.show();
                                return;
                            }
                            else if (connection.getResponseCode() == 404) {
                                // Displaying alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Card not Found");
                                alert.setContentText("Card not found");
                                alert.show();
                            }

                            // Getting the response string
                            String jsonStringCard = "";
                            String serverResponse = "";
                            BufferedReader serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            while ((serverResponse = serverReader.readLine()) != null) { // Keep this looping if the server response is not null
                                jsonStringCard += serverResponse;
                            }

                            // Disconnecting
                            serverReader.close();
                            connection.disconnect();

                            // If the response is empty, return
                            if (jsonStringCard.length() == 0) {
                                // Displaying alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Card not Found");
                                alert.setContentText("Card not found");
                                alert.show();
                                return;
                            }

                            /// Else display the card

                            // Mapping the card using the Gson library
                            Gson gson = new Gson();
                            Card tempCard = gson.fromJson(jsonStringCard, Card.class);


                            /// Creating a new window for users to display the card's details
                            Stage cardStage = new Stage();
                            cardStage.setHeight(500);
                            cardStage.setWidth(400);
                            cardStage.setTitle("Card Details");

                            BorderPane cardLayout = new BorderPane();
                            cardLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

                            // Label
                            Label cardLabel = new Label("Card Details");
                            cardLabel.setFont(new Font(15));
                            cardLabel.setStyle("-fx-font-weight: bold;");
                            HBox cardLabelContainer = new HBox(cardLabel);
                            cardLabelContainer.setPadding(new Insets(10, 0, 0, 0));
                            cardLabelContainer.setAlignment(Pos.CENTER);
                            cardLayout.setTop(cardLabelContainer);


                            /// Form section
                            GridPane cardGrid = new GridPane();
                            cardGrid.setAlignment(Pos.CENTER);
                            cardGrid.setVgap(10);
                            cardGrid.setHgap(15);

                            // Card ID
                            cardGrid.add(new Label("ID"), 0, 0);
                            TextField cardID = new TextField();
                            cardID.setText(String.valueOf(tempCard.getID()));
                            cardID.setEditable(false);
                            cardGrid.add(cardID, 1, 0);

                            // Card name
                            cardGrid.add(new Label("Name"), 0, 1);
                            TextField cardName = new TextField();
                            cardName.setText(tempCard.getName());
                            cardGrid.add(cardName, 1, 1);

                            // Card type
                            cardGrid.add(new Label("Type"), 0, 2);
                            TextField cardType = new TextField();
                            cardType.setText(tempCard.getType());
                            cardGrid.add(cardType, 1, 2);

                            // Rarity
                            cardGrid.add(new Label("Rarity"), 0, 3);
                            TextField cardRarity = new TextField();
                            cardRarity.setText(String.valueOf(tempCard.getRarity()));
                            cardGrid.add(cardRarity, 1, 3);

                            // Picture URL
                            cardGrid.add(new Label("Picture URL"), 0, 4);
                            TextField cardPictureURL = new TextField();
                            cardPictureURL.setEditable(false);
                            cardPictureURL.setText(tempCard.getPictureURL());
                            Button addPictureBtn = new Button("Add Picture");
                            addPictureBtn.setStyle("-fx-background-color: #fb99d0;");
                            FileChooser cardPictureChooser = new FileChooser();
                            cardPictureChooser.setTitle("Add Card Picture");
                            cardPictureChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                            final File[] picture = new File[1];
                            picture[0] = new File("img/" + tempCard.getPictureURL());
                            addPictureBtn.setOnAction(new EventHandler<ActionEvent>() { // Clicking on the add picture button
                                @Override
                                public void handle(ActionEvent event) {
                                    picture[0] = cardPictureChooser.showOpenDialog(cardStage);

                                    // If picture isn't null, set the text
                                    if (picture[0] != null) {
                                        cardPictureURL.setText(picture[0].getName());
                                    }
                                }
                            });
                            cardGrid.add(cardPictureURL, 1, 4);
                            cardGrid.add(addPictureBtn, 2, 4);

                            // Level
                            cardGrid.add(new Label("Level"), 0, 5);
                            TextField cardLevel = new TextField();
                            cardLevel.setText(String.valueOf(tempCard.getLevel()));
                            cardGrid.add(cardLevel, 1, 5);

                            // HP
                            cardGrid.add(new Label("Health Points"), 0, 6);
                            TextField cardHealthPoints = new TextField();
                            cardHealthPoints.setText(String.valueOf(tempCard.getHealthPoints()));
                            cardGrid.add(cardHealthPoints, 1, 6);

                            // AP
                            cardGrid.add(new Label("Attack Points"), 0, 7);
                            TextField cardAttackPoints = new TextField();
                            cardAttackPoints.setText(String.valueOf(tempCard.getAttackPoints()));
                            cardGrid.add(cardAttackPoints, 1, 7);

                            // DP
                            cardGrid.add(new Label("Defense Points"), 0, 8);
                            TextField cardDefensePoints = new TextField();
                            cardDefensePoints.setText(String.valueOf(tempCard.getDefensePoints()));
                            cardGrid.add(cardDefensePoints, 1, 8);
                            cardLayout.setCenter(cardGrid);


                            // Update button
                            Button updateBtn = new Button("Update");
                            updateBtn.setStyle("-fx-background-color: #7daff6; -fx-border-color: black;");
                            updateBtn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    // Extracting the form inputs
                                    String tempID = cardID.getText();
                                    String name = cardName.getText();
                                    String type = cardType.getText();
                                    String tempRarity = cardRarity.getText();
                                    String pictureURL = cardPictureURL.getText();
                                    String tempLevel = cardLevel.getText();
                                    String tempHealthPoints = cardHealthPoints.getText();
                                    String tempAttackPoints = cardAttackPoints.getText();
                                    String tempDefensePoints = cardDefensePoints.getText();

                                    // Attempting to send the data to the server
                                    try {
                                        // If any of the inputs are empty, display an alert
                                        if (tempID.trim().isEmpty() || name.trim().isEmpty() || type.trim().isEmpty() ||
                                                tempRarity.trim().isEmpty() || pictureURL.trim().isEmpty() || tempLevel.trim().isEmpty() ||
                                                tempHealthPoints.trim().isEmpty() || tempAttackPoints.trim().isEmpty() ||
                                                tempDefensePoints.trim().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                            alert.setTitle("Input Error");
                                            alert.setContentText("Enter values for each field");
                                            alert.show();
                                            return;
                                        }

                                        // Converting the necessary strings to integers
                                        int cID = Integer.parseInt(tempID);
                                        int rarity = Integer.parseInt(tempRarity);
                                        if (rarity > 10 || rarity < 1) {
                                            // Display an alert
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                            alert.setTitle("Rarity Input Error");
                                            alert.setContentText("Enter a rarity between 1 and 10");
                                            alert.show();
                                            return;
                                        }
                                        int level = Integer.parseInt(tempLevel);
                                        if (level > 13 || level < 0) {
                                            // Display an alert
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                            alert.setTitle("Level Input Error");
                                            alert.setContentText("Enter a level between 0 and 13");
                                            alert.show();
                                            return;
                                        }
                                        int healthPoints = Integer.parseInt(tempHealthPoints);
                                        int attackPoints = Integer.parseInt(tempAttackPoints);
                                        int defensePoints = Integer.parseInt(tempDefensePoints);


                                        // Saving the image to img folder
                                        File imgFolder = new File("img");
                                        if (!imgFolder.exists()) { // Creating the folder if it doesn't exist
                                            imgFolder.mkdir();
                                        }
                                        Path imgFolderPath = imgFolder.toPath().resolve(picture[0].getName());
                                        Files.copy(picture[0].toPath(), imgFolderPath, StandardCopyOption.REPLACE_EXISTING); // Replace the image if it already exists


                                        /// Sending the data

                                        // Creating the url
                                        URI uri = new URI("http://localhost:8080/api/card/edit/" + tempCard.getID());
                                        URL url = uri.toURL();

                                        // Creating the connection and setting up the request
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("PUT");
                                        connection.setDoOutput(true);
                                        connection.setRequestProperty("Content-Type", "application/json");

                                        // Writing the data over to the server
                                        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                                        writer.write("{\"id\":" + cID + ",\"name\":\"" + name + "\",\"type\":\"" + type +
                                                "\",\"rarity\":\"" + rarity + "\",\"pictureURL\":\"" + pictureURL +
                                                "\",\"healthPoints\":" + healthPoints + ",\"level\":" + level +
                                                ",\"attackPoints\":" + attackPoints + ",\"defensePoints\":" + defensePoints + "}");
                                        writer.flush();
                                        writer.close();

                                        // Connecting explicitly
                                        connection.connect();

                                        // Handling response codes
                                        if (connection.getResponseCode() == 200) {
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Successfully Updated Card");
                                            alert.setContentText("Updated card successfully");
                                            alert.show();
                                        }
                                        else if (connection.getResponseCode() == 404) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Update Card Error");
                                            alert.setContentText("Card with the provided ID was not found");
                                            alert.show();
                                        }
                                        else if (connection.getResponseCode() == 500) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Update Card Error");
                                            alert.setContentText("Server error");
                                            alert.show();
                                        }

                                        // Disconnecting
                                        connection.disconnect();

                                        // Updating the cards display
                                        cardsLayout.getChildren().clear();
                                        displayCards(cardsLayout);
                                    }

                                    catch (Exception obj) {
                                        // Display an alert
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                        alert.setTitle("Input Error");
                                        alert.setContentText("Please enter valid integers of reasonable length " +
                                                "for ID, Rarity, Level, Health Points, " +
                                                "Attack Points, and Defense Points");
                                        alert.show();
                                    }

                                }
                            });


                            // Delete card button
                            Button deleteBtn = new Button("Delete Card");
                            deleteBtn.setStyle("-fx-background-color: #ff6868; -fx-border-color: black;");
                            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        // Creating the url
                                        URI uri = new URI("http://localhost:8080/api/card/" + tempCard.getID());
                                        URL url = uri.toURL();

                                        // Creating the connection and setting up the request
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("DELETE");
                                        connection.setDoOutput(true);

                                        // Connecting explicitly
                                        connection.connect();

                                        // Handling response codes
                                        if (connection.getResponseCode() == 204) {
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Successfully Deleted Card");
                                            alert.setContentText("Deleted card successfully");
                                            alert.show();
                                        }
                                        else if (connection.getResponseCode() == 404) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Delete Card Error");
                                            alert.setContentText("Card with the provided ID was not found");
                                            alert.show();
                                        }
                                        else if (connection.getResponseCode() == 500) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Delete Card Error");
                                            alert.setContentText("Server error");
                                            alert.show();
                                        }

                                        // Disconnecting
                                        connection.disconnect();

                                        // Updating the cards display
                                        cardStage.close();
                                        cardsLayout.getChildren().clear();
                                        displayCards(cardsLayout);
                                    }

                                    catch (Exception obj) {
                                        System.out.println(obj);
                                        // Displaying the alert
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Delete Error");
                                        alert.setContentText("Failed to delete Card");
                                        alert.show();
                                    }
                                }
                            });


                            // Adding the buttons to the card layout
                            HBox cardBtnsContainer = new HBox(updateBtn, deleteBtn);
                            cardBtnsContainer.setSpacing(15);
                            cardBtnsContainer.setAlignment(Pos.CENTER);
                            cardBtnsContainer.setPadding(new Insets(0, 0, 20, 0));
                            cardLayout.setBottom(cardBtnsContainer);


                            // Setting the scene
                            Scene cardScene = new Scene(cardLayout);
                            cardStage.setScene(cardScene);
                            cardStage.show();
                        }

                        catch (Exception obj) {
                            // Displaying alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Server Error");
                            alert.setContentText("Server error");
                            alert.show();
                        }
                    }
                });
                cardBox.getChildren().add(cardDetailsBtn); // Adding the details button to the card

            }
        }

        catch (Exception obj) {
            System.out.println(obj.getMessage());
            // Display an alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to display the cards");
            alert.show();
        }
    }


    // Main function
    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }


    // Start function
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Setting the window's title and size
        primaryStage.setTitle("Cardchive");
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);


        /// Creating the scene

        // Main container
        BorderPane layoutContainer = new BorderPane();
        layoutContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));


        // Header
        Label header = new Label("Card Collection");
        header.setFont(new Font(25));
        header.setStyle("-fx-font-weight: bold;");


        // Add cards button
        Button addCardsBtn = new Button("Add Cards");
        header.setFont(new Font(20));
        addCardsBtn.setStyle("-fx-background-color: #5aff80; -fx-border-color: black;");
        addCardsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Creating a new window for users to add a new card
                Stage addCardsStage = new Stage();
                addCardsStage.setHeight(500);
                addCardsStage.setWidth(400);
                addCardsStage.setTitle("Add New Card");

                BorderPane addCardsLayout = new BorderPane();
                addCardsLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));


                // Label
                Label addCardsLabel = new Label("Add Card");
                addCardsLabel.setFont(new Font(15));
                addCardsLabel.setStyle("-fx-font-weight: bold;");
                HBox addCardsLabelContainer = new HBox(addCardsLabel);
                addCardsLabelContainer.setPadding(new Insets(10, 0, 0, 0));
                addCardsLabelContainer.setAlignment(Pos.CENTER);
                addCardsLayout.setTop(addCardsLabelContainer);


                /// Form section
                GridPane addCardsGrid = new GridPane();
                addCardsGrid.setAlignment(Pos.CENTER);
                addCardsGrid.setVgap(10);
                addCardsGrid.setHgap(15);

                // Card ID
                addCardsGrid.add(new Label("ID"), 0, 0);
                TextField cardID = new TextField();
                addCardsGrid.add(cardID, 1, 0);

                // Card name
                addCardsGrid.add(new Label("Name"), 0, 1);
                TextField cardName = new TextField();
                addCardsGrid.add(cardName, 1, 1);

                // Card type
                addCardsGrid.add(new Label("Type"), 0, 2);
                TextField cardType = new TextField();
                addCardsGrid.add(cardType, 1, 2);

                // Rarity
                addCardsGrid.add(new Label("Rarity"), 0, 3);
                TextField cardRarity = new TextField();
                addCardsGrid.add(cardRarity, 1, 3);

                // Picture URL
                addCardsGrid.add(new Label("Picture URL"), 0, 4);
                TextField cardPictureURL = new TextField();
                cardPictureURL.setEditable(false);
                Button addPictureBtn = new Button("Add Picture");
                addPictureBtn.setStyle("-fx-background-color: #fb99d0;");
                FileChooser cardPictureChooser = new FileChooser();
                cardPictureChooser.setTitle("Add Card Picture");
                cardPictureChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                final File[] picture = new File[1];
                addPictureBtn.setOnAction(new EventHandler<ActionEvent>() { // Clicking on the add picture button
                    @Override
                    public void handle(ActionEvent event) {
                        picture[0] = cardPictureChooser.showOpenDialog(addCardsStage);

                        // If picture isn't null, set the text
                        if (picture[0] != null) {
                            cardPictureURL.setText(picture[0].getName());
                        }
                    }
                });
                addCardsGrid.add(cardPictureURL, 1, 4);
                addCardsGrid.add(addPictureBtn, 2, 4);

                // Level
                addCardsGrid.add(new Label("Level"), 0, 5);
                TextField cardLevel = new TextField();
                addCardsGrid.add(cardLevel, 1, 5);

                // HP
                addCardsGrid.add(new Label("Health Points"), 0, 6);
                TextField cardHealthPoints = new TextField();
                addCardsGrid.add(cardHealthPoints, 1, 6);

                // AP
                addCardsGrid.add(new Label("Attack Points"), 0, 7);
                TextField cardAttackPoints = new TextField();
                addCardsGrid.add(cardAttackPoints, 1, 7);

                // DP
                addCardsGrid.add(new Label("Defense Points"), 0, 8);
                TextField cardDefensePoints = new TextField();
                addCardsGrid.add(cardDefensePoints, 1, 8);
                addCardsLayout.setCenter(addCardsGrid);


                // Add button
                Button addCardBtn = new Button("Add");
                addCardBtn.setStyle("-fx-background-color: #7daff6; -fx-border-color: black;");
                HBox addCardBtnContainer = new HBox(addCardBtn);
                addCardBtnContainer.setAlignment(Pos.CENTER);
                addCardBtnContainer.setPadding(new Insets(0, 0, 20, 0));
                addCardsLayout.setBottom(addCardBtnContainer);
                addCardBtn.setOnAction(new EventHandler<ActionEvent>() { // Clicking on the add card button
                    @Override
                    public void handle(ActionEvent event) {
                        // Extracting the form inputs
                        String tempID = cardID.getText();
                        String name = cardName.getText();
                        String type = cardType.getText();
                        String tempRarity = cardRarity.getText();
                        String pictureURL = cardPictureURL.getText();
                        String tempLevel = cardLevel.getText();
                        String tempHealthPoints = cardHealthPoints.getText();
                        String tempAttackPoints = cardAttackPoints.getText();
                        String tempDefensePoints = cardDefensePoints.getText();

                        // Attempting to send the data to the server
                        try {
                            // If any of the inputs are empty, display an alert
                            if (tempID.trim().isEmpty() || name.trim().isEmpty() || type.trim().isEmpty() ||
                                    tempRarity.trim().isEmpty() || pictureURL.trim().isEmpty() || tempLevel.trim().isEmpty() ||
                                    tempHealthPoints.trim().isEmpty() || tempAttackPoints.trim().isEmpty() ||
                                    tempDefensePoints.trim().isEmpty()) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                alert.setTitle("Input Error");
                                alert.setContentText("Enter values for each field");
                                alert.show();
                                return;
                            }

                            // Converting the necessary strings to integers
                            int cID = Integer.parseInt(tempID);
                            int rarity = Integer.parseInt(tempRarity);
                            if (rarity > 10 || rarity < 1) {
                                // Display an alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                alert.setTitle("Rarity Input Error");
                                alert.setContentText("Enter a rarity between 1 and 10");
                                alert.show();
                                return;
                            }
                            int level = Integer.parseInt(tempLevel);
                            if (level > 13 || level < 0) {
                                // Display an alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                                alert.setTitle("Level Input Error");
                                alert.setContentText("Enter a level between 0 and 13");
                                alert.show();
                                return;
                            }
                            int healthPoints = Integer.parseInt(tempHealthPoints);
                            int attackPoints = Integer.parseInt(tempAttackPoints);
                            int defensePoints = Integer.parseInt(tempDefensePoints);


                            // Saving the image to img folder
                            File imgFolder = new File("img");
                            if (!imgFolder.exists()) { // Creating the folder if it doesn't exist
                                imgFolder.mkdir();
                            }
                            Path imgFolderPath = imgFolder.toPath().resolve(picture[0].getName());
                            Files.copy(picture[0].toPath(), imgFolderPath, StandardCopyOption.REPLACE_EXISTING); // Replace the image if it already exists


                            /// Sending the data

                            // Creating the url
                            URI uri = new URI("http://localhost:8080/api/card/add");
                            URL url = uri.toURL();

                            // Creating the connection and setting up the request
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setRequestProperty("Content-Type", "application/json");

                            // Writing the data over to the server
                            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                            writer.write("{\"id\":" + cID + ",\"name\":\"" + name + "\",\"type\":\"" + type +
                                    "\",\"rarity\":\"" + rarity + "\",\"pictureURL\":\"" + pictureURL +
                                    "\",\"healthPoints\":" + healthPoints + ",\"level\":" + level +
                                    ",\"attackPoints\":" + attackPoints + ",\"defensePoints\":" + defensePoints + "}");
                            writer.flush();
                            writer.close();

                            // Connecting explicitly
                            connection.connect();

                            // Handling response codes
                            if (connection.getResponseCode() == 201) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Successfully Added Card");
                                alert.setContentText("Added card successfully");
                                alert.show();
                            }
                            else if (connection.getResponseCode() == 409) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Add Card Error");
                                alert.setContentText("Card with the provided ID already exists");
                                alert.show();
                            }
                            else if (connection.getResponseCode() == 500) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Add Card Error");
                                alert.setContentText("Server error");
                                alert.show();
                            }

                            // Disconnecting
                            connection.disconnect();
                        }

                        catch (Exception obj) {
                            // Display an alert
                            Alert alert = new Alert(Alert.AlertType.INFORMATION.ERROR);
                            alert.setTitle("Input Error");
                            alert.setContentText("Please enter valid integers of reasonable length " +
                                    "for ID, Rarity, Level, Health Points, " +
                                    "Attack Points, and Defense Points");
                            alert.show();
                        }

                    }
                });

                // Setting the scene
                Scene addCardsScene = new Scene(addCardsLayout);
                addCardsStage.setScene(addCardsScene);
                addCardsStage.show();
            }
        });


        // Display cards button
        Button displayCardsBtn = new Button("Display Cards");
        header.setFont(new Font(20));

        // Container for the cards
        GridPane cardsLayout = new GridPane();
        cardsLayout.setMinHeight(300);
        cardsLayout.setMinWidth(500);
        cardsLayout.setHgap(30);
        cardsLayout.setVgap(40);
        cardsLayout.setAlignment(Pos.BASELINE_CENTER);

        displayCardsBtn.setStyle("-fx-background-color: #ffbe3f; -fx-border-color: black;");
        displayCardsBtn.setOnAction(new EventHandler<ActionEvent>() { // Clicking the display cards button
            @Override
            public void handle(ActionEvent actionEvent) {
                // Calling the function to display the cards
                displayCards(cardsLayout);
            }
        });


        // Adding the buttons to the container
        HBox buttons = new HBox(addCardsBtn, displayCardsBtn);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.CENTER);
        VBox headerContainer = new VBox(header, buttons);
        headerContainer.setPadding(new Insets(20, 0, 20, 0));
        headerContainer.setSpacing(15);
        headerContainer.setAlignment(Pos.CENTER);
        layoutContainer.setTop(headerContainer);


        // Adding the cards container to a scroll pane which will be added to the container
        ScrollPane scrollPane = new ScrollPane(cardsLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        layoutContainer.setCenter(scrollPane);


        // Showing the stage
        Scene scene = new Scene(layoutContainer);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
