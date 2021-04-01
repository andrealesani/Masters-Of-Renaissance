package model;

import Exceptions.RequirementsNotMetException;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.storage.ResourceStash;
import model.storage.Warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents one single physical game player board. It holds all the information about a player's status
 * during the game.
 */
public class PlayerBoard {
    private Game game;
    private String username;
    private int faith;
    private List<PopeTileState> popeFavorTiles = new ArrayList<>();
    private Warehouse warehouse = new Warehouse();
    private ResourceStash strongbox = new ResourceStash();
    private ResourceStash waitingRoom = new ResourceStash();
    private List<Resource> marbleConversions = new ArrayList<>();
    private Map<Resource, Integer> discounts = new HashMap<>();
    private List<List<DevelopmentCard>> cardSlots = new ArrayList<List<DevelopmentCard>>();
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

    public void sendResourceToDepot(int depot, Resource resource) {
        //TODO
    }

    public void chooseMarbleConversion(Resource resource) {
        //TODO
    }

    public void addDevelopmentCard(int slot, DevelopmentCard developmentCard) {
        cardSlots.get(slot).add(developmentCard);
    }

    public void addResourceToStrongbox(Resource resource, int quantity) {
        strongbox.addResource(resource, quantity);
    }

    public void addLeaderCard(LeaderCard leaderCard) {
        leaderCards.add(leaderCard);
    }

    public void playLeaderCard(int i) throws RequirementsNotMetException{
        if (leaderCards.get(i).areRequirementsMet(this)) {
            leaderCards.get(i).doAction(this);
        } else throw new RequirementsNotMetException();
    }

    public void addMarbleConversion(Resource resource) {
        marbleConversions.add(resource);
    }

    public void addProduction(Production production) {
        productionHandler.addProduction(production);
    }

    public void addDiscount(Resource resource, int discount) {
        discounts.put(resource, discount);
    }

    public void discardLeaderCard(int i) {
        leaderCards.remove(i);
        faithIncrease();
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

    //TO TEST
    public int getNumOfCards(CardColor cardColor, int level) {
        int num = 0;
        for (List<DevelopmentCard> deck: cardSlots) {
            for (DevelopmentCard developmentCard: deck) {
                if(developmentCard.getColor() == cardColor && developmentCard.getLevel() == level){
                    num++;
                }
            }
        }
        return num;
    }

    /* atm this method is super dumb but robust. It depends on the method above */
    public int getNumOfCards(CardColor cardcolor) {
        int num = 0;
        for (int i = 1; i <= 3; i++) { /* number of card levels in temporarily hard coded */
            num = num + getNumOfCards(cardcolor, i);
        }
        return num;
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
