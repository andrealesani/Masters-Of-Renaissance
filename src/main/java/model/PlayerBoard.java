package model;

import Exceptions.*;
import model.card.Card;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents one single physical game player board. It holds all the information about a player's status
 * during the game, and has methods for executing many of the users actions.
 */
public class PlayerBoard {
    /**
     * Attribute used to store the game to which the player belongs
     */
    private final Game game;
    /**
     * Attribute used to store the player's username
     */
    private final String username;
    /**
     * List used to store the player's pope's favor tiles
     */
    private final List<PopeFavorTile> popeFavorTiles;
    /**
     * Attribute used to store the player's warehouse, with all of its accessible depots
     */
    private final Warehouse warehouse;
    /**
     * Attribute used to store the player's production handler, an object use to store and activate available productions
     */
    private final ProductionHandler productionHandler;
    /**
     * Attribute used to store the player's strongbox
     */
    private final UnlimitedStorage strongbox;
    /**
     * Attribute used to store the player's resource waiting room, a temporary storage for resources obtained from the market for which they haven't yet chosen a final storage location
     */
    private final UnlimitedStorage waitingRoom;
    /**
     * Attribute used to store the number of white marbles obtained from the market for which the player still has to choose a conversion
     */
    private int whiteMarbleNum = 0;
    /**
     * Attribute used to store the player's faith score
     */
    private int faith;
    /**
     * List used to store all of the conversions for white marbles available to the player
     */
    private final List<ResourceType> marbleConversions;
    /**
     * Data structure used to map each resource for which the player has access to a discount, to the value of the corresponding discount
     */
    private final Map<ResourceType, Integer> discounts;
    /**
     * List used to store the player's slots for development cards, represented each as a list of development cards
     */
    private final List<List<DevelopmentCard>> cardSlots;
    /**
     * List used to store the player's available leader cards
     */
    private final List<LeaderCard> leaderCards;
    /**
     * Array used to store the faith values of the victory points faith tiles
     */
    private final int[] vpFaithTiles;
    /**
     * Array used to store the victory points awarded by the victory points faith tile of corresponding position
     */
    private final int[] vpFaithValues;
    /**
     * Attribute used to store the last faith value of the faith track
     */
    private final int finalFaith;
    /**
     * Attribute used to store the number of development cards the player can have before triggering the endgame
     */
    private final int devCardMax;

    /**
     * The class constructor
     *
     * @param game           reference to the game the player is playing
     * @param username       nickname that the player chose in the lobby
     * @param numOfDepots    the number of basic depots to be instantiated in the warehouse
     * @param popeFavorTiles a List of the player's pope's favor tiles
     */
    public PlayerBoard(Game game, String username, int numOfDepots, int finalFaith, int devCardMax, int[] vpFaithTiles, int[] vpFaithValues, List<PopeFavorTile> popeFavorTiles) {
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
        this.finalFaith = finalFaith;
        this.devCardMax = devCardMax;
        this.vpFaithTiles = vpFaithTiles;
        this.vpFaithValues = vpFaithValues;
        for (int i = 0; i < 3; i++)
            cardSlots.add(new ArrayList<DevelopmentCard>());
        leaderCards = new ArrayList<LeaderCard>();
        productionHandler = new ProductionHandler();
    }

    /**
     * Constructor FOR TESTING
     */
    public PlayerBoard() {
        game = null;
        username = null;
        faith = 0;
        popeFavorTiles = null;
        warehouse = new Warehouse(0);
        waitingRoom = new UnlimitedStorage();
        strongbox = new UnlimitedStorage();
        marbleConversions = new ArrayList<>();
        discounts = new HashMap<>();
        cardSlots = new ArrayList<>();
        finalFaith = 24;
        devCardMax = 7;
        vpFaithTiles = null;
        vpFaithValues = null;
        for (int i = 0; i < 3; i++)
            cardSlots.add(new ArrayList<DevelopmentCard>());
        leaderCards = new ArrayList<>();
        productionHandler = new ProductionHandler();
    }

    /**
     * Getter
     *
     * @return ProductionHandler
     */
    public ProductionHandler getProductionHandler() {
        return productionHandler;
    }

    /**
     * Getter for the player's username
     *
     * @return the player's username
     */
    public String getUsername () {return username;}

    /**
     * Getter for player's discounts
     *
     * @return the player's discount map
     */
    public Map<ResourceType, Integer> getDiscounts() {
        return discounts;
    }

    /**
     * Getter for player's strongbox
     *
     * @return the player's strongbox
     */
    public UnlimitedStorage getStrongbox() {
        return strongbox;
    }

    /**
     * Getter for the player's warehouse
     *
     * @return the player's warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Getter for the player's faith
     *
     * @return the player's faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter
     *
     * @return cardSlots
     */
    public List<List<DevelopmentCard>> getCardSlots() {
        return cardSlots;
    }

    /**
     * Getter
     *
     * @return marbleConversions list
     */
    public List<ResourceType> getMarbleConversions() {
        return marbleConversions;
    }

    /**
     * Getter
     *
     * @return all 4 player's LeaderCards from which he has to choose 2
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * Temporarily stores the given amount of the given resource in the waiting room, so that the player may decide in which depot to place it
     *
     * @param resource the resource to be added
     * @param quantity the amount of resource to add
     */
    public void addResourceToWarehouse(ResourceType resource, int quantity) {
        waitingRoom.addResource(resource, quantity);
    }

    /**
     * Swaps the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot
     * @param depotNumber2 the number of the second depot
     * @throws DepotNotPresentException    if one of the depot numbers given does not correspond with any depot
     * @throws ParametersNotValidException if the two inputs are the same number or below 1
     * @throws SwapNotValidException       if the content of one or both of the depots cannot be transferred to the other
     */
    public void swapDepotContent(int depotNumber1, int depotNumber2) throws ParametersNotValidException, SwapNotValidException, DepotNotPresentException {
        warehouse.swapDepotContent(depotNumber1, depotNumber2);
    }

    /**
     * Adds the given amount of the given resource to the strongbox
     *
     * @param resource the resource to be added
     * @param quantity the amount of the resource to add
     */
    public void addResourceToStrongbox(ResourceType resource, int quantity) {
        strongbox.addResource(resource, quantity);
    }

    /**
     * Checks if the player has the requisites for buying the selected development card.
     * If so, it adds the card to the player's requested slot and adds its production to the production handler.
     * The cost of the card is memorized in the waiting room, so that the player may choose where to take the resources from
     *
     * @param card the card to be bought by the player
     * @param slot the slot in which to put the card
     * @throws SlotNotValidException      if the selected slot cannot hold the selected card
     * @throws NotEnoughResourceException if the player does not have the resources necessary for buying the card
     */
    public void buyDevelopmentCard(DevelopmentCard card, int slot) throws SlotNotValidException, NotEnoughResourceException {
        List<DevelopmentCard> requestedSlot = cardSlots.get(slot - 1);
        int cardLevel = card.getLevel();

        //Checks if the selected slot can take a card of the given level
        if (cardLevel == 1) {
            if (!requestedSlot.isEmpty()) {
                throw new SlotNotValidException();
            }
        } else if (cardLevel > 1) {
            if (requestedSlot.isEmpty() || requestedSlot.get(requestedSlot.size() - 1).getLevel() != card.getLevel() - 1) {
                throw new SlotNotValidException();
            }
        }

        //Turns the cost into a map, and makes a set of the different resource types it contains
        List<ResourceType> cost = card.getCost();
        Map<ResourceType, Long> resourceQuantities =
                cost.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Set<ResourceType> resourcesInCost = resourceQuantities.keySet();

        //For every type in cost checks the quantity, applies discounts, then checks if there is enough of that resource type in storage to pay
        for (ResourceType resource : resourcesInCost) {

            int quantity = Math.toIntExact(resourceQuantities.get(resource));

            if (discounts.containsKey(resource)) {

                int newQuantity = quantity - discounts.get(resource);
                if (newQuantity >= 0) {
                    quantity = newQuantity;

                } else {
                    quantity = 0;
                }
            }

            if (resourceQuantities.get(resource) < getNumOfResource(resource)) {
                waitingRoom.clear();
                throw new NotEnoughResourceException();
            }

            waitingRoom.addResource(resource, quantity);
        }

        //Adds the new production and card, deleting the old production if an old card is covered
        if (cardLevel > 1) {
            productionHandler.removeProduction(requestedSlot.get(requestedSlot.size() - 1).getProduction());
        }
        productionHandler.addProduction(card.getProduction());
        requestedSlot.add(card);
    }

    /**
     * Removes the given amount of the given resource from the given depot, as part of the player's choice of where to take resources to pay for the cost of a development card.
     * If the player asks to pay a greater amount than that required by the cost, only the required amount is taken
     *
     * @param depotNumber the number of the depot from which to remove the resource
     * @param resource    the resource to remove
     * @param quantity    the amount of resource to remove
     * @throws NotEnoughResourceException if the given resource is not present in the target depot in the amount to be deleted
     * @throws DepotNotPresentException   if the number of the target depot does not correspond to any depot in the warehouse
     */
    public void takeResourceFromWarehouseCard(int depotNumber, ResourceType resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException {
        int debt = waitingRoom.getNumOfResource(resource);
        if (quantity > debt) {
            quantity = debt;
        }
        warehouse.removeFromDepot(depotNumber, resource, quantity);
        try {
            waitingRoom.removeResource(resource, quantity);
        } catch (NotEnoughResourceException ex) {
            //This should never happen
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Removes the given amount of the given resource from the player's strongbox, as part of the player's choice of where to take resources to pay for the cost a development card.
     * If the player asks to pay a greater amount than that required by the cost, only the required amount is taken
     *
     * @param resource the resource to remove
     * @param quantity the amount of resource to remove
     * @throws NotEnoughResourceException if the given resource is not present in the target depot in the amount to be deleted
     */
    public void takeResourceFromStrongboxCard(ResourceType resource, int quantity) throws NotEnoughResourceException {
        int debt = waitingRoom.getNumOfResource(resource);
        if (quantity > debt) {
            quantity = debt;
        }
        strongbox.removeResource(resource, quantity);
        try {
            waitingRoom.removeResource(resource, quantity);
        } catch (NotEnoughResourceException ex) {
            //This should never happen
            System.out.println(ex.getMessage());
        }
    }

    //TODO maybe unify takeResourceFrom* methods
    /**
     * Removes the given amount of the given resource from the given depot, as part of the player's choice of where to take resources to pay for the cost of their productions.
     * If the player asks to pay a greater amount than that required by the cost, only the required amount is taken
     *
     * @param depotNumber the number of the depot from which to remove the resource
     * @param resource    the resource to remove
     * @param quantity    the amount of resource to remove
     * @throws NotEnoughResourceException if the given resource is not present in the target depot in the amount to be deleted
     * @throws DepotNotPresentException   if the number of the target depot does not correspond to any depot in the warehouse
     */
    public void takeResourceFromWarehouseProduction(int depotNumber, Resource resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException {
        int debt = productionHandler.getDebt(resource);
        if (quantity > debt) {
            quantity = debt;
        }
        warehouse.removeFromDepot(depotNumber, resource.getType(), quantity);
        try {
            productionHandler.takeResource(this, resource, quantity);
        } catch (ResourceNotPresentException ex) {
            //This should never happen
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Removes the given amount of the given resource from the player's strongbox, as part of the player's choice of where to take resources to pay for the cost of their productions.
     * If the player asks to pay a greater amount than that required by the cost, only the required amount is taken
     *
     * @param resource the resource to remove
     * @param quantity the amount of resource to remove
     * @throws NotEnoughResourceException if the given resource is not present in the target depot in the amount to be deleted
     */
    public void takeResourceFromStrongboxProduction(Resource resource, int quantity) throws NotEnoughResourceException {
        int debt = productionHandler.getDebt(resource);
        if (quantity > debt) {
            quantity = debt;
        }
        strongbox.removeResource(resource.getType(), quantity);
        try {
            productionHandler.takeResource(this, resource, quantity);
        } catch (ResourceNotPresentException ex) {
            //This should never happen
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Selects the production with the given number as part of those to activate during this turn
     *
     * @param number the number of the production
     */
    public void selectProduction(int number) {
        productionHandler.selectProduction(number);
    }

    /**
     * Deselects all selected productions
     */
    public void resetProductionChoice() {
        productionHandler.resetProductionChoice();
    }

    /**
     * Checks if the player has converted all jollies in selected productions to other resources, and if they have enough resources to pay for productions
     */
    public void confirmProductionChoice() throws UnknownResourceException, NotEnoughResourceException {
        if (!productionHandler.resourcesAreEnough(this)) {
            throw new NotEnoughResourceException();
        }
    }

    /**
     * Converts a jolly resource in current production input into the given resource
     *
     * @param resource the resource into which to turn the jolly
     */
    public void chooseJollyInput(Resource resource) {
        productionHandler.chooseJollyInput (resource);
    }

    /**
     * Converts a jolly resource in current production output into the given resource
     *
     * @param resource the resource into which to turn the jolly
     */
    public void chooseJollyOutput(Resource resource) {
        productionHandler.chooseJollyOutput (resource);
    }

    public boolean isProductionInputEmpty() {
        return productionHandler.getCurrentInput().isEmpty();
    }


    /**
     * Depending on the number of available marble conversions: does nothing if there are zero, adds a resource of the corresponding type to the waiting room if there is one, and adds a white orb resource to the waiting room if there are multiple
     */
    public void addWhiteMarble() {
        if (marbleConversions.size() == 1) {
            waitingRoom.addResource(marbleConversions.get(0), 1);
        } else if (marbleConversions.size() > 1) {
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
     * @throws NotEnoughResourceException if the waiting room contains less than the given amount of the given resource
     */
    public void sendResourceToDepot(int depot, ResourceType resource, int quantity) throws BlockedResourceException, WrongResourceTypeException, NotEnoughSpaceException, DepotNotPresentException, NotEnoughResourceException {
        waitingRoom.removeResource(resource, quantity);
        warehouse.addToDepot(depot, resource, quantity);
    }

    /**
     * Converts the given amount of white orbs, from the amount waiting to be converted, into the given resource from the available marble conversions
     *
     * @param resource the resource into which to convert the white orb
     * @param quantity the amount of white orbs to convert
     * @throws NotEnoughResourceException      if there are less white orbs in the waiting room than the amount to be converted
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
     * Returns the amount of the given resource present in total in the player's storage
     *
     * @param resource specifies the Resource type to count
     * @return returns the total amount of the player's resource available both in his strongbox and his warehouse
     */
    public int getNumOfResource(ResourceType resource) {
        int sum = 0;
        sum += warehouse.getNumOfResource(resource);
        sum += strongbox.getNumOfResource(resource);
        return sum;
    }

    /**
     * Increases the player's faith score by the given amount
     *
     * @param quantity the amount by which to increase the player's faith
     */
    public void increaseFaith(int quantity) {
        faith += quantity;
    }

    /**
     * Checks the player's pope's favor tiles, starting from the one with the highest index to not have yet been triggered up until last turn.
     * Checks the necessity for a vatican report by checking the player's pope's favor tiles, starting from the one with the highest index to not have yet been triggered up until last turn.
     * A tile is considered 'triggered' once a player's faith score has reached or surpassed the tile's faith score.
     * Returns the tile with the highest index to have been triggered by the player during this turn (might be the same as the last)
     *
     * @param lastTriggeredTile the index of the tile the with the highest index that has been triggered (before this turn)
     * @return the index of the tile the with the highest index that has been triggered (during this turn)
     */
    public int getNewTriggeredTile(int lastTriggeredTile) {
        int newTriggeredTile = lastTriggeredTile;
        for (int tileNumber = lastTriggeredTile; tileNumber < popeFavorTiles.size(); tileNumber++) {
            if (popeFavorTiles.get(tileNumber).isTriggered(faith)) {
                newTriggeredTile = tileNumber + 1;
            }
        }
        return newTriggeredTile;
    }

    /**
     * The vatican will never be the same again.
     * <p>
     * Checks if the player is in the activation area for any of the tiles starting from the first to have not yet been flipped before this turn, up to the last one flipped during this turn.
     * During the check, each affected tile is either activated or discarded
     *
     * @param newTriggeredTile  the index of the tile the with the highest index that has been triggered (during this turn)
     * @param lastTriggeredTile the index of the tile the with the highest index that has been triggered (before this turn)
     */
    public void theVaticanReport(int newTriggeredTile, int lastTriggeredTile) {
        for (int tileNumber = lastTriggeredTile; tileNumber <= newTriggeredTile; tileNumber++) {
            popeFavorTiles.get(tileNumber).checkActivation(faith);
        }
    }

    /**
     *                  SOLO PER TESTING !!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * Adds the specified DevelopmentCard to the specified slot in the player's PlayerBoard
     *
     * @param slot            specifies to which production slot the Card must be added
     * @param developmentCard card that must be added
     */
    public void addDevelopmentCard(int slot, DevelopmentCard developmentCard) {
        cardSlots.get(slot - 1).add(developmentCard);
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
     * This method is called when a player decides to discard one of his two LeaderCards in order to get one faith point
     *
     * @param i specifies the position of the LeaderCard in the leaderCards list
     */
    public void discardLeaderCard(int i) {
        leaderCards.remove(i);
        increaseFaith(1);
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
        whiteMarbleNum = 0;
    }

    public boolean isGameEnding() {
        //24 faith, 7 DevelopmentCards
        if (faith >= finalFaith)
            return true;

        int devCardsNum = 0;
        for (List<DevelopmentCard> slot : cardSlots) {
            devCardsNum += slot.size();
        }
        if (devCardsNum >= devCardMax)
            return true;

        return false;
    }

    public int calculateVictoryPoints() {
        int vp = 0;
        //LeaderCards
        for (LeaderCard leaderCard : leaderCards) {
            if(leaderCard.isActive())
                vp += leaderCard.getVictoryPoints();
        }
        //DevelopmentCards
        for (List<DevelopmentCard> slot : cardSlots) {
            for (DevelopmentCard developmentCard : slot) {
                vp += developmentCard.getVictoryPoints();
            }
        }
        //Pope's favor tiles
        for (PopeFavorTile tile : popeFavorTiles) {
            vp += tile.getVictoryPoints();
        }
        //Faith track
        for (int i=0; i<vpFaithTiles.length; i++) {
            if (faith>=vpFaithTiles[i]) {
                vp += vpFaithValues[i];
            }
        }
        //Check every 5 Resources
        int resourceNum = 0;
        for (ResourceType resourceType : ResourceType.values()) {
            resourceNum += getNumOfResource(resourceType);
        }
        vp += resourceNum / 5;

        return vp;
    }

    public void chooseLeaderCard(int pos){

        if(leaderCards.get(pos - 1).isActive())
            leaderCards.get(pos-1).deActivate();
        else if(!leaderCards.get(pos - 1).isActive())
            leaderCards.get(pos-1).activate();

    }

    public int getActiveLeaderCards() {
        return Math.toIntExact(leaderCards.stream().filter(Card::isActive).count());
    }

    public void finishLeaderCardSelection() {
        int size = leaderCards.size();
        int i = 0;
        while (i<size) {
            LeaderCard card = leaderCards.get(i);
            if (!card.isActive()) {
                leaderCards.remove(card);
                size--;
            } else {
                card.deActivate();
                i++;
            }
        }
    }

}
