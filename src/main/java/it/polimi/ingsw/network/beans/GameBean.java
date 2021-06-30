package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.leadercard.LeaderCard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.server.GameController;

import java.util.List;

/**
 * Class used to serialize a Game object, send it over the network and store its information in the client
 */
public class GameBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * The player that has to make a move
     */
    private String currentPlayer;
    /**
     * The turn order
     */
    private String[] turnOrder;
    /**
     * The players currently connected
     */
    private boolean[] connectedPlayers;
    /**
     * The turn phase that the current player is in
     */
    private TurnPhase turnPhase;
    /**
     * All the LeaderCards IDs. These are used to match the IDs sent by the server to the actual images stored in the client
     */
    private transient List<LeaderCard> leaderCards;
    /**
     * The winner of the game. Null if game is still running
     */
    private String winner;
    /**
     * The victory points of the winner.
     */
    private int winnerVp;
    /**
     * Boolean that tells if we're in the last turn of the game
     */
    private boolean isLastTurn;
    /**
     * Boolean parameters that is used by methods that need cards data to check if the cards have already been initialized
     */
    private transient boolean cardsInitialized = false;

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     */
    public GameBean(GameController controller) {
        this.controller = controller;
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        Game game = (Game) observable;
        setCurrentPlayerFromGame(game);
        setTurnOrderAndConnectedFromGame(game);
        setTurnPhaseFromGame(game);
        setLastTurn(game);
        setWinner(game);
        setWinnerVp(game);

        controller.broadcastMessage(MessageType.GAME_BEAN, gson.toJson(this));

        if (winner != null)
            controller.setGameOver();
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.GAME_BEAN, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        //If the game is over
        if (winner != null)
            return          "\n" +
                            Color.RESOURCE_STD + winner + Color.RESET + " wins the game with " +
                            Color.YELLOW_LIGHT_FG + winnerVp + Color.RESET + " points!" +
                            "\n" +
                            "The game has ended, type 'ESC + :q' to close the game" +
                            "\n";

        //If the game is not over
        String content =    "\n" +
                            Color.HEADER + "Game State:" + Color.RESET +
                            "\n";

        //If the game is in its last turn
        if (isLastTurn)
                    content += " " + Color.YELLOW_LIGHT_BG + Color.GREY_DARK_FG + "LAST TURN!" + Color.RESET;

        content +=  " Current player is " + Color.RESOURCE_STD + currentPlayer + Color.RESET +
                    " and we're in the " + turnPhase.colorDefinitionPrint() + " phase" +
                    "\n";

        return content;
    }

    // GETTERS

    /**
     * Getter for the game's player who should currently be taking their turn
     *
     * @return a String containing the current player's username
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Getter for the game's turn order
     *
     * @return an array of the player's usernames as Strings, following turn order
     */
    public String[] getTurnOrder() {
        return turnOrder.clone();
    }

    /**
     * Getter for the status of connection for the game's players
     *
     * @return an array of the player's connection status (true - connected, false - disconnected), following turn order
     */
    public boolean[] getConnectedPlayers() {
        return connectedPlayers.clone();
    }

    /**
     * Getter for the game's turn phase
     *
     * @return the game's current turn phase
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * Getter for the leader card corresponding to a specific card id.
     * The first time this method is called, LeaderCard objects are created
     *
     * @param id the id of the leader card
     * @return the LeaderCard with the given id
     * @throws CardNotPresentException if the given id does not correspond to any card
     */
    public LeaderCard getLeaderCardFromId(int id) throws CardNotPresentException {
        if (!cardsInitialized) {
            leaderCards = StaticMethods.getLeaderCardsFromJson();
            cardsInitialized = true;
        }

        for (LeaderCard leaderCard : leaderCards)
            if (leaderCard.getId() == id)
                return leaderCard;

        throw new CardNotPresentException();
    }

    /**
     * Returns the username of the game's winner
     *
     * @return the game's winner. 'null' if the game is not over
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Returns the number of victory points of the game's winner
     *
     * @return the winner's victory points. 'null' if the game is not over
     */
    public int getWinnerVp() {
        return winnerVp;
    }

    /**
     * Returns true if the game is on its last turn
     *
     * @return true if the game is on its last turn
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    // SETTERS

    /**
     * Sets the player who should be currently taking their turn
     *
     * @param game the object to take the information from
     */
    private void setCurrentPlayerFromGame(Game game) {
        if (game.getCurrentPlayer() != null)
            currentPlayer = game.getCurrentPlayer().getUsername();
        else
            currentPlayer = null;
    }

    /**
     * Sets the current turn phase
     *
     * @param game the object to take the information from
     */
    private void setTurnPhaseFromGame(Game game) {
        turnPhase = game.getTurnPhase();
    }

    /**
     * Sets the game's turn order and the connection status of its players
     *
     * @param game the object to take the information from
     */
    private void setTurnOrderAndConnectedFromGame(Game game) {
        List<PlayerBoard> playerBoards = game.getPlayersBoardsTurnOrder();
        turnOrder = new String[playerBoards.size()];
        connectedPlayers = new boolean[playerBoards.size()];

        for (int i = 0; i < turnOrder.length; i++) {
            PlayerBoard playerBoard = playerBoards.get(i);
            turnOrder[i] = playerBoard.getUsername();
            connectedPlayers[i] = playerBoard.isConnected();
        }
    }

    /**
     * Sets the game's winner
     *
     * @param game the object to take the information from
     */
    private void setWinner(Game game) {
        winner = game.getWinner();
    }

    /**
     * Sets the game's winners' victory points
     *
     * @param game the object to take the information from
     */
    private void setWinnerVp(Game game) {
        winnerVp = game.getWinnerVp();
    }

    /**
     * Sets the flag stating if the game is on its last turn
     *
     * @param game the object to take the information from
     */
    private void setLastTurn(Game game) {
        isLastTurn = game.isLastTurn();
    }
}
