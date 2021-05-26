package client.GUI;

import client.ClientView;
import client.GUI.controllers.GUIController;
import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class GUI extends Application {
    private static final String LOGIN = "loginV.fxml";
    private static final String GAME_BOARD = "gameBoard.fxml";
    private static final String SETTINGS = "gameSettings.fxml";
    private static final String PLAYERBOARD = "playerBoard.fxml";
    private static final String LOADING = "waitingPlayers.fxml";

    private Scene currentScene;
    private Stage window;
    //private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * This object holds all the information about the game state
     */
    private final ClientView clientView;
    /**
     * Maps each scene name to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    /**
     * Maps each scene controller's name to the effective controller object, in order to get the correct controller
     * for modifying operations.
     */
    private final HashMap<String, GUIController> nameMapController = new HashMap<>();

    // CONSTRUCTOR

    public GUI() {
        clientView = new ClientView();
    }

    // JAVA FX METHODS

    /**
     * Main method of the GUI, which is called from the launcher in case user chooses GUI from the options.
     *
     * @param args of type String[] - parsed arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        // If we wanna add special fonts we should do it here
        run();
    }

    /**
     * @see Application#stop()
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    // PUBLIC METHODS

    /**
     * This method changes the scene displayed by the game window.
     * It is supposed to be called from the scenes controllers
     *
     * @param newScene represents the name of the scene to be shown
     */
    public void changeScene(String newScene) {
        if (nameMapScene.get(newScene) == null)
            System.out.println("Warning: couldn't find the specified scene");
        currentScene = nameMapScene.get(newScene);
        window.setScene(currentScene);
        window.show();
    }

    /**
     * This method sends a message to the server.
     * It is supposed to be called from the scenes controllers
     *
     * @param command the message to be sent
     */
    public void sendCommand(String command) {
        out.println(command);
    }

    public String readMessage() {
        String message = "";
        try {
            message = in.readLine();
        } catch (IOException e) {
            System.out.println("Warning: couldn't read message from server");
        }
        return message;
    }

    // PRIVATE METHODS

    /**
     * This method creates all the scenes of the App.
     * Each stage scene is put inside an hashmap, which links their name to their fxml filename.
     */
    private void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(LOGIN, GAME_BOARD, SETTINGS, PLAYERBOARD, LOADING));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/" + path));
                nameMapScene.put(path, new Scene(loader.load()));
                GUIController controller = loader.getController();
                controller.setGui(this);
                nameMapController.put(path, controller);
            }
        } catch (IOException e) {
            System.out.println("Warning: scenes setup failed");
        }
        currentScene = nameMapScene.get(LOGIN);
    }

    /**
     * Method run sets the title of the main stage and launches the window.
     */
    private void run() {
        window.setTitle("Masters Of Renaissance");
        window.setScene(currentScene);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/punchboard/calamaio.png")));
        window.show();
        /*ResizeHandler resize = new ResizeHandler((Pane) currentScene.lookup("#mainPane"));
        System.out.println("Resizing...");
        currentScene.widthProperty().addListener(resize.getWidthListener());
        currentScene.heightProperty().addListener(resize.getHeightListener());*/
    }

    // GETTERS

    public ClientView getClientView() {
        return clientView;
    }

    //SETTERS

    public void setClientSocket(Socket clientSocket) {
        //this.clientSocket = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Warning: failed to setup server connection");
        }
    }
}