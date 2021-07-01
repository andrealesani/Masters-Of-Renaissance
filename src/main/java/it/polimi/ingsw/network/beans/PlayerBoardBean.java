package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.model.card.leadercard.LeaderCard;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.GameController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.resource.ResourceType.*;


/**
 * Class used to serialize a PlayerBoard object, send it over the network and store its information in the client
 */
public class PlayerBoardBean implements Observer, PlayerBean {
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
     * Represent the faith score that, if reached by one of the players, triggers the corresponding pope's favor tile
     */
    private int[] popeTriggerValues;
    /**
     * Represent the size of the faith track area before (and including) the trigger value where players can still benefit from the tile's activation
     */
    private int[] popeSectionSizes;
    /**
     * Represents the player's number of resources left to convert
     */
    private int resourcesToConvert;
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
     * Represents the association between a warehouse leader depot's number and its LeaderCard's id
     */
    private Map<Integer, Integer> leaderDepotCardsId;
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
            setPopeSectionSizesFromPB(pb);
            setPopeTriggerValuesFromPB(pb);

            isFirstUpdate = false;
        }

        setFaithFromPB(pb);
        setResourcesToConvertFromPB(pb);
        setCardSlotsFromPB(pb);
        setLeaderCardsFromPB(pb);
        setActiveLeaderCardsFromPB(pb);
        setPopeTileStatesFromPB(pb);
        setMarbleConversionsFromPB(pb);
        setDiscountFromPB(pb);
        setLeaderDepotCardsFromPB(pb);

        controller.broadcastMessage(MessageType.PLAYERBOARD_BEAN, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.PLAYERBOARD_BEAN, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * This method is used to print only one line of the PlayerBoard so that multiple objects can be printed
     * in parallel in the CLI. It does not display leader cards
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {
        return printLine(line, null);
    }

    /**
     * This method is used to print only one line of the PlayerBoard so that multiple objects can be printed
     * in parallel in the CLI. It requires the client's player's username in order to know which leader cards to print
     *
     * @param line the line to print (starts from 1)
     * @param clientUsername the client's player's username
     * @return the String with the line to print
     */
    public String printLine(int line, String clientUsername) {

        if (line < 1 || line > 10)
            throw new ParametersNotValidException();

        String content = "";

        switch (line) {

            //Row 1
            case 1 -> content += drawFaithTrack();

            //Row 2
            case 2 -> content += drawPopeTiles();

            //Row 3
            case 3 -> content += drawWhiteMarbles();

            //Row 4
            case 4 -> {
                if (marbleConversions.length == 0)
                    content += " Player does not have any marble conversions";
                else {
                    content += " White Marble Conversions: ";
                    for (ResourceType marbleConversion : marbleConversions) {
                        content += marbleConversion.iconPrint() + "  ";
                    }
                }
            }

            //Row 5
            case 5 -> {
                if (discountType.length == 0)
                    content += " Player does not have any discounts";
                else {
                    content += " Card discounts: ";
                    for (int i = 0; i < discountType.length; i++) {
                        content += " -" + discountQuantity[i] + " x " + discountType[i].iconPrint() + "  ";
                    }
                }
            }

            //Row 6
            case 6 -> content += " CardSlots:";

            //Row 7
            case 7 -> content += drawCardSlot(1);

            //Row 8
            case 8 -> content += drawCardSlot(2);

            //Row 9
            case 9 -> content += drawCardSlot(3);

            //Row 10
            case 10 -> content += drawLeaderCards(clientUsername);

            default -> content += Color.RED_LIGHT_FG + "This row should not be printed!\n" + Color.RESET;

        }
        return content;
    }

    /**
     * Prints a String representation of the bean's data. It does not display leader cards
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return toString(null);
    }

    /**
     * Prints a String representation of the bean's data.
     * It requires the client's player's username in order to know which leader cards to print
     *
     * @param clientUsername the client's player's username
     * @return the String representation
     */
    public String toString(String clientUsername) {

        String result = Color.HEADER + username + "'s PlayerBoard:\n" + Color.RESET;

        for (int i = 1; i <= 10; i++) {
            result += "\n" + printLine(i) + "\n";
        }

        return result;
    }

    // PRIVATE DRAWING METHODS

    /**
     * Returns the String representation of the faith track and the player's faith marker
     *
     * @return the String representation of the faith track
     */
    private String drawFaithTrack () {
        String content = " ";
        int nextPopeTile = 0;
        int nextFaithTile = 0;
        for (int pos = 0; pos <= vpFaithTiles[vpFaithTiles.length - 1]; pos++) {

            //The faith track tile
            if (pos == 24 && faith > 24) {
                content += Color.RED_LIGHT_FG + "+" + Color.RESET;
            } else {
                if (faith == pos) {
                    content += Color.RED_LIGHT_FG + "+" + Color.RESET;
                } else {
                    content += Color.GREY_LIGHT_FG + "■" + Color.RESET;
                }
            }

            //The modifiers
            if (pos == popeTriggerValues[nextPopeTile]) {
                content += Color.ORANGE_LIGHT_FG + "±" + Color.RESET;
            }
            if (pos == vpFaithTiles[nextFaithTile]) {
                content += Color.YELLOW_LIGHT_FG + "" + vpFaithValues[nextFaithTile] + "" + Color.RESET;
                nextFaithTile++;
            }

            //The space between tiles
            if (pos == popeTriggerValues[nextPopeTile] - popeSectionSizes[nextPopeTile]) {

                content += Color.GREY_LIGHT_FG + "─" + Color.RESET;
                content += Color.ORANGE_LIGHT_FG + "[" + Color.RESET;

            } else if (pos == popeTriggerValues[nextPopeTile]) {

                content += Color.ORANGE_LIGHT_FG + "]" + Color.RESET;
                if (pos != vpFaithTiles[vpFaithTiles.length - 1]) {
                    content += Color.GREY_LIGHT_FG + "─" + Color.RESET;
                }
                nextPopeTile++;

            } else {

                content += Color.GREY_LIGHT_FG + "─" + Color.RESET;

            }
        }

        return content;
    }

    /**
     * Returns the String representation of the card slot corresponding to the given number
     *
     * @param number the number of the card slot to draw
     * @return the String representation of the given card slot
     */
    private String drawCardSlot (int number) {
        return  "  " +
                "Slot " + (number) + " : " +
                cardSlots[number - 1].toString();
    }

    /**
     * Returns the String representation of the player's leader cards, according to what the client's player should be able to see
     *
     * @param clientUsername the client's player's username
     * @return the String representation of the player's leader cards, according to visibility rules
     */
    private String drawLeaderCards (String clientUsername) {

        if (clientUsername == null) {
            return "";
        }

        String content = " LeaderCards: ";
        for (int i = 0; i < leaderCards.length; i++) {
            if (activeLeaderCards[i] || clientUsername.equals(username)) {
                if (activeLeaderCards[i]) {
                    content += Color.RESOURCE_STD;
                } else {
                    content += Color.RESET;
                }
                content += "[" + leaderCards[i] + "] ";
                content += Color.RESET;
            } else {
                content += "[X] ";
            }
        }
        return content;
    }

    /**
     * Returns the String representation of the white marbles to convert / bonus resources to choose
     *
     * @return the String representation of the resources to convert
     */
    private String drawWhiteMarbles () {
        String content = " Resources left to convert: ";

        if (resourcesToConvert > 0)
            content += Color.RESOURCE_STD;

        content += resourcesToConvert;
        content += Color.RESET;

        return content;
    }

    /**
     * Returns the String representation of the pope's favor tiles
     *
     * @return the String representation of the pope's favor tiles
     */
    private String drawPopeTiles () {
        String content = " Pope Tiles: ";
        for (int i = 0; i < popeTileStates.length; i++) {
            content += " " + (i+1) + ".[";
            content +=  popeTileStates[i];
            content +=  " - " + Color.YELLOW_LIGHT_FG + "" + popeTilePoints[i] + "VPs" + Color.RESET + "] ";
        }
        return content;
    }

    // GETTERS

    /**
     * Getter for the player board's player's username
     *
     * @return player's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the player's faith score
     *
     * @return player's faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter for the player's white marbles to convert / bonus resources to choose
     *
     * @return player's WhiteMarbles
     */
    public int getResourcesToConvert() {
        return resourcesToConvert;
    }

    /**
     * Getter for player's set of cards
     *
     * @return cardSlots
     */
    public SlotBean[] getCardSlots() {
        return cardSlots.clone();
    }

    /**
     * Getter for the player's leader cards
     *
     * @return player's leaderCards
     */
    public int[] getLeaderCards() {
        return leaderCards.clone();
    }

    /**
     * Getter for which LeaderCards are active
     *
     * @return activeLeaderCards
     */
    public boolean[] getActiveLeaderCards() {
        return activeLeaderCards.clone();
    }

    //TODO prenderli da json?
    /**
     * Getter for which faith track tiles have a victory points reward
     *
     * @return VpFaithTiles
     */
    public int[] getVpFaithTiles() {
        return vpFaithTiles.clone();
    }

    /**
     * Getter for the faith track tiles victory points reward
     *
     * @return vpFaithValues
     */
    public int[] getVpFaithValues() {
        return vpFaithValues.clone();
    }

    /**
     * Getter for the state of the pope's favor tiles
     *
     * @return popeTileStates
     */
    public PopeTileState[] getPopeTileStates() {
        return popeTileStates.clone();
    }

    /**
     * Getter for the victory points of the pope's favor tiles
     *
     * @return popeTilePoints
     */
    public int[] getPopeTilePoints() {
        return popeTilePoints.clone();
    }

    /**
     * Getter for the faith track score that triggers the pope's favor tiles
     *
     * @return popeTriggerValues
     */
    public int[] getPopeTriggerValues() {
        return popeTriggerValues.clone();
    }

    /**
     * Getter for the number of faith track tiles before and including the trigger tile that allow to activate a pope's favor tile
     *
     * @return popeSectionSizes
     */
    public int[] getPopeSectionSizes() {
        return popeSectionSizes.clone();
    }

    /**
     * Getter for the available conversions for white marbles
     *
     * @return marbleConversions
     */
    public ResourceType[] getMarbleConversions() {
        return marbleConversions.clone();
    }

    /**
     * Getter for the available discounts' resources for development cards
     *
     * @return discountType
     */
    public ResourceType[] getDiscountType() {
        return discountType.clone();
    }

    /**
     * Getter for the available discounts' amount for development cards
     *
     * @return discountQuantity
     */
    public int[] getDiscountQuantity() {
        return discountQuantity.clone();
    }

    /**
     * Getter for the correspondence between leader depots and leader cards
     *
     * @return a Map where the keys are depot numbers and the values are leader card ids
     */
    public Map<Integer, Integer> getLeaderDepotCardsId() {
        Map<Integer, Integer> result = new HashMap<>();

        for (Integer depot : leaderDepotCardsId.keySet())
            result.put(depot, leaderDepotCardsId.get(depot));

        return result;
    }

    // SETTERS

    /**
     * Sets the value for the bean's username
     *
     * @param playerBoard the object to take the information from
     */
    private void setUsernameFromPB(PlayerBoard playerBoard) {
        username = playerBoard.getUsername();
    }

    /**
     * Sets the value for the bean's faith
     *
     * @param playerBoard the object to take the information from
     */
    private void setFaithFromPB(PlayerBoard playerBoard) {
        faith = playerBoard.getFaith();
    }

    /**
     * Sets the value for the bean's resourcesToConvert
     *
     * @param playerBoard the object to take the information from
     */
    private void setResourcesToConvertFromPB(PlayerBoard playerBoard) {
        resourcesToConvert = playerBoard.getResourcesToConvert();
    }

    /**
     * Sets the value for the bean's CardSlots
     *
     * @param playerBoard the object to take the information from
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
     * @param playerBoard the object to take the information from
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
     * @param playerBoard the object to take the information from
     */
    private void setActiveLeaderCardsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        activeLeaderCards = new boolean[playerBoard.getLeaderCards().size()];
        for (LeaderCard leaderCard : playerBoard.getLeaderCards()) {
            activeLeaderCards[i++] = leaderCard.isActive();
        }
    }

    /**
     * Sets the value for the bean's VpFaithTiles
     *
     * @param playerBoard the object to take the information from
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
     * @param playerBoard the object to take the information from
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
     * @param playerBoard the object to take the information from
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
     * @param playerBoard the object to take the information from
     */
    private void setPopeTilePointsFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTilePoints = new int[current.size()];
        for (i = 0; i < popeTilePoints.length; i++)
            popeTilePoints[i] = current.get(i).getVictoryPoints();
    }

    /**
     * Sets the value for the bean's PopeTriggerValues
     *
     * @param playerBoard the object to take the information from
     */
    private void setPopeTriggerValuesFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTriggerValues = new int[current.size()];
        for (i = 0; i < popeTriggerValues.length; i++)
            popeTriggerValues[i] = current.get(i).getTriggerValue();
    }

    /**
     * Sets the value for the bean's PopeSectionSize
     *
     * @param playerBoard the object to take the information from
     */
    private void setPopeSectionSizesFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeSectionSizes = new int[current.size()];
        for (i = 0; i < popeSectionSizes.length; i++)
            popeSectionSizes[i] = current.get(i).getActiveSectionSize();
    }

    /**
     * Sets the value for the bean's MarbleConversions
     *
     * @param playerBoard the object to take the information from
     */
    private void setMarbleConversionsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        marbleConversions = new ResourceType[playerBoard.getMarbleConversions().size()];
        for (ResourceType resourceType : playerBoard.getMarbleConversions()) {
            marbleConversions[i++] = resourceType;
        }
    }

    /**
     * Sets the value for the bean's Discounts
     *
     * @param playerBoard the object to take the information from
     */
    private void setDiscountFromPB(PlayerBoard playerBoard) {
        int i = 0;
        discountType = new ResourceType[playerBoard.getDiscounts().size()];
        discountQuantity = new int[discountType.length];
        for (Map.Entry<ResourceType, Integer> entry : playerBoard.getDiscounts().entrySet()) {
            discountType[i] = entry.getKey();
            discountQuantity[i++] = entry.getValue();
        }
    }

    /**
     * Sets the value for the bean's LeaderDepotCards
     *
     * @param playerBoard the object to take the information from
     */
    private void setLeaderDepotCardsFromPB(PlayerBoard playerBoard) {
        leaderDepotCardsId = playerBoard.getLeaderDepotCardsId();
    }
}
