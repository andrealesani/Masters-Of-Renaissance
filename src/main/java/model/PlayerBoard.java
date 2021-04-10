package model;

import Exceptions.*;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents one single physical game player board. It holds all the information about a player's status
 * during the game.
 */
public class PlayerBoard {
    private final Game game;
    private final String username;
    private final List<PopeFavorTile> popeFavorTiles;
    private final Warehouse warehouse;
    private final UnlimitedStorage strongbox;
    private final UnlimitedStorage waitingRoom;
    private int whiteMarbleNum = 0;
    private final List<ResourceType> marbleConversions;
    private final Map<ResourceType, Integer> discounts;
    private final List<List<DevelopmentCard>> cardSlots;
    private final List<LeaderCard> leaderCards;
    private final ProductionHandler productionHandler;
    private int faith;
    private boolean productionsAreConfirmed;

    /**
     * Constructor
     *
     * @param game     reference to the game the player is playing
     * @param username nickname that the player chose in the lobby
     */
    public PlayerBoard(Game game, String username, int numOfDepots, List<PopeFavorTile> popeFavorTiles) {
        this.game = game;
        this.username = username;
        faith = 0;
        this.popeFavorTiles = popeFavorTiles;
        warehouse = new Warehouse(numOfDepots);
        waitingRoom = new UnlimitedStorage();
        strongbox = new UnlimitedStorage();
        marbleConversions = new ArrayList<>();
        discounts = new HashMap<>();
        cardSlots = new ArrayList<List<DevelopmentCard>>();
        leaderCards = new ArrayList<>();
        productionHandler = new ProductionHandler();
    }

    /**
     * Getter for the player's warehouse
     *
     * @return - returns the player's warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Temporarily stores the given amount of the given resource in the waiting room
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add
     */
    public void addResourceToWarehouse(ResourceType resource, int quantity) {
        waitingRoom.addResource(resource, quantity);
    }

    public void swapDepotContent (int depotNumber1, int depotNumber2) throws ParametersNotValidException, SwapNotValidException, DepotNotPresentException {
        warehouse.swapDepotContent(depotNumber1, depotNumber2);
    }

    /**
     * Adds the given amount of the given resource to the strongbox
     * @param resource the resource to be added
     * @param quantity the amount of the resource to add
     */
    public void addResourceToStrongbox(ResourceType resource, int quantity) {
        strongbox.addResource(resource, quantity);
    }

