package network;

import exceptions.network.GameFullException;
import exceptions.network.UnknownPlayerNumberException;
import model.Game;

import java.io.PrintWriter;
import java.util.List;

public class GameLobby {
    private List<GameController> currentGames;
    private GameController newGame;

    public GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, GameFullException {
        //TODO Check that the username isn't already in use
        if (newGame == null) {
            newGame = new GameController(username, userOut);
            currentGames.add(newGame);
            return newGame;
        } else {
            newGame.addPlayer(username, userOut);
            if (newGame.isFull()) {
                newGame = null;
            }
        }

        return newGame;
    }
}
