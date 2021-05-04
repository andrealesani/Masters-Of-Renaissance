package network.beans;

import com.google.gson.Gson;
import model.*;
import model.card.leadercard.LeaderCard;

import java.util.List;

import static model.ResourceType.*;


/**
 * The purpose of this class is to simplify the information contained in the PlayerBoard in order to
 * transcribe it into a json file that will be passed to the Server object for the communication with the client
 */
public class PlayerBoardBean implements Observer {
    /**
     * Represents the player's username
     */
    private String username;
    /**
     * Represents the status (active/inactive/discarded) of each Pope's favor tile
     */
    private PopeTileState[] popeTileStates;
    /**
     * Represents the victory points assigned to each Pope's favor tile when it is activated
     */
    private int[] popeTilePoints;
    /**
     * Represents the different types of resources in the strongbox
     */
    private ResourceType[] strongboxType;
    /**
     * Represents the number present in the strongbox for each type of resources
     */
    private int[] strongboxQuantities;
    /**
     * Represents the different types of resources in the waitingRoom
     */
    private ResourceType[] waitingRoomType;
    /**
     * Represents the number present in the waitingRoom for each type of resources
     */
    private int[] waitingRoomQuantities;
    /**
     * Represents the player's number of whiteMarbles
     */
    private int whiteMarbles;
    /**
     * Represents the player's faith
     */
    private int faith;
    /**
     * Represents the set of 3 slots in which the development cards are inserted.
     * The card in the first position corresponds to the one at the top of the pile
     */
    private SlotBean[] cardSlots;
    /**
     * Represents the different types of LeaderCards for each player
     */
    private int[] leaderCards;
    /**
     * Represents which player's LeaderCards are active or not
     */
    private boolean[] activeLeaderCards;
    /**
     * Represents the faith value reached by the player to earns victory points
     */
    private int[] vpFaithTiles;
    /**
     * Represents the victory points assigned to each square
     */
    private int[] vpFaithValues;

    // TODO add productions, marble conversions, discounts
    // TODO remove strongbox and waiting room attributes


    // GETTERS

    /**
     * Getter
     *
     * @return player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter
     *
     * @return player's faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter
     *
     * @return player's WhiteMarbles
     */
    public int getWhiteMarbles() {
        return whiteMarbles;
    }

    /**
     * Getter for resource's type in WaitingRoom
     *
     * @return waitingRoomType
     */
    public ResourceType[] getWaitingRoomType() {
        return waitingRoomType;
    }

    /**
     * Getter for resource's quantity in WaitingRoom
     *
     * @return waitingRoomQuantities
     */
    public int[] getWaitingRoomQuantities() {
        return waitingRoomQuantities;
    }

    /**
     * Getter for resource's type in the strongbox
     *
     * @return strongboxType
     */
    public ResourceType[] getStrongboxType() {
        return strongboxType;
    }

    /**
     * Getter for resource's quantity in the strongbox
     *
     * @return strongboxQuantities
     */
    public int[] getStrongboxQuantities() {
        return strongboxQuantities;
    }

    /**
     * Getter for player's set of cards
     *
     * @return cardSlots
     */
    public SlotBean[] getCardSlots() {
        return cardSlots;
    }

    /**
     * Getter
     *
     * @return player's leaderCards
     */
    public int[] getLeaderCards() {
        return leaderCards;
    }

    /**
     * Getter LeaderCard's activity
     *
     * @return activeLeaderCards
     */
    public boolean[] getActiveLeaderCards() {
        return activeLeaderCards;
    }

    /**
     * Getter
     *
     * @return VpFaithTiles
     */
    public int[] getVpFaithTiles() {
        return vpFaithTiles;
    }

    /**
     * Getter
     *
     * @return vpFaithValues
     */
    public int[] getVpFaithValues() {
        return vpFaithValues;
    }

    /**
     * Getter
     *
     * @return popeTileStates
     */
    public PopeTileState[] getPopeTileStates() {
        return popeTileStates;
    }

    /**
     * Getter
     *
     * @return popeTilePoints
     */
    public int[] getPopeTilePoints() {
        return popeTilePoints;
    }

    // SETTERS

    /**
     * Sets the value for the bean's username
     *
     * @param playerBoard from which information is retrieved
     */
    private void setUsernameFromPB(PlayerBoard playerBoard) {
        username = playerBoard.getUsername();
    }

    /**
     * Sets the value for the bean's faith
     *
     * @param playerBoard from which information is retrieved
     */
    private void setFaithFromPB(PlayerBoard playerBoard) {
        faith = playerBoard.getFaith();
    }

    /**
     * Sets the value for the bean's whiteMarbles
     *
     * @param playerBoard from which information is retrieved
     */
    private void setWhiteMarblesFromPB(PlayerBoard playerBoard) {
        whiteMarbles = playerBoard.getWhiteMarbles();
    }

