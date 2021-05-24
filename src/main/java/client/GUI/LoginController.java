package client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    public AnchorPane pane;
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public Label statusLabel;
    @FXML
    public TextField serverField;

    public String getUsername() {
        return usernameField.getText();
    }

    public String getServer() { return serverField.getText(); }


    //TODO mettere condizione username già usato (controllo in risposta dal server)
    public void setUsername(URL location, ResourceBundle resources) {
        loginButton.setDisable(true);

        if (getUsername().isBlank())
            statusLabel.setText("LOGIN STATUS: SET A VALID USERNAME!");
        else {
            statusLabel.setText("LOGIN STATUS: SUCCESSFULLY LOGGED IN");
        }
    }

    //TODO Mettere i metodi della playerboard in ClientWriter
    public void loggingIn(ActionEvent event) throws Exception {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        try {
            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage(StageStyle.DECORATED);
            loader.setLocation(getClass().getResource("/graphics/gameSettings.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/graphics/punchboard/calamaio.png"));
            stage.setTitle("Game Settings");

            SettingsController controller = loader.getController();
            controller.setIpServerLabel(loader.getLocation(), loader.getResources());
            stage.setFullScreen(true);
            stage.showAndWait();
        } catch (IllegalStateException e) {
            System.out.println("pressButton failed");
        }


    }


    public void confirmUsername(ActionEvent actionEvent) {
        loginButton.setDisable((getUsername().isBlank()) && (getServer().isBlank()));
    }

    public void changeUsername(ActionEvent actionEvent) {
        usernameField.clear();
    }

    public void handleButtonAction(ActionEvent event) {

        serverField.setDisable(true);
        Button button = (Button) event.getSource();
        button.setDisable(true);
        String address = serverField.getText();

        try {
            if (address == null || !address.equals("localhost")) {
                statusLabel.setText("Server Unavailable at: " + address + "(Try: localhost)");
                throw new IllegalStateException("Server Unavailable at: " + address + "\n");
            }

        } catch (Exception exception) {
            System.out.println("handleButtonAction failed");
            serverField.setDisable(false);
            button.setDisable(false);
        }

    }
}



