package client.GUI;

import client.GUI.Controller;
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

public class SampleController implements Initializable {

    @FXML
    public AnchorPane pane;
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public Label statusLabel;
    @FXML
    private ImageView backgroundImage;

    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(pane.widthProperty());
        backgroundImage.fitHeightProperty().bind(pane.heightProperty());
    }

    //TODO mettere condizione username già usato (controllo in risposta dal server)
    public void loggingIn(ActionEvent actionEvent) throws Exception {

        if (getUsername().isBlank())
            statusLabel.setText("LOGIN STATUS: SET A VALID USERNAME!");
        else {
            statusLabel.setText("LOGIN STATUS: SUCCESSFULLY LOGGED IN");
            pressButton(actionEvent);
        }
    }

    //TODO Mettere i metodi della playerboard in ClientWriter
    public void pressButton(ActionEvent event) throws Exception {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        try {
            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage(StageStyle.DECORATED);
            loader.setLocation(getClass().getResource("/graphics/numPlayers.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/graphics/punchboard/calamaio.png"));
            stage.setTitle("Game Settings");

            Controller controller = loader.getController();
            controller.setIpServerLabel(loader.getLocation(), loader.getResources());

            stage.showAndWait();
        } catch(IllegalStateException e) {
            System.out.println("pressButton failed");
        }


    }


}



