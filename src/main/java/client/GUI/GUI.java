package client.GUI;

import client.ClientReader;
import client.ClientView;
import client.GUI.controllers.GUIController;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.Command;
import network.beans.MessageWrapper;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * This class represents the executable for the game's Graphical User Interface on the client machine
 */
public class GUI extends Application {
    /**
     * The Scene being currently displayed
     */
    private Scene currentScene;
    /**
     * The window that displays the scenes to screen
     */
    private Stage window;
    /**
     * The socket for the connection with the server
     */
    private Socket clientSocket;
    /**
     * This object handles the reception of messages from the server
     */
    private ClientReader clientReader;
    /**
     * The buffer for the messages received from the server
     */
    private BufferedReader in;
    /**
     * The printer for sending messages to the server
     */
    private PrintWriter out;
    /**
     * This object holds all the information about the game state
     */
    private final ClientView clientView;
    /**
     * Maps each SceneName to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<SceneName, Scene> nameMapScene = new HashMap<>();
    /**
     * Maps each SceneName to the effective controller object for that scene, in order to get the correct controller for modifying operations.
     */
    private final HashMap<SceneName, GUIController> nameMapController = new HashMap<>();
    /**
     * Json serializer
     */
    private final Gson gson;

    // CONSTRUCTOR

    /**
     * The class constructor
     */
    public GUI() {
        clientView = new ClientView();
        gson = new Gson();
    }

    //MAIN

    /**
     * Main method of the GUI, which is called from the launcher in case user chooses GUI from the options.
     *
     * @param args of type String[] - parsed arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    // JAVA FX METHODS

    /**
     * Starts the GUI
     *
     * @param stage the Stage class
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        window.setMinWidth(400);
        window.setMinHeight(600);
        window.setResizable(true);
        // If we want to add special fonts we should do it here
        run();
    }

    /**
     * Stops the GUI from running
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
     * @param newSceneName represents the name of the scene to be shown
     */
    public void changeScene(SceneName newSceneName) {
        System.out.println("Changing scene to: " + newSceneName.getFileName());

        if (nameMapScene.get(newSceneName) == null) {
            System.err.println("Warning: couldn't find the specified scene");
            stop();
        }

        currentScene = nameMapScene.get(newSceneName);
        window.setScene(currentScene);
        window.sizeToScene();
        window.show();

        switch (newSceneName) {
            case HOST_AND_PORT -> {
                window.setResizable(true);
                window.setMinWidth(400);
                window.setMinHeight(600);
            }
            case SETTINGS -> {
                window.setResizable(true);
                window.setMinWidth(700);
                window.setMinHeight(480);
            }
            case WAITING -> {
                window.setResizable(true);
                window.setMinWidth(600);
                window.setMinHeight(550);
            }
            case GAME_OVER -> {
                window.setResizable(false);
                window.setMinWidth(689);
                window.setMinHeight(382);
            }
            case GAME_BOARD -> window.setResizable(false);

        }
    }

    /**
     * This method sends a string message to the server.
     * It is supposed to be called from the scenes' controllers
     *
     * @param message the command to be sent
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * This method sends a command to the server.
     * It is supposed to be called from the scenes' controllers
     *
     * @param command the command to be sent
     */
    public void sendCommand(Command command) {
        out.println(gson.toJson(command));
    }

    public void setupConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            setClientReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Warning: couldn't complete connection setup");
            stop();
        }
    }

    /**
     * Forwards the given string (json received from the server) to the currently active scene
     *
     * @param response the json message sent by the server
     */
    public void notifyCurrentScene(MessageWrapper response) {
        Set<SceneName> currentController = nameMapScene.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), currentScene))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Iterator<SceneName> iterator = currentController.iterator();
        if (!iterator.hasNext()) {
            System.err.println("Warning: Couldn't find the current scene controller");
            stop();
        }
        GUIController controller = nameMapController.get(iterator.next());
        if (iterator.hasNext()) {
            System.err.println("Warning: There is more than 1 scene with the same name, this shouldn't be possible");
            stop();
        }

        try {
            controller.updateFromServer(response);
        } catch (Exception ex) {
            System.err.println("Warning: Exception thrown when updating current scene: " + ex.getMessage());
            stop();
        }
    }

    /**
     * Sets the window to full screen mode
     */
    public void setFullScreen() {
        window.setFullScreen(true);
    }

    // PRIVATE METHODS

    /**
     * This method creates all the scenes of the App.
     * Each stage scene is put inside two hashmaps, which link their name to their scene object and controller respectively.
     */
    private void setup() {
        SceneName[] fxmList = SceneName.values();
        try {
            for (SceneName sceneName : SceneName.values()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName.getFilePath()));
                //Creates actual scene for this scene name
                Scene scene = new Scene(loader.load());
                scene.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/graphics/cursor.png"))));
                nameMapScene.put(sceneName, scene);
                GUIController controller = loader.getController();
                //Sets the scene's controller
                controller.setGui(this);
                nameMapController.put(sceneName, controller);
            }
        } catch (IOException e) {
            System.out.println("Warning: scenes setup failed");
            stop();
        }
        currentScene = nameMapScene.get(SceneName.HOST_AND_PORT);
    }

    /**
     * Sets the title of the main stage and launches the window.
     */
    private void run() {
        window.setTitle("Masters Of Renaissance");
        window.setScene(currentScene);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/punchboard/calamaio.png")));
        currentScene.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/graphics/cursor.png"))));
        window.show();
    }

    // GETTERS

    /**
     * Getter for the client's ClientView object
     *
     * @return the client's ClientView
     */
    public ClientView getClientView() {
        return clientView;
    }

    /**
     * Getter for a Scene object corresponding to a given SceneName
     *
     * @param sceneName the name of the Scene
     * @return the Scene object
     */
    public Scene getSceneBySceneName(SceneName sceneName) {
        return nameMapScene.get(sceneName);
    }

    /**
     * Getter for a GUIController object corresponding to a given SceneName
     *
     * @param sceneName the name of the Scene
     * @return the corresponding GUIController object
     */
    public GUIController getControllerBySceneName(SceneName sceneName) {
        return nameMapController.get(sceneName);
    }

    //SETTERS

    /**
     * Creates a new ClientReader class with the given input buffer
     *
     * @param in the buffer for messages received from the server
     */
    private void setClientReader(BufferedReader in) {
        clientReader = new ClientReader(in, clientView, this);
        Thread readerThread = new Thread(clientReader);
        readerThread.start();
    }
}