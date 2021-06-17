package client.GUI;

import model.Color;

/**
 * This enum contains all javaFX scenes employed in the GUI
 */
public enum SceneName {
    HOST_AND_PORT,
    SETTINGS,
    WAITING,
    GAME_BOARD,
    PRODUCTIONS,
    GAME_OVER;

    /**
     * Fetches the scene's file's name
     *
     * @return the scene's file name
     */
    public String getFileName() {
        String result = null;

        switch(this) {
            case HOST_AND_PORT -> result = "hostAndPort.fxml";
            case SETTINGS -> result = "gameSettings.fxml";
            case WAITING -> result = "waitingPlayers.fxml";
            case GAME_BOARD -> result = "gameBoard.fxml";
            case PRODUCTIONS -> result = "productions.fxml";
            case GAME_OVER -> result = "gameOver.fxml";
        }

        return result;
    }

    /**
     * Fetches the scene's file's path
     *
     * @return the scene's path
     */
    public String getFilePath() {
        return "/fxml/" + getFileName();
    }
}
