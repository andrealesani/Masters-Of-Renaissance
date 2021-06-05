package client.GUI;

import com.google.gson.Gson;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SimplePopup {
    public static void display(String messageType, String jsonMessage) {
        Gson gson = new Gson();
        Stage window = new Stage();
        //the regex is used to eliminate the special characters that would be counted in the string length
        String title = messageType.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "");
        String message = jsonMessage.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "");


        //Block events to main game window
        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().add(new Image(SimplePopup.class.getResourceAsStream("/graphics/punchboard/calamaio.png")));
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(120);
        window.setResizable(false);

        Label label = new Label();
        label.setFont(new Font("Roboto", 16));
        label.setText(message);
        label.getStyleClass().add("text-field");
        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("mainBackground");

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("/css/gameBoard.css");
        window.setScene(scene);
        scene.setCursor(new ImageCursor(new Image(SimplePopup.class.getResourceAsStream("/graphics/cursor.png"))));
        window.show(); // call showAndWait() if you want the game board to stop updating until this window is dismissed

    }
}
