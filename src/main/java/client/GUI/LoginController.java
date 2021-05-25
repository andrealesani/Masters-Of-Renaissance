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

    public String getServer() {
        return serverField.getText();
    }


    //TODO mettere condizione username già usato (controllo in risposta dal server)

    //TODO Mettere i metodi della playerboard in ClientWriter
    public void loggingIn(ActionEvent event) throws Exception {


        if (getServer().isBlank() || !getServer().equals("localhost")) {
            statusLabel.setText("Server Unavailable at: " + getServer() + " (Try: localhost)");

            throw new IllegalStateException("Server Unavailable at: " + getServer() + "\n");
        }
        else if (getUsername().isBlank())
            statusLabel.setText("LOGIN STATUS: SET A VALID USERNAME!");

        else {
            System.out.println(getUsername());
            System.out.println(getServer());
            statusLabel.setText("LOGIN STATUS: SUCCESSFULLY LOGGED IN");

            //((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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


    }


}





