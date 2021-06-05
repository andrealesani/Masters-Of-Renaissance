package client.GUI;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ThiccPopup {
    public static void display(GUI gui, String fileName) {
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
        window.setScene(gui.getSceneByFileName(fileName));

        window.showAndWait(); // call showAndWait() if you want the game board to stop updating until this window is dismissed

    }
}
