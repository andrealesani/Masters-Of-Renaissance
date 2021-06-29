package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.LorenzoTokenType;
import network.ServerMessageType;
import server.GameController;

import java.util.Arrays;

/**
 * Class used to serialize a Lorenzo object, send it over the network and store its information in the client
 */
public class LorenzoBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Lorenzo's faith
     */
    private int faith;
    /**
     * Represents Lorenzo's activeDeck list size
     */
    private int activeTokenNumber;
    /**
     * Represents Lorenzo's usedDeck list
     */
    private LorenzoTokenType[] discardedTokens;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     */
    public LorenzoBean(GameController controller) {
        this.controller = controller;
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        Lorenzo lorenzo = (Lorenzo) observable;
        setFaithFromGame(lorenzo);
        setActiveTokenNumberFromGame(lorenzo);
        setDiscardedTokensFromGame(lorenzo);

        controller.broadcastMessage(ServerMessageType.LORENZO, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, ServerMessageType.LORENZO, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * This method is used to print only one line of Lorenzo so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if(line < 1 || line > 3)
            throw new ParametersNotValidException();

        //Row 1
        if(line == 1)
            return drawFaithTrack();

        //Row 2
        if (line == 2)
            return " Active Tokens: " + Color.RESOURCE_STD + activeTokenNumber + Color.RESET;

        //Row 3
        else
            return " Discarded Tokens: " + Arrays.toString(
                    Arrays.stream(discardedTokens).map(LorenzoTokenType::iconPrint).toArray());
    }

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {

        String result = Color.HEADER + "Lorenzo:\n" + Color.RESET;

        for (int i = 1; i <= 3; i++) {
            result += "\n" + printLine(i) + "\n";
        }

        return result;
    }

    //PRIVATE DRAWING METHODS

    /**
     * Returns a String representation of a faith track with lorenzo's faith marker
     *
     * @return the String representation of lorenzo's faith track
     */
    private String drawFaithTrack () {
        //TODO NON HARDCODARE
        int[] popeTriggerValues = {8, 16, 24};
        int[] popeSectionSizes = {4, 5, 6};

        String content = " ";
        int nextPopeTile = 0;

        for (int pos = 0; pos <= 24; pos++) {

            //The faith track tile
            if (faith == pos) {
                content += Color.PURPLE_FG + "+" + Color.RESET;
            } else {
                content += Color.GREY_LIGHT_FG + "■" + Color.RESET;
            }

            //The modifiers
            if (pos == popeTriggerValues[nextPopeTile]) {
                content += Color.ORANGE_LIGHT_FG + "±" + Color.RESET;
            }

            //The space between tiles
            if (pos == popeTriggerValues[nextPopeTile] - popeSectionSizes[nextPopeTile]) {

                content += Color.GREY_LIGHT_FG + "─" + Color.RESET;
                content += Color.ORANGE_LIGHT_FG + "[" + Color.RESET;

            } else if (pos == popeTriggerValues[nextPopeTile]) {

                content += Color.ORANGE_LIGHT_FG + "]" + Color.RESET;
                if (pos != 24) {
                    content += Color.GREY_LIGHT_FG + "─" + Color.RESET;
                }
                nextPopeTile++;

            } else {

                content += Color.GREY_LIGHT_FG + "─" + Color.RESET;

            }
        }

        return content;
    }

    // GETTERS

    /**
     * Getter for lorenzo's faith
     *
     * @return an int representing the AI's faith score
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Getter for lorenzo's number of unused tokens
     *
     * @return an int representing the AI's remaining unused tokens
     */
    public int getActiveTokenNumber() {
        return activeTokenNumber;
    }

    /**
     * Getter for lorenzo's used tokens
     *
     * @return an array of the AI's discarded tokens, the first being the last that was used
     */
    public LorenzoTokenType[] getDiscardedTokens() {
        return discardedTokens.clone();
    }

    // SETTERS

    /**
     * Sets lorenzo's faith score
     *
     * @param lorenzo the object to take the information from
     */
    private void setFaithFromGame(Lorenzo lorenzo) {
        faith = lorenzo.getFaith();
    }

    /**
     * Sets lorenzo's number of unused tokens
     *
     * @param lorenzo the object to take the information from
     */
    private void setActiveTokenNumberFromGame(Lorenzo lorenzo) {
        activeTokenNumber = lorenzo.getActiveDeck().size();
    }

    /**
     * Sets lorenzo's array of used tokens
     *
     * @param lorenzo the object to take the information from
     */
    private void setDiscardedTokensFromGame(Lorenzo lorenzo) {
        int i = 0;
        discardedTokens = new LorenzoTokenType[lorenzo.getUsedDeck().size()];
        for (ActionToken token : lorenzo.getUsedDeck()) {
            discardedTokens[i++] = token.getType();
        }
    }

}
