package model;

import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents one single physical game playerboard. It holds all the information about a player's status
 * during the game.
 */
public class PlayerBoard {
    private Game game;
    private String username;
    private int faith;
    private List<PopeTileState> popeFavorTiles = new ArrayList<>();
    private Warehouse warehouse = new Warehouse();
    private UnlimitedStorage strongbox = new UnlimitedStorage();
    private UnlimitedStorage waitingRoom = new UnlimitedStorage();
    private List<Resource> marbleConversions = new ArrayList<>();
    private Map<Resource, Integer> discounts = new HashMap<>();
    private List<List<DevelopmentCard>> cardSlots; //da istanziare
    private List<LeaderCard> leaderCards = new ArrayList<>();
    private ProductionHandler productionHandler = new ProductionHandler();

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void addResourceToWarehouse(Resource resource) {
        //TODO
    }

    public void addWhiteMarble() {
        //TODO
    }

    public void sendResourceToDepot(int quantity, Resource resource) {
        //TODO
    }

    public void chooseMarbleConversion(Resource resource) {
        //TODO
    }

    public void addDevelopmentCard(int quantity, Resource resource) {
        //TODO
    }

    public void addResourceToStrongbox(Resource resource) {
        //TODO
    }

    public void addLeaderCard(LeaderCard leaderCard) {
        //TODO
    }

    public void playLeaderCard(int quantity) {
        //TODO
    }

    public void addMarbleConversion(Resource resource) {
        //TODO
    }

    public void addProduction(Production production) {
        //TODO
    }

    public void addDiscont(Resource resource, int discount) {
        //TODO
    }

    public void discardLeaderCard(int quantity) { // l'int sta per una quantità?
        //TODO
    }

    public void faithIncrease() {
        faith++;
    }

    public void theVaticanReport() {
        //TODO
    }

    public int getNumOfResource(Resource resource) {
        //TODO
        return 0;
    }

    public int getNumOfCards(CardColor cardColor, int quantity) {
        //TODO
        return 0;
    }

    public int leftInWaitingRoom() {
        //TODO
        return 0;
    }

    public boolean isGameEnding() {
        //TODO
        return false;
    }
}
