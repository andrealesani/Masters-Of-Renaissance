package model;

import model.card.leadercard.LeaderCard;
import model.lorenzo.Lorenzo;
import model.resource.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model.Game master class
 */
public class Game {
    private final Market market;
    private final CardTable cardTable;
    private final List<LeaderCard> leaderCards;
    private final List<PlayerBoard> playersTurnOrder;
    private PlayerBoard currentPlayer;
    private final Lorenzo lorenzo;

    /**
     * Constructor
     */
    public Game(List<String> nicknames) {
        market = new Market();
        cardTable = new CardTable();
        leaderCards = new ArrayList<>();
        playersTurnOrder = new ArrayList<>();
        lorenzo = new Lorenzo();

        for (String nickname: nicknames) {
            playersTurnOrder.add(new PlayerBoard(this, nickname));
        }
        assignInkwell();
        currentPlayer = playersTurnOrder.get(0);
    }

    /**
     * This method creates the instances of all the LeaderCards before the game starts
     */
    private void initializeLeaders() {
        //TODO
    }

    /**
     * This method assumes that playersTurnOrder list has been filled already. It randomly chooses the first player
     * by putting it first in playersTurnOrder list
     */
    private void assignInkwell() {
        int num = (int) (Math.random() * playersTurnOrder.size());
        for (int i = 0; i < num; i++) {
            playersTurnOrder.add(0, playersTurnOrder.get(playersTurnOrder.size() - 1));
        }
    }

    /**
     * This method shuffles the LeaderCards before distributing them to the players
     */
    private void shuffleLeaderCards() {
        Collections.shuffle(leaderCards);
    }

    /**
     * This method is called when a player's move makes all other players increase their faith
     * (for example when discarding resources)
     *
     * @param i number of faith points to add
     */
    private void faithIncreaseAll(int i) {
        for (PlayerBoard playerBoard : playersTurnOrder) {
            if (!playerBoard.equals(getCurrentPlayer())) {
                for (int j = 0; j < i; j++) {
                    playerBoard.faithIncrease();
                }
            }
        }
    }

    /**
     * @return current active player
     */
    public PlayerBoard getCurrentPlayer() {
        return currentPlayer;
    }

    //shufflare le leadercards
    //dividere il mazzo delle leader cards in 4 mazzetti (da dichiarare negli attributi e aggiungere nell'UML)
    public void chooseLeaderCard(int i) {
        //TODO
    }

    public void checkPopeFavor() {
        //TODO
    }

    //se solo mode attivare Lorenzo
    //controllare se il gioco è finito (sia solo mode sia multiplayer)
    //se il gioco non è finito e partita è multiplayer cambiare currentPlayer
    public void endCurrentTurn() {
        //TODO
    }
}