    public void takeResourceFromWarehouse (int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException {
        int debt = productionHandler.getDebt(resource);
        if (quantity>debt) {
            quantity=debt;
        }
        warehouse.removeFromDepot(depotNumber, resource.getType(), quantity);
        try {
            productionHandler.takeResource(this, resource, quantity);
        } catch (ResourceNotPresentException ex) {

        }
    }

    public void takeResourceFromStrongbox (Resource resource, int quantity) throws NotEnoughResourceException {
        int debt = productionHandler.getDebt(resource);
        if (quantity>debt) {
            quantity=debt;
        }
        strongbox.removeResource(resource.getType(), quantity);
        try {
            productionHandler.takeResource(this, resource, quantity);
        } catch (ResourceNotPresentException ex) {
            //This should never happen
        }
    }

    /**
     * Depending on the number of available marble conversions: does nothing if there are zero, adds a resource of the corresponding type to the waiting room if there is one, and adds a white orb resource to the waiting room if there are multiple
     */
    public void addWhiteMarble() {
        if (marbleConversions.size() == 1) {
            waitingRoom.addResource(marbleConversions.get(0), 1);
        } else if (marbleConversions.size()>1) {
            whiteMarbleNum++;
        }
    }

    /**
     * Moves the given amount of the given resource from the waiting room to the selected depot
     *
     * @param depot    the number of the depot to which to add the resource
     * @param resource the resource to be moved
     * @param quantity the amount of the resource to be moved
     * @throws DepotNotPresentException   if the number of the target depot does not correspond to any depot in the warehouse
     * @throws WrongResourceTypeException if the type of the resource to be added cannot (currently) be added to the target depot
     * @throws NotEnoughSpaceException    if the quantity of the resource to be added plus the amount already stored in the target depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException   if the depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    public void sendResourceToDepot(int depot, ResourceType resource, int quantity) throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException, DepotNotPresentException {
        warehouse.addToDepot(depot, resource, quantity);
    }

    /**
     * Converts the given amount of white orbs in waiting room into the given resource from the available marble conversions
     *
     * @param resource the resource into which to convert the white orb
     * @param quantity the amount of white orbs to convert
<<<<<<< Updated upstream
     * @throws ResourceNotPresentException     if there are no white orbs in the waiting room
     * @throws NotEnoughResourceException      if there are less white orbs in the waiting room than the amount to be converted
=======
<<<<<<< HEAD
     * @throws NotEnoughResourceException if there are less white orbs in the waiting room than the amount to be converted
=======
     * @throws ResourceNotPresentException     if there are no white orbs in the waiting room
     * @throws NotEnoughResourceException      if there are less white orbs in the waiting room than the amount to be converted
>>>>>>> 9f4f30bb7bf7e044c5e2434687eb738d222118d2
>>>>>>> Stashed changes
     * @throws ConversionNotAvailableException if the conversion to the given resource is not available
     */
    public void chooseMarbleConversion(ResourceType resource, int quantity) throws NotEnoughResourceException, ConversionNotAvailableException {
        if (!marbleConversions.contains(resource)) {
            throw new ConversionNotAvailableException();
        }
        int newQuantity = whiteMarbleNum - quantity;
        if (newQuantity < 0) {
            throw new NotEnoughResourceException();
        }
        waitingRoom.addResource(resource, quantity);
        whiteMarbleNum = newQuantity;
    }

    /**
     * @param resource specifies the Resource type to count
     * @return returns the total amount of the player's Resources distributed both in his strongbox and his warehouse
     */
    public int getNumOfResource(ResourceType resource) {
        int sum = 0;
        sum += warehouse.getNumOfResource(resource);
        sum += strongbox.getNumOfResource(resource);
        return sum;
    }


    /**
     * Increases the player's faith by the given amount
     */

    public void increaseFaith(int quantity) {
        faith += quantity;
    }

    public int getNewTriggeredTile(int lastTriggeredTile) {
        int newTriggeredTile = 0;
        for (int tileNumber = lastTriggeredTile; tileNumber<=popeFavorTiles.size(); tileNumber++) {
            if (popeFavorTiles.get(tileNumber).isTriggered(faith)) {
                newTriggeredTile = tileNumber;
            }
        }
        return newTriggeredTile;
    }

    /**
     * The vatican will never be the same again
     */
    public void theVaticanReport(int newTriggeredTile, int lastTriggeredTile) {
        for (int tileNumber = lastTriggeredTile; tileNumber<=newTriggeredTile; tileNumber++) {
                popeFavorTiles.get(tileNumber).checkActivation(faith);
        }
    }

    /**
     * Adds the specified DevelopmentCard to the specified slot in the player's PlayerBoard
     *
     * @param slot            specifies to which of the 3 production slots the Card must be added
     * @param developmentCard card that must be added
     */
    public void addDevelopmentCard(int slot, DevelopmentCard developmentCard) {
        cardSlots.get(slot).add(developmentCard);
    }

    /**
     * Adds the specified LeaderCard to the player's leaderCards list
     *
     * @param leaderCard specifies the LeaderCard to add
     */
    public void addLeaderCard(LeaderCard leaderCard) {
        leaderCards.add(leaderCard);
    }

    /**
     * Checks if the player can activate a certain LeaderCard and activates it
     *
     * @param i specifies the position of the LeaderCard in the leaderCards list
     * @throws RequirementsNotMetException thrown if the player does not fulfill the requirements to activate the specified LeaderCard
     */
    public void playLeaderCard(int i) throws RequirementsNotMetException {
        if (leaderCards.get(i).areRequirementsMet(this)) {
            leaderCards.get(i).doAction(this);
        } else throw new RequirementsNotMetException();
    }

    /**
     * Adds the specified Resource to the marbleConversion list so that the player can convert a WhiteOrb
     * into the Resource whenever he picks it from the Market
     *
     * @param resource Resource (specified in the LeaderCard) that the WhiteOrb can be transformed into from now on
     */
    public void addMarbleConversion(ResourceType resource) {
        marbleConversions.add(resource);
    }

    /**
     * Adds the specified Production to the ProductionHandler so that the player can activate it just like
     * any other Production he already owns
     *
     * @param production Production (specified in the LeaderCard) that can be activated from now on
     */
    public void addProduction(Production production) {
        productionHandler.addProduction(production);
    }

    /**
     * Adds the specified discount to the discounts list so that the player can pay less
     * Resources when buying DevelopmentCards
     *
     * @param resourceType specifies the Resource affected by the discount
     * @param discount     specifies the amount of Resources discounted
     */
    public void addDiscount(ResourceType resourceType, int discount) {
        discounts.put(resourceType, discount);
    }

    /**
     * This method is called when a player decides to discard one of his two LeaderCards in order to get one faith point
     *
     * @param i specifies the position of the LeaderCard in the leaderCards list
     */
    public void discardLeaderCard(int i) {
        leaderCards.remove(i);
        increaseFaith(1);
    }

    /**
     * @param cardColor specifies the CardColor of the cards to count
     * @param level     specifies the level of the card to count
     * @return the total number of DevelopmentCards owned by the player that fulfill both the color and the level requirements
     */
    public int getNumOfCards(CardColor cardColor, int level) {
        int num = 0;
        for (List<DevelopmentCard> deck : cardSlots) {
            for (DevelopmentCard developmentCard : deck) {
                if (developmentCard.getColor() == cardColor && developmentCard.getLevel() == level) {
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * atm this method is super dumb but robust. It depends on the method above (cringe)
     *
     * @param cardColor specifies the CardColor of the cards to count
     * @return returns the total number of DevelopmentCards owned by the player that fulfill the color requirement
     */
    public int getNumOfCards(CardColor cardColor) {
        int num = 0;
        for (int i = 1; i <= 3; i++) { /* number of card levels in temporarily hard coded */
            num = num + getNumOfCards(cardColor, i);
        }
        return num;
    }

    /**
     * Returns the number of resources still present in waiting room
     *
     * @return the number of resources left
     */
    public int leftInWaitingRoom() {
        List<ResourceType> leftovers = waitingRoom.getStoredResources();
        int sum = 0;
        for (ResourceType resource : leftovers) {
            sum += waitingRoom.getNumOfResource(resource);
        }
        sum += whiteMarbleNum;
        return sum;
    }

    public void clearWaitingRoom() {
        waitingRoom.clear();
    }

    public boolean isGameEnding() {
        //TODO
        return false;
    }
}
