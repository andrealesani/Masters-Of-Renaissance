package network;

import Exceptions.network.GameFullException;
import Exceptions.network.UnknownPlayerNumberException;
import Exceptions.network.UsernameAlreadyExistsException;

import java.io.PrintWriter;
import java.util.List;

public class GameLobby {
    private List<GameController> currentGames;
    private GameController newGame;

    public GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, GameFullException, UsernameAlreadyExistsException {

        for (GameController game : currentGames) {
            if (newGame.getPlayersUsernames().contains(username)) {
                throw new UsernameAlreadyExistsException();
            }
        }

        if (newGame == null) {

            newGame = new GameController(username, userOut);
            currentGames.add(newGame);

        } else {

            newGame.addPlayer(username, userOut);

            if (newGame.isFull()) {
                GameController temp = newGame;
                newGame = null;
                return temp;
            }
        }

        return newGame;
    }
}
