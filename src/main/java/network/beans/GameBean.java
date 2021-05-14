package network.beans;

import com.google.gson.Gson;
import model.Game;
import model.Observer;
import model.TurnPhase;
import network.GameController;

public class GameBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    private String currentPlayer;
    private TurnPhase turnPhase;

    // CONSTRUCTOR

    public GameBean(GameController controller) {
        this.controller = controller;
        System.out.println("Game bean created");
    }

    // GETTERS

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    // SETTERS

    public void setCurrentPlayerFromGame(Game game) {
        currentPlayer = game.getCurrentPlayer().getUsername();
    }

    public void setTurnPhaseFromGame(Game game) {
        turnPhase = game.getTurnPhase();
    }


    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        Game game = (Game) observable;
        setCurrentPlayerFromGame(game);
        setTurnPhaseFromGame(game);

        controller.broadcastMessage(MessageType.GAME, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.GAME, gson.toJson(this));
    }

    @Override
    public String toString() {
        return "\u001B[32;1mGame State:\u001B[0m\n" +
                "   Current player is " + currentPlayer +
                " and we're in " + turnPhase + " phase\n";
    }
}
