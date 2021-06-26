package model;

import Exceptions.*;
import model.card.Card;
import model.card.CardColor;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;
import model.resource.ResourceJolly;
import model.resource.ResourceType;
import model.storage.ResourceDepot;
import model.storage.UnlimitedStorage;
import model.storage.Warehouse;
import network.beans.SlotBean;

import java.util.*;
import java.util.stream.Collectors;

import static model.TurnPhase.CARDPAYMENT;
import static model.TurnPhase.PRODUCTIONPAYMENT;

/**
 * This class represents one single physical game player board. It holds all the information about a player's status
 * during the game, and has methods for executing many of the users actions.
 */
public class PlayerBoard implements Observable {
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
     * Map used to signal the correspondence between a leader depot numbers and their leader card
     */
    private final Map<Integer, Integer> leaderDepotCards;
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
     * Attribute used to indicate whether or not the player is connected
     */
    private boolean isConnected;
    /**
     * Attribute used to indicate whether or not the player has taken the first turn (chosen the leader cards and bonus resources)
     */
    private boolean hasTakenFirstTurn;

    //CONSTRUCTORS

    /**
     * The class constructor
     *
     * @param game           reference to the game the player is playing
     * @param username       nickname that the player chose in the lobby
     * @param popeFavorTiles a List of the player's pope's favor tiles
     * @param finalFaith     the faith amount that, once reached by the player, ends the game
     * @throws ParametersNotValidException if the parameters for the constructor are not valid
     */
    public PlayerBoard(Game game, String username, int finalFaith, List<PopeFavorTile> popeFavorTiles) {
        if (game == null || username == null || finalFaith <= 0 || popeFavorTiles == null) {
            throw new ParametersNotValidException();
        }

        //TODO make vpfaithtiles, vpfaithvalues, numofdepots, baseProduction, devCardMax and initialized in a JSON (maybe in PlayerBoard)
        int devCardMax = 7;
        int devCardSlots = 3;
        int numOfDepots = 3;
        int[] vpFaithTiles = {3, 6, 9, 12, 15, 18, 21, 24};
        int[] vpFaithValues = {1, 2, 4, 6, 9, 12, 16, 20};
        Resource jolly = new ResourceJolly();
        List<Resource> baseProdInput = new ArrayList<>();
        baseProdInput.add(jolly);
        baseProdInput.add(jolly);
        List<Resource> baseProdOutput = new ArrayList<>();
        baseProdOutput.add(jolly);
        Production baseProduction = new Production(0, baseProdInput, baseProdOutput);

        this.username = username;
        faith = 0;
        this.popeFavorTiles = popeFavorTiles;
        warehouse = new Warehouse(numOfDepots);
        waitingRoom = new UnlimitedStorage();
        strongbox = new UnlimitedStorage();
        marbleConversions = new ArrayList<>();
        discounts = new HashMap<>();
        cardSlots = new ArrayList<>();
        this.finalFaith = finalFaith;
        this.devCardMax = devCardMax;
        this.vpFaithTiles = vpFaithTiles;
        this.vpFaithValues = vpFaithValues;
        for (int i = 0; i < devCardSlots; i++)
            cardSlots.add(new ArrayList<>());
        leaderCards = new ArrayList<>();
        leaderDepotCards = new HashMap<>();
        productionHandler = new ProductionHandler();
        productionHandler.addProduction(baseProduction);

        isConnected = true;
        hasTakenFirstTurn = false;

        notifyObservers();
    }

    /**
     * Constructor FOR TESTING
     */
    public PlayerBoard() {
        username = null;
        faith = 0;
        popeFavorTiles = new ArrayList<>();
        warehouse = new Warehouse(3);
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
        leaderDepotCards = new HashMap<>();
        productionHandler = new ProductionHandler();

        isConnected = true;
        hasTakenFirstTurn = false;

        notifyObservers();
    }

    //PUBLIC METHODS

    //Resource addition methods

    /**
     * Temporarily stores the given amount of the given resource in the waiting room
     *
     * @param resources the resources to be added
     */
    public void addResourcesToWaitingRoom(Map<ResourceType, Integer> resources) {
        waitingRoom.addResources(resources);
    }

    /**
     * Adds the given amount of the given resource to the strongbox
     *
     * @param resources the resource to be added
     */
    public void addResourcesToStrongbox(Map<ResourceType, Integer> resources) {
        strongbox.addResources(resources);
    }


    /**
     * Increases the player's faith score by the given amount
     *
     * @param quantity the amount by which to increase the player's faith
     */
    public void addFaith(int quantity) {
        faith += quantity;

        notifyObservers();
    }

    /**
     * Depending on the number of available marble conversions: does nothing if there are zero, adds a resource of the corresponding type to the waiting room if there is one, and adds a white orb resource to the waiting room if there are multiple
     */
    public void addWhiteMarble(int quantity) {
        if (marbleConversions.size() == 1) {
            waitingRoom.addResources(Map.of(marbleConversions.get(0), quantity));
        } else if (marbleConversions.size() > 1) {
            whiteMarbleNum += quantity;
        }

        notifyObservers();
    }

    //Market's resources distribution methods

    /**
     * Converts the given amount of white orbs, from the amount waiting to be converted, into the given resource from the available marble conversions
     *
     * @param resource the resource into which to convert the white orb
     * @param quantity the amount of white orbs to convert
     * @throws NotEnoughResourceException      if there are less white orbs in the waiting room than the amount to be converted
     * @throws ConversionNotAvailableException if the conversion to the given resource is not available
     */
    public void chooseMarbleConversion(ResourceType resource, int quantity) throws NotEnoughResourceException, ConversionNotAvailableException {
        if (quantity < 0 || resource == null || !resource.canBeStored())
            throw new ParametersNotValidException();

        if (!marbleConversions.contains(resource)) {
            throw new ConversionNotAvailableException(resource);
        }

        int newQuantity = whiteMarbleNum - quantity;

        if (newQuantity < 0) {
            throw new NotEnoughResourceException();
        }
        waitingRoom.addResources(Map.of(resource, quantity));
        whiteMarbleNum = newQuantity;

        notifyObservers();
    }

    /**
     * Moves the given amount of the given resource from the waiting room to the selected depot
     *
     * @param depot    the number of the depot to which to add the resource
     * @param resource the resource to be moved
     * @param quantity the amount of the resource to be moved
     * @throws DepotNotPresentException        if the number of the target depot does not correspond to any depot in the warehouse
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the target depot
     * @throws NotEnoughSpaceException         if the quantity of the resource to be added plus the amount already stored in the target depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException        if the depot is affected by resource blocking and the resource is being blocked by a different depot
     * @throws NotEnoughResourceException      if the waiting room contains less than the given amount of the given resource
     */
    public void sendResourceToDepot(int depot, ResourceType resource, int quantity) throws BlockedResourceException, WrongResourceInsertionException, NotEnoughSpaceException, DepotNotPresentException, NotEnoughResourceException {
        int toPlace = waitingRoom.getNumOfResource(resource);
        if (quantity > toPlace) {
            quantity = toPlace;
        }

        warehouse.addToDepot(depot, resource, quantity);

        try {
            waitingRoom.removeResource(resource, quantity);
        } catch (NotEnoughResourceException ex) {
            //This should never happen
            System.err.println("BUG! Player tried to move to depot more resources than there were in waiting room!");
            ex.printStackTrace();
        }
    }

    /**
     * Swaps the contents of two warehouse depots
     *
     * @param depotNumber1 the number of the first depot
     * @param depotNumber2 the number of the second depot
     * @throws DepotNotPresentException if one of the depot numbers given does not correspond with any depot
     * @throws SwapNotValidException    if the content of one or both of the depots cannot be transferred to the other
     */
    public void swapDepotContent(int depotNumber1, int depotNumber2) throws SwapNotValidException, DepotNotPresentException {
        warehouse.swapDepotContent(depotNumber1, depotNumber2);
    }

    /**
     * Moves a certain amount of resource from one depot to another
     *
     * @param depotNumberTake the number of the depot from which to take the resources
     * @param depotNumberGive the number of the depot to which to move the resources
     * @param resource        the resource to move between the two depots
     * @param quantity        the quantity of the resource to move
     * @throws DepotNotPresentException        if one of the depot numbers given does not correspond with any depot
     * @throws ParametersNotValidException     if the two inputs are the same number or below 1
     * @throws NotEnoughResourceException      if the given resource is not present in the providing depot in the amount to be deleted
     * @throws WrongResourceInsertionException if the type of the resource to be added cannot (currently) be added to the receiving depot
     * @throws NotEnoughSpaceException         if the quantity of the resource to be added plus the amount already stored in the receiving depot exceeds the depot's maximum capacity
     * @throws BlockedResourceException        if the receiving depot is affected by resource blocking and the resource is being blocked by a different depot
     */
    public void moveDepotContent(int depotNumberTake, int depotNumberGive, ResourceType resource, int quantity) throws WrongTurnPhaseException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, NotEnoughResourceException, DepotNotPresentException {
        warehouse.moveDepotContent(depotNumberTake, depotNumberGive, resource, quantity);
    }

    //Development card purchase methods

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

            if (quantity > getNumOfResource(resource)) {
                waitingRoom.clear();
                throw new NotEnoughResourceException();
            }

            waitingRoom.addResources(Map.of(resource, quantity));
        }

        //Adds the new production and card, deleting the old production if an old card is covered
        if (cardLevel > 1) {
            try {
                productionHandler.removeProduction(requestedSlot.get(requestedSlot.size() - 1).getProduction());
            } catch (ProductionIsSelectedException | ProductionNotPresentException e) {
                e.printStackTrace();
            }
        }

        productionHandler.addProduction(card.getProduction());
        requestedSlot.add(card);

        notifyObservers();
    }

    /**
     * Adds the specified DevelopmentCard to the specified slot in the player's PlayerBoard without making any checks (ONLY FOR TESTING)
     *
     * @param slot            specifies to which production slot the Card must be added
     * @param developmentCard card that must be added
     */
    public void addDevelopmentCardNoCheck(int slot, DevelopmentCard developmentCard) {
        cardSlots.get(slot - 1).add(developmentCard);

        notifyObservers();
    }

    //Productions activation methods

    /**
     * Selects the production with the given number as part of those to activate during this turn
     *
     * @param number the number of the production
     */
    public void selectProduction(int number) throws ProductionNotPresentException {
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
    public void confirmProductionChoice() throws UndefinedJollyException, NotEnoughResourceException {
        if (!productionHandler.arePlayerResourcesEnough(this)) {
            throw new NotEnoughResourceException();
        }
        productionHandler.releaseInput(this);

        notifyObservers();
    }

    /**
     * Converts a jolly resource in current production input into the given resource
     *
     * @param resource the resource into which to turn the jolly
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    public void chooseJollyInput(Resource resource) throws ResourceNotPresentException {
        productionHandler.chooseJollyInput(resource);

        notifyObservers();
    }

    /**
     * Converts a jolly resource in current production output into the given resource
     *
     * @param resource the resource into which to turn the jolly
     * @throws ResourceNotPresentException if the productions' input does not contain any more jollies
     */
    public void chooseJollyOutput(Resource resource) throws ResourceNotPresentException {
        productionHandler.chooseJollyOutput(resource);

        notifyObservers();
    }

    /**
     * Prompts the board's production handler to release the active production's output to the player's storage
     */
    public void releaseProductionOutput() {
        productionHandler.releaseOutput(this);
        productionHandler.resetProductionChoice();

        notifyObservers();
    }

    //Vatican report handling methods

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
            } else {
                break;
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
        for (int tileNumber = lastTriggeredTile; tileNumber < newTriggeredTile; tileNumber++) {
            popeFavorTiles.get(tileNumber).checkActivation(faith);
        }

        notifyObservers();
    }

    //Dept payment methods

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
    public void takeResourceFromWarehouse(int depotNumber, ResourceType resource, int quantity) throws NotEnoughResourceException, DepotNotPresentException {
        int debt = waitingRoom.getNumOfResource(resource);

        if (quantity > debt)
            quantity = debt;

        warehouse.removeFromDepot(depotNumber, resource, quantity);

        try {
            waitingRoom.removeResource(resource, quantity);
        } catch (NotEnoughResourceException ex) {
            //This should never happen
            System.out.println("BUG! Tried to pay for a resource that was not required from warehouse!");
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
    public void takeResourceFromStrongbox(ResourceType resource, int quantity) throws NotEnoughResourceException {
        int debt = waitingRoom.getNumOfResource(resource);

        if (quantity > debt)
            quantity = debt;

        strongbox.removeResource(resource, quantity);

        try {
            waitingRoom.removeResource(resource, quantity);
        } catch (NotEnoughResourceException ex) {
            //This should never happen
            System.out.println("BUG! Tried to pay for a resource that was not required from strongbox!");
        }
    }

    //Leader card handling methods

    /**
     * Adds the specified LeaderCards to the player's leaderCards list
     *
     * @param newLeaderCards specifies the LeaderCards to add
     */
    public void addLeaderCards(List<LeaderCard> newLeaderCards) {
        this.leaderCards.addAll(newLeaderCards);
        notifyObservers();
    }

    /**
     * Checks if the player can activate a certain LeaderCard and activates it
     *
     * @param number specifies the position of the LeaderCard in the leaderCards list
     * @throws LeaderRequirementsNotMetException thrown if the player does not fulfill the requirements to activate the specified LeaderCard
     * @throws LeaderNotPresentException         if the number selected does not correspond to a leader card
     * @throws CardAlreadyActiveException        if the selected card is already active
     */
    public void playLeaderCard(int number) throws LeaderRequirementsNotMetException, LeaderNotPresentException, CardAlreadyActiveException {
        if (number <= 0)
            throw new ParametersNotValidException();
        if (number > leaderCards.size())
            throw new LeaderNotPresentException();

        if (leaderCards.get(number - 1).areRequirementsMet(this)) {
            leaderCards.get(number - 1).doAction(this);
            setLeaderDepotCards();
        } else throw new LeaderRequirementsNotMetException();

        notifyObservers();
    }

    /**
     * This method is called when a player decides to discard one of his two LeaderCards in order to get one faith point
     *
     * @param number specifies the position of the LeaderCard in the leaderCards list
     * @throws LeaderIsActiveException   if the leader card to be discarded has been activated
     * @throws LeaderNotPresentException if the number selected does not correspond to a leader card
     */
    public void discardLeaderCard(int number) throws LeaderIsActiveException, LeaderNotPresentException {
        if (number <= 0)
            throw new ParametersNotValidException();
        if (number > leaderCards.size())
            throw new LeaderNotPresentException();
        if (leaderCards.get(number - 1).isActive())
            throw new LeaderIsActiveException();

        leaderCards.remove(number - 1);
        addFaith(1);

        notifyObservers();
    }

    /**
     * Adds the specified Resource to the marbleConversion list so that the player can convert a WhiteOrb
     * into the Resource whenever he picks it from the Market
     *
     * @param resource Resource (specified in the LeaderCard) that the WhiteOrb can be transformed into from now on
     */
    public void addMarbleConversion(ResourceType resource) {
        marbleConversions.add(resource);

        notifyObservers();
    }

    /**
     * Adds the specified Production to the ProductionHandler so that the player can activate it just like
     * any other Production he already owns
     *
     * @param production Production (specified in the LeaderCard) that can be activated from now on
     */
    public void addProduction(Production production) {
        productionHandler.addProduction(production);

        notifyObservers();
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

        notifyObservers();
    }

    /**
     * Adds the given depot to the list of available depots in the warehouse
     *
     * @param depot the depot to be added
     */
    public void addNewDepot(ResourceDepot depot) {
        warehouse.addNewDepot(depot);

        notifyObservers();
    }


    //First turn methods

    /**
     * Adds the selected amount of white marbles to waiting room
     */
    public void addWhiteNoCheck(int quantity) {
        whiteMarbleNum += quantity;

        notifyObservers();
    }

    /**
     * Converts the given amount of white marbles into the given resource
     *
     * @param resource the resource into which to convert the white marbles
     * @param quantity the amount of resource to convert
     * @throws NotEnoughResourceException if there are not enough white marbles to get the required resource
     */
    public void chooseStartingResource(ResourceType resource, int quantity) throws NotEnoughResourceException {
        int newQuantity = whiteMarbleNum - quantity;
        if (newQuantity < 0) {
            throw new NotEnoughResourceException();
        }
        waitingRoom.addResources(Map.of(resource, quantity));
        whiteMarbleNum = newQuantity;

        notifyObservers();
    }

    /**
     * Selects or deselects the leader card with the given number, as part of the player's choice of which cards to keep from the initial deck
     *
     * @param number the number of the card to select or deselect
     * @throws LeaderNotPresentException if the number selected does not correspond to a leader card
     */
    public void chooseLeaderCard(int number) throws LeaderNotPresentException {
        if (number <= 0)
            throw new ParametersNotValidException();
        if (number > leaderCards.size())
            throw new LeaderNotPresentException();

        LeaderCard leaderCard = leaderCards.get(number - 1);

        if (leaderCard.isActive())
            leaderCard.deActivate();
        else if (!leaderCard.isActive())
            leaderCard.activate();

        notifyObservers();
    }

    /**
     * After the player has chosen which cards to keep, deactivates them and deletes the rest
     */
    public void finishLeaderCardSelection() {
        int size = leaderCards.size();
        int i = 0;
        while (i < size) {
            LeaderCard card = leaderCards.get(i);
            if (!card.isActive()) {
                leaderCards.remove(card);
                size--;
            } else {
                card.deActivate();
                i++;
            }
        }

        notifyObservers();
    }

    //End turn checks methods

    /**
     * Automatically handles payment for development cards or productions
     */
    public void automaticPayment() {
        boolean found;
        for (ResourceType resource : waitingRoom.getStoredResources()) {
            while (waitingRoom.getNumOfResource(resource) > 0) {
                found = false;
                for (int i = 1; i <= warehouse.getNumOfDepots(); i++) {
                    try {
                        warehouse.removeFromDepot(i, resource, 1);
                        waitingRoom.removeResource(resource, 1);
                        found = true;
                        break;
                    } catch (DepotNotPresentException ex) {
                        //This should never happen
                        System.out.println("BUG! Automatic payment from non existent depot.");
                    } catch (NotEnoughResourceException ignored) {
                    }
                }
                if (!found) {
                    try {
                        strongbox.removeResource(resource, 1);
                        waitingRoom.removeResource(resource, 1);
                    } catch (NotEnoughResourceException ex) {
                        //This should never happen
                        System.out.println("BUG! Automatic payment failed because player does not have enough resources.");
                    }
                }
            }
        }

    }

    /**
     * Returns whether or not the production handler's current input is empty, signaling the productions' price has been paid
     *
     * @return true if ProductionHandler's currentInput is empty
     */
    public boolean isProductionInputEmpty() {
        return productionHandler.getCurrentInput().isEmpty();
    }

    /**
     * Empties the content of the player's waiting room
     */
    public void clearWaitingRoom() {
        waitingRoom.clear();
        whiteMarbleNum = 0;
    }

    /**
     * Returns whether or not the player has meets the requirements for starting the ending phase of the game
     *
     * @return true if the final phase should start after this turn
     */
    public boolean isEndGameTime() {
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

    /**
     * Calculates the player's victory points
     *
     * @return the player's victory points
     */
    public int calculateVictoryPoints() {
        int vp = 0;
        //LeaderCards
        for (LeaderCard leaderCard : leaderCards) {
            if (leaderCard.isActive())
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
            vp += tile.getActiveVictoryPoints();
        }
        //Faith track
        for (int i = 0; i < vpFaithTiles.length; i++) {
            if (faith >= vpFaithTiles[i]) {
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

    // PERSISTENCE METHODS

    /**
     * Restores the player's faith score
     *
     * @param faith the player's faith
     */
    public void restoreFaith(int faith) {
        this.faith += faith;
    }

    /**
     * Restores the player's unconverted white marbles / bonus resources
     *
     * @param whiteMarbleNum the player's unconverted resources
     */
    public void restoreWhiteMarbleNum(int whiteMarbleNum) {
        this.whiteMarbleNum = whiteMarbleNum;
    }

    /**
     * Restores the player's card slots' development cards
     *
     * @param developmentCardsIDs an array of SlotBeans containing the player's development cards' IDs
     * @param game                the Game class
     */
    public void restoreCardSlots(SlotBean[] developmentCardsIDs, Game game) {
        for (int i = 0; i < developmentCardsIDs.length; i++) {
            for (int j = 0; j < developmentCardsIDs[i].getDevelopmentCards().length; j++)
                try {
                    DevelopmentCard card = game.findDevelopmentCardById(developmentCardsIDs[i].getDevelopmentCards()[j]);
                    cardSlots.get(i).add(card);
                    productionHandler.addProduction(card.getProduction());
                } catch (CardNotPresentException e) {
                    System.out.println("Warning: couldn't find one of player " + username + "'s DevelopmentCards during the game restore: ID " + developmentCardsIDs[i].getDevelopmentCards()[j]);
                }
        }
    }

    /**
     * Restores the player's leader cards and their activation effects
     *
     * @param leaderCardIDs     an array of the player's leader cards' IDs
     * @param activeLeaderCards an array flagging which leader cards were active
     * @param firstTurnTaken    a flag determining if the player has already chosen which leader cards to keep
     * @param game              the Game class
     */
    public void restoreLeaderCards(int[] leaderCardIDs, boolean[] activeLeaderCards, boolean firstTurnTaken, Game game) {
        leaderCards.clear();
        for (int i = 0; i < leaderCardIDs.length; i++)
            try {
                LeaderCard card = game.findLeaderCardById(leaderCardIDs[i]);

                this.leaderCards.add(card);
                if (activeLeaderCards[i])

                    if (firstTurnTaken)
                        try {
                            card.doAction(this);
                        } catch (CardAlreadyActiveException ignored) {
                        }
                    else
                        card.activate();


            } catch (CardNotPresentException e) {
                System.out.println("Warning: couldn't find one of the LeaderCards during the game restore: ID " + leaderCardIDs[i]);
            }

        setLeaderDepotCards();
    }

    /**
     * Restores the state of the player's pope's favor tiles
     *
     * @param popeTileState an array of the tile's states
     */
    public void restorePopeTileState(PopeTileState[] popeTileState) {
        for (int i = 0; i < popeTileState.length; i++)
            this.popeFavorTiles.get(i).restoreState(popeTileState[i]);
    }

    /**
     * Restores the flag stating if the player has chosen which leader cards to keep
     *
     * @param firstTurnTaken the flag for taking the preliminary turn
     */
    public void restoreFirstTurnTaken(boolean firstTurnTaken) {
        if (firstTurnTaken)
            setFirstTurnTaken();
    }

    /**
     * Executes any necessary end turn cleanup if the player was the current player
     *
     * @param turnPhase the game's turn phase at the last save
     * @param game the Game class
     */
    public void restoreEndTurn(TurnPhase turnPhase, Game game) {
        if (turnPhase == TurnPhase.MARKETDISTRIBUTION) {

            game.checkDiscarded(this);

        } else if (turnPhase == CARDPAYMENT) {

            automaticPayment();

        } else if (turnPhase == PRODUCTIONPAYMENT) {

            automaticPayment();
            releaseProductionOutput();

        }
    }

    // CONNECTION METHODS

    /**
     * Sets the player's connection status to connected
     */
    public void setConnectedStatus() {
        isConnected = true;
    }

    /**
     * Sets the player's connection status to disconnected
     */
    public void setDisconnectedStatus() {
        isConnected = false;
    }

    //TURN PHASE HANDLING METHODS

    /**
     * Flags the user as having taken their first turn
     */
    public void setFirstTurnTaken() {
        hasTakenFirstTurn = true;
    }

    //PRIVATE METHODS

    /**
     * Sets the correspondence between leader depots and their cards
     */
    private void setLeaderDepotCards() {

        int[] depotId = warehouse.getDepotCardId();

        for (int i = 0; i < depotId.length; i++) {
            if (depotId[i] != 0) {
                for (int j = 0; j < leaderCards.size(); j++) {
                    if (depotId[i] == leaderCards.get(j).getId()) {
                        leaderDepotCards.put(i + 1, j + 1);
                        break;
                    }
                }
            }
        }
    }

    //GETTERS

    //Composite getters

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
     * Returns the total number of cards of a specific color the player has
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
     * Returns the total number of cards of a specific color and level the player has
     *
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
     * Returns the number of resources still present in waiting room (white marbles are included)
     *
     * @return the number of resources left
     */
    public int getLeftInWaitingRoom() {
        List<ResourceType> leftovers = waitingRoom.getStoredResources();
        int sum = 0;
        for (ResourceType resource : leftovers) {
            sum += waitingRoom.getNumOfResource(resource);
        }
        sum += whiteMarbleNum;
        return sum;
    }

    //Simple getters

    /**
     * Returns whether or not the player has taken the first turn
     *
     * @return true if the player has chosen their leader cards and starting resources
     */
    public boolean isFirstTurnTaken() {
        return hasTakenFirstTurn;
    }

    /**
     * Returns whether or not the player is flagged as connected
     *
     * @return true if the player is flagged as connected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter
     *
     * @return whiteMarbleNum
     */
    public int getWhiteMarbles() {
        return whiteMarbleNum;
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
    public String getUsername() {
        return username;
    }

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
     * Getter for the player's waiting room
     *
     * @return the player's waiting room
     */
    public UnlimitedStorage getWaitingRoom() {
        return waitingRoom;
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
     * Getter
     *
     * @return the number of active leader cards among the player's
     */
    public int getActiveLeaderCards() {
        return Math.toIntExact(leaderCards.stream().filter(Card::isActive).count());
    }

    /**
     * Getter
     *
     * @return popeFavorTiles
     */
    public List<PopeFavorTile> getPopeFavorTiles() {
        return popeFavorTiles;
    }

    /**
     * Getter
     *
     * @return vpFaithTiles
     */
    public int[] getVpFaithTiles() {
        return vpFaithTiles.clone();
    }

    /**
     * Getter
     *
     * @return vpFaithValues
     */
    public int[] getVpFaithValues() {
        return vpFaithValues.clone();
    }

    /**
     * Getter
     *
     * @return leaderDepotCards
     */
    public Map<Integer, Integer> getLeaderDepotCards() {
        Map<Integer, Integer> result = new HashMap<>();
        for (Integer depotNumber : leaderDepotCards.keySet()) {
            result.put(depotNumber, leaderDepotCards.get(depotNumber));
        }
        return result;
    }

    /**
     * Returns the maximum amount of faith on the faith track, upon reaching which the game enters its last turn
     *
     * @return the final faith amount
     */
    public int getFinalFaith() {
        return finalFaith;
    }

    /**
     * Returns the maximum number of development cards a player can own, upon reaching which the game enters its last turn
     *
     * @return the maximum development card number
     */
    public int getDevCardMax() {
        return devCardMax;
    }

    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    /**
     * Adds an observer to the list of this object's observers
     *
     * @param observer the Observer to be added
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    /**
     * Returns the list of this object's observers
     *
     * @return a List of the Observers
     */
    public List<Observer> getObservers() {
        return observers;
    }
}
