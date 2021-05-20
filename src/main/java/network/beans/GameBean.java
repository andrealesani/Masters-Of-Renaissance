package network.beans;

import Exceptions.CardNotPresentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Color;
import model.Game;
import model.Observer;
import model.TurnPhase;
import model.card.leadercard.*;
import network.GameController;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
     * The victory points of the winner. -1 if game is still running
     */
    private int winnerVp;
    /**
     * Boolean parameters that is used by methods that need cards data to check if the cards have already been initialized
     */
    private transient boolean cardsInitialized = false;

    // CONSTRUCTOR

    public GameBean(GameController controller) {
        this.controller = controller;
    }

    // GETTERS

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public LeaderCard getLeaderCardFromId(int id) throws CardNotPresentException {
        if (!cardsInitialized) {
            setLeaderCardsFromJson();
            cardsInitialized = true;
        }
        for (LeaderCard leaderCard : leaderCards)
            if (leaderCard.getId() == id)
                return leaderCard;
        throw new CardNotPresentException();
    }

    public String getWinner() {
        return winner;
    }

    public int getWinnerVp() {
        return winnerVp;
    }

    // SETTERS

    public void setCurrentPlayerFromGame(Game game) {
        currentPlayer = game.getCurrentPlayer().getUsername();
    }

    public void setTurnPhaseFromGame(Game game) {
        turnPhase = game.getTurnPhase();
    }

    public void setWinner(Game game) {
        winner = game.getWinner();
    }

    public void setWinnerVp(Game game) {
        winnerVp = game.getWinnerVp();
    }

    private void setLeaderCardsFromJson() {
        //TODO controllare valori in input dal JSON (typo nelle enum, valori <0, etc)
        leaderCards = new ArrayList<>();
        Gson gson = new Gson();
        Reader reader = null;

        // depot leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/cards/leadercards/DepotLeaderCards.json"), StandardCharsets.UTF_8);
        Type DepotDecArray = new TypeToken<ArrayList<DepotLeaderCard>>() {
        }.getType();
        ArrayList<DepotLeaderCard> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // discount leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/cards/leadercards/DiscountLeaderCards.json"), StandardCharsets.UTF_8);
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {
        }.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // marble leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/cards/leadercards/MarbleLeaderCards.json"), StandardCharsets.UTF_8);
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleLeaderCard>>() {
        }.getType();
        ArrayList<MarbleLeaderCard> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // production leader cards
        reader = new InputStreamReader(this.getClass().getResourceAsStream("/cards/leadercards/ProductionLeaderCards.json"), StandardCharsets.UTF_8);
        Type ProductionDecArray = new TypeToken<ArrayList<ProductionLeaderCard>>() {
        }.getType();
        ArrayList<ProductionLeaderCard> productionLeaderCards = gson.fromJson(reader, ProductionDecArray);
        leaderCards.addAll(productionLeaderCards);

        int i = 1; // i++ prima passa i e poi lo incrementa => se voglio che id parta da 1 devo settare i a 1
        for (LeaderCard leaderCard : leaderCards) {
            leaderCard.setId(i++);
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        Game game = (Game) observable;
        setCurrentPlayerFromGame(game);
        setTurnPhaseFromGame(game);
        setWinner(game);
        setWinnerVp(game);

        controller.broadcastMessage(MessageType.GAME, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.GAME, gson.toJson(this));
    }

    @Override
    public String toString() {
        if (winner == null)
            return Color.HEADER + "\nGame State:\n" + Color.RESET +
                    " Current player is " + currentPlayer +
                    " and we're in " + turnPhase + " phase\n";
        else
            return Color.HEADER + winner + " wins the game with " + winnerVp + " points!\nThe game has ended, type 'ESC + :q' to close the game";
    }
}
