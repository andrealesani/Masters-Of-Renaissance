package network.beans;

import com.google.gson.Gson;
import model.*;
import model.card.leadercard.LeaderCard;
import network.GameController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static model.ResourceType.*;


/**
 * The purpose of this class is to simplify the information contained in the PlayerBoard in order to
 * transcribe it into a json file that will be passed to the Server object for the communication with the client
 */
public class PlayerBoardBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
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
     * Represents the IDs of the productions available to the player
     */
    private int[] productions;
    /**
     * Represents the player's number of whiteMarbles
     */
    private int whiteMarbles;
    /**
     * Represents the player's faith
     */
    private int faith;
    /**
     * Represents all the marble conversions available to the player
     */
    private ResourceType[] marbleConversions;
    /**
     * Represents the type of Resources that the player has a discount on
     */
    private ResourceType[] discountType = {COIN, SERVANT, SHIELD, STONE};
    /**
     * Represents the discount applied to the Resource in the same position of the discountType array
     */
    private int[] discountQuantity;
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

    // CONSTRUCTOR

    /**
     * Server constructor
     *
     * @param controller to which the bean notifies changes
     */
    public PlayerBoardBean(GameController controller) {
        this.controller = controller;
    }

    /**
     * Client constructor, it doesn't need reference to the controller
     */
    public PlayerBoardBean() {
        this.controller = null;
    }

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

    /**
     * Getter
     *
     * @return productions IDs
     */
    public int[] getProductions() {
        return productions;
    }

    /**
     * Getter
     *
     * @return marbleConversions
     */
    public ResourceType[] getMarbleConversions() {
        return marbleConversions;
    }

    /**
     * Getter
     *
     * @return discountType
     */
    public ResourceType[] getDiscountType() {
        return discountType;
    }

    /**
     * Getter
     *
     * @return discountQuantity
     */
    public int[] getDiscountQuantity() {
        return discountQuantity;
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

    private void setProductionsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        productions = new int[playerBoard.getProductionHandler().getProductions().size()];
        for (Production production : playerBoard.getProductionHandler().getProductions()) {
            productions[i++] = production.getId();
        }
    }

    private void setMarbleConversionsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        marbleConversions = new ResourceType[playerBoard.getMarbleConversions().size()];
        for (ResourceType resourceType : playerBoard.getMarbleConversions()) {
            marbleConversions[i++] = resourceType;
        }
    }

    private void setDiscountFromPB(PlayerBoard playerBoard) {
        int i = 0;
        discountType = new ResourceType[playerBoard.getDiscounts().size()];
        discountQuantity = new int[discountType.length];
        for (Map.Entry<ResourceType, Integer> entry : playerBoard.getDiscounts().entrySet()) {
            discountType[i] = entry.getKey();
            discountQuantity[i++] = entry.getValue();
        }
    }

    // OBSERVER METHODS
    private transient boolean isFirstUpdate = true;

    public void update(Object observable) {
        Gson gson = new Gson();
        PlayerBoard pb = (PlayerBoard) observable;

        if (isFirstUpdate) {
            setUsernameFromPB(pb);
            setVpFaithTilesFromPB(pb);
            setVpFaithValuesFromPB(pb);
            setPopeTilePointsFromPB(pb);

            isFirstUpdate = false;
        }

        setFaithFromPB(pb);
        setWhiteMarblesFromPB(pb);
        setCardSlotsFromPB(pb);
        setLeaderCardsFromPB(pb);
        setActiveLeaderCardsFromPB(pb);
        setPopeTileStatesFromPB(pb);
        setProductionsFromPB(pb);
        setMarbleConversionsFromPB(pb);
        setDiscountFromPB(pb);

        controller.broadcastMessage(MessageType.PLAYERBOARD, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.PLAYERBOARD, gson.toJson(this));
    }

    public String printLine(int line) {
        line--;
        String content = "";

        switch (line) {
            case 0 -> {
                content += " PopeTiles: ";
                for (int i = 0; i < popeTileStates.length; i++) {
                    content += " " + popeTileStates[i] + ": " + popeTilePoints[i] + " VPs ";
                }
            }
            case 1 -> {
                if (productions.length == 1)
                    return " Player has only default production";
                return " Productions: " + Arrays.toString(productions);
            }
            case 2 -> {
                return " WhiteMarbles: " + whiteMarbles;
            }
            case 3 -> {
                return " Faith: " + faith;
            }
            case 4 -> {
                if (marbleConversions.length == 0)
                    return " Player doesn't have any marble conversions";
                return " MarbleConversions: " + Arrays.toString(marbleConversions);
            }
            case 5 -> {
                if (discountType.length == 0)
                    content += " Player doesn't have any discounts";
                else
                    for (int i = 0; i < discountType.length; i++) {
                        content += " " + discountType[i] + ": " + discountQuantity[i] + "  ";
                    }
            }
            case 6 -> {
                content += " LeaderCards: ";
                for (int i = 0; i < leaderCards.length; i++) {
                    content += "[" + leaderCards[i] + ": " + activeLeaderCards[i] + "] ";
                }
            }
            case 7 -> {
                content += " VpFaithTiles: " + Arrays.toString(vpFaithTiles);
            }
            default -> {
                content += " VpFaithTiles: " + Arrays.toString(vpFaithTiles) + "\n" +
                        " VpFaithValues: " + Arrays.toString(vpFaithValues) + "\n";
                content += " CardSlots:\n";
                for (SlotBean slotBean : cardSlots) {
                    content += "  ";
                    content += slotBean.toString();
                }
            }
        }
        return content;
    }

    @Override
    public String toString() {
        String content = "";
        content += "\n" + Color.HEADER + username + "'s PlayerBoard:\n" + Color.RESET;
        content += " PopeTiles: ";
        for (int i = 0; i < popeTileStates.length; i++) {
            content += " " + popeTileStates[i] + ": " + popeTilePoints[i] + "  ";
        }
        content += "\n Productions: " + Arrays.toString(productions) + "\n" +
                " WhiteMarbles: " + whiteMarbles + "\n" +
                " Faith: " + faith + "\n" +
                " MarbleConversions: " + Arrays.toString(marbleConversions) + "\n";
        if (discountType.length == 0)
            content += " Player has not activated any discounts";
        else
            for (int i = 0; i < discountType.length; i++) {
                content += " " + discountType[i] + ": " + discountQuantity[i] + "  ";
            }
        content += "\n LeaderCards: ";
        for (int i = 0; i < leaderCards.length; i++) {
            content += "[" + leaderCards[i] + ": " + activeLeaderCards[i] + "] ";
        }
        content += "\n VpFaithTiles: " + Arrays.toString(vpFaithTiles) + "\n" +
                " VpFaithValues: " + Arrays.toString(vpFaithValues) + "\n";
        content += " CardSlots:\n";
        for (SlotBean slotBean : cardSlots) {
            content += "  ";
            content += slotBean.toString();
        }

        return content;
    }
}
