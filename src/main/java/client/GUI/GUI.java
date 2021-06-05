package client.GUI;

import client.ClientReader;
import client.ClientView;
import client.GUI.controllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class GUI extends Application {
    private static final String LOGIN = "hostAndPort.fxml";
    private static final String GAME_BOARD = "gameBoard.fxml";
    private static final String SETTINGS = "gameSettings.fxml";
    private static final String LOADING = "waitingPlayers.fxml";
    private static final String GAME_BOARD_4 = "gameBoard4Players.fxml";
    private static final String PRODUCTIONS = "productions.fxml";


    private Scene currentScene;
    private Stage window;
    private Socket clientSocket;
    private ClientReader clientReader;
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
        window.setMinWidth(400);
        window.setMinHeight(600);
        window.setResizable(true);
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
        System.out.println("Changing scene to: " + newScene);
        if (nameMapScene.get(newScene) == null) {
            System.out.println("Warning: couldn't find the specified scene");
            return;
        }
        currentScene = nameMapScene.get(newScene);
        window.setScene(currentScene);
        window.sizeToScene();
        window.show();
        if (newScene.equals("gameBoard4Players.fxml") || newScene.equals("gameSettings.fxml")) {
            window.setResizable(false);
        } else {
            window.setResizable(true);
        }
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

    public void setupConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            setClientReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Warning: couldn't complete connection setup");
        }
    }

    public void notifyCurrentScene(String jsonMessage) {
        Set<String> currentController = nameMapScene.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), currentScene))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Iterator<String> iterator = currentController.iterator();
        if (!iterator.hasNext()) {
            throw new RuntimeException("Couldn't find the current scene controller");
        }
        GUIController controller = nameMapController.get(iterator.next());
        if (iterator.hasNext()) {
            throw new RuntimeException("There is more than 1 scene with the same name, this shouldn't be possible");
        }

        controller.updateFromServer(jsonMessage);
    }

    public void setFullScreen() {
        window.setFullScreen(true);
    }

    // PRIVATE METHODS

    /**
     * This method creates all the scenes of the App.
     * Each stage scene is put inside an hashmap, which links their name to their fxml filename.
     */
    private void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(LOGIN, GAME_BOARD, SETTINGS, LOADING, GAME_BOARD_4, PRODUCTIONS));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameMapScene.put(path, new Scene(loader.load()));
                nameMapScene.get(path).setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/graphics/cursor.png"))));
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
        currentScene.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/graphics/cursor.png"))));
        window.show();
    }

    // GETTERS

    public ClientView getClientView() {
        return clientView;
    }

    public Scene getSceneByFileName(String fileName) {
        return nameMapScene.get(fileName);
    }

    //SETTERS

    private void setClientReader(BufferedReader in) {
        clientReader = new ClientReader(in, clientView, new CountDownLatch(1), this);
        Thread readerThread = new Thread(clientReader);
        readerThread.start();
    }
}