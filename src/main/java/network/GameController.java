package network;

import com.google.gson.Gson;
import exceptions.ParametersNotValidException;
import exceptions.network.GameFullException;
import exceptions.network.PlayerNumberAlreadySetException;
import exceptions.network.UnknownPlayerNumberException;
import model.Game;
import model.UserCommandsInterface;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameController {
    private final Gson gson;
    private Game game;
    private Map<String, PrintWriter> players;
    int numOfPlayers;

    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut) {
        this.gson = new Gson();
        this.numOfPlayers = 0;

        this.players = new HashMap<>();
        players.put(username, userOut);
        //TODO rispondere chiedendo al giocator e di inviare il numero di giocatori
    }

    //PUBLIC METHODS

    public void readCommand(String username, Command command) {
        if (!username.equals(getCurrentPlayerUsername())) {
            //TODO rispondere con messaggio errore
            players.get(username).println("Ur not the right dude, my dude.");
        } else {
            String result = command.runCommand(this);
            if (result != null) {
                //TODO rispondere con messaggio errore
                players.get(username).println(result);
            }
        }
    }

    public void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException {
        if (numOfPlayers == 0)
            throw new UnknownPlayerNumberException();
        if (players.size() >= numOfPlayers)
            throw new GameFullException();

        players.put(username, userOut);
        checkGameStart();
    }

    //TODO non hardcodare il numero max di giocatori?
    public void choosePlayerNumber(int number) throws PlayerNumberAlreadySetException {
        if (numOfPlayers > 0) {
            throw new PlayerNumberAlreadySetException();
        }
        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        numOfPlayers = number;
        checkGameStart();
    }

    public boolean isFull() {
        if (numOfPlayers == 0 || players.size() < numOfPlayers) {
            return false;
        }
        return true;
    }

    //PRIVATE METHODS

    private void checkGameStart() {
        if (players.size() != numOfPlayers)
            return;

        game = new Game(players.keySet());
        //TODO Broadcastare a tutti i giocatori che la partita è iniziata (forse creare canale broadcast) e inviare tutti i bean
    }

    //GETTERS

    public UserCommandsInterface getGameInterface() {
        return game;
    }

    public String getCurrentPlayerUsername() {
        return game.getCurrentPlayer().getUsername();
    }

    public Set<String> getPlayersUsernames() {
        return players.keySet();
    }
}