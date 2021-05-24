package client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {


    @Override
    public void start (Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        primaryStage = new Stage(StageStyle.DECORATED);
        loader.setLocation(getClass().getResource("/graphics/gameSettings.fxml"));
        Parent parent = loader.load();
        primaryStage.getIcons().add(new Image("/graphics/punchboard/calamaio.png"));
        primaryStage.setTitle("Game Settings");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();

        Controller controller = loader.getController();
        controller.setIpServerLabel(loader.getLocation(), loader.getResources());
    }


    public static void main(String[] args) {
        launch(args);
    }
}