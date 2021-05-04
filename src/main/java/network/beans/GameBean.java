package network.beans;

import com.google.gson.Gson;
import model.Game;
import model.Observer;
import model.TurnPhase;
import model.storage.Warehouse;

public class GameBean implements Observer {
    private String currentPlayer;
    private TurnPhase turnPhase;

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

        BeanWrapper beanWrapper = new BeanWrapper(BeanType.GAME, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