    /**
     * Sets the value for the bean's WaitingRoomType
     *
     * @param playerBoard from which information is retrieved
     */
    private void setWaitingRoomTypeFromPB(PlayerBoard playerBoard) {
        int i = 0;
        waitingRoomType = new ResourceType[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            waitingRoomType[i++] = resourceType;
        }

    }

    /**
     * Sets the value for the bean's WaitingRoomQuantities
     *
     * @param playerBoard from which information is retrieved
     */
    private void setWaitingRoomQuantitiesFromPB(PlayerBoard playerBoard) {
        int i = 0;
        waitingRoomQuantities = new int[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            waitingRoomQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(resourceType);
        }
    }

    /**
     * Sets the value for the bean's StrongboxType
     *
     * @param playerBoard from which information is retrieved
     */
    private void setStrongboxTypeFromPB(PlayerBoard playerBoard) {
        int i = 0;
        strongboxType = new ResourceType[(int) playerBoard.getStrongbox().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getStrongbox().getStoredResources()) {
            strongboxType[i++] = resourceType;
        }
    }

    /**
     * Sets the value for the bean's StrongboxQuantities
     *
     * @param playerBoard from which information is retrieved
     */
    private void setStrongboxQuantitiesFromPB(PlayerBoard playerBoard) {
        int i = 0;
        strongboxQuantities = new int[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            strongboxQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(resourceType);
        }
    }

    /**
     * Sets the value for the bean's CardSlots
     *
     * @param playerBoard from which information is retrieved
     */
    private void setCardSlotsFromPB(PlayerBoard playerBoard) {
        int i;
        cardSlots = new SlotBean[playerBoard.getCardSlots().size()];
        for (i = 0; i < 3; i++) {
            cardSlots[i] = new SlotBean();
            cardSlots[i].setDevelopmentCardsFromPB(playerBoard, i + 1);
        }
    }

    /**
     * Sets the value for the bean's LeaderCards
     *
     * @param playerBoard from which information is retrieved
     */
    private void setLeaderCardsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        leaderCards = new int[playerBoard.getLeaderCards().size()];
        for (LeaderCard leaderCard : playerBoard.getLeaderCards()) {
            leaderCards[i++] = leaderCard.getId();
        }
    }

    /**
     * Sets the value for the bean's ActiveLeaderCards
     *
     * @param playerBoard from which information is retrieved
     */
    private void setActiveLeaderCardsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        activeLeaderCards = new boolean[playerBoard.getLeaderCards().size()];
        for (LeaderCard leaderCard : playerBoard.getLeaderCards()) {
            if (leaderCard.isActive())
                activeLeaderCards[i++] = true;
            else
                activeLeaderCards[i++] = false;
        }
    }

    /**
     * Sets the value for the bean's VpFaithTiles
     *
     * @param playerBoard from which information is retrieved
     */
    private void setVpFaithTilesFromPB(PlayerBoard playerBoard) {
        int i;
        int[] current = playerBoard.getVpFaithTiles();
        vpFaithTiles = new int[current.length];
        for (i = 0; i < vpFaithTiles.length; i++)
            vpFaithTiles[i] = current[i];

    }

    /**
     * Sets the value for the bean's VpFaithValues
     *
     * @param playerBoard from which information is retrieved
     */
    private void setVpFaithValuesFromPB(PlayerBoard playerBoard) {
        int i;
        int[] current = playerBoard.getVpFaithValues();
        vpFaithValues = new int[current.length];
        for (i = 0; i < vpFaithValues.length; i++)
            vpFaithValues[i] = current[i];
    }

    /**
     * Sets the value for the bean's PopeTilesStates
     *
     * @param playerBoard from which information is retrieved
     */
    private void setPopeTileStatesFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTileStates = new PopeTileState[current.size()];
        for (i = 0; i < popeTileStates.length; i++)
            popeTileStates[i] = current.get(i).getState();
    }

    /**
     * Sets the value for the bean's PopeTilesPoints
     *
     * @param playerBoard from which information is retrieved
     */
    private void setPopeTilePointsFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTilePoints = new int[current.size()];
        for (i = 0; i < popeTilePoints.length; i++)
            popeTilePoints[i] = current.get(i).getVictoryPoints();
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        PlayerBoard pb = (PlayerBoard) observable;

        setUsernameFromPB(pb);
        setFaithFromPB(pb);
        setWhiteMarblesFromPB(pb);
        setWaitingRoomTypeFromPB(pb);
        setWaitingRoomQuantitiesFromPB(pb);
        setStrongboxTypeFromPB(pb);
        setStrongboxQuantitiesFromPB(pb);
        setCardSlotsFromPB(pb);
        setLeaderCardsFromPB(pb);
        setActiveLeaderCardsFromPB(pb);
        setVpFaithTilesFromPB(pb);
        setVpFaithValuesFromPB(pb);
        setPopeTileStatesFromPB(pb);
        setPopeTilePointsFromPB(pb);

        BeanWrapper beanWrapper = new BeanWrapper(BeanType.PLAYERBOARD, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
