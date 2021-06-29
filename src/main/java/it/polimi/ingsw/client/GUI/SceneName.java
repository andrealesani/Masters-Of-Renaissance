package it.polimi.ingsw.client.GUI;

/**
 * This enum contains all javaFX scenes employed in the GUI
 */
public enum SceneName {
    /**
     * The host and port selection screen
     */
    HOST_AND_PORT,
    /**
     * The username and number of players selection screen
     */
    SETTINGS,
    /**
     * The screen for waiting for a game's players to join
     */
    WAITING,
    /**
     * The game board screen
     */
    GAME_BOARD,
    /**
     * The production selection screen
     */
    PRODUCTIONS,
    /**
     * The game over screen
     */
    GAME_OVER;

    /**
     * Fetches the scene's file's name
     *
     * @return the scene's file name
     */
    public String getFileName() {
        String result = null;

        switch (this) {
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
