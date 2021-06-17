package client.GUI;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used for displaying complex popups, using an entire Scene as content
 */
public class ThiccPopup {

    /**
     * Displays the given scene as a popup
     *
     * @param scene the scene to be displayed
     */
    public static void display(Scene scene) {
        Stage window = new Stage();
        String title = "t h i c c  popup";


        //Block events to main game window
        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().add(new Image(SimplePopup.class.getResourceAsStream("/graphics/punchboard/calamaio.png")));
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(120);
        window.setResizable(false);

        //Display window and wait for it to be closed before returning
        window.setScene(scene);

        window.showAndWait(); // call showAndWait() if you want the game board to stop updating until this window is dismissed

    }
}
