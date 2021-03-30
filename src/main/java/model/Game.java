package model;

import model.card.leadercard.LeaderCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Model.Game master class
 */
public class Game {
    private Market market;
    private CardTable cardTable;
    private List<LeaderCard> leaderCards = new ArrayList<>();
    private List<PlayerBoard> playersTurnOrder = new ArrayList<>();
    private PlayerBoard currentPlayer;
    private Lorenzo lorenzo;

    private void initializeLeaders() {
        //TODO
    }

    private void assignInkwell() {
        //TODO
    }

    private void shuffleLeaderCards() {
        //TODO
    }

    private void faithIncreaseAll(int i) {
        //TODO
    }

    public PlayerBoard getCurrentPlayer() {
        return currentPlayer;
    }

    public void chooseLeaderCard(int i) {
        //TODO
    }

    public void checkPopeFavor() {
        //TODO
    }

    public void endCurrentTurn() {
        //TODO
    }
}
