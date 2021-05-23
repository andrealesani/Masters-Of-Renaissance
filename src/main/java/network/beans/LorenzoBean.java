package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.LorenzoTokenType;
import server.GameController;

import java.util.Arrays;

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
     * Represents Lorenzo's activeDeck list
     */
    private LorenzoTokenType[] activeTokens;
    /**
     * Represents Lorenzo's usedDeck list
     */
    private LorenzoTokenType[] discardedTokens;

    public LorenzoBean(GameController controller) {
        this.controller = controller;
    }

    // GETTERS

    public int getFaith() {
        return faith;
    }

    public LorenzoTokenType[] getActiveTokens() {
        return activeTokens;
    }

    public LorenzoTokenType[] getDiscardedTokens() {
        return discardedTokens;
    }

    // SETTERS

    private void setFaithFromGame(Lorenzo lorenzo) {
        faith = lorenzo.getFaith();
    }

    private void setActiveTokensFromGame(Lorenzo lorenzo) {
        int i = 0;
        activeTokens = new LorenzoTokenType[lorenzo.getActiveDeck().size()];
        for (ActionToken token : lorenzo.getActiveDeck()) {
            activeTokens[i++] = token.getType();
        }
    }

    private void setDiscardedTokensFromGame(Lorenzo lorenzo) {
        int i = 0;
        discardedTokens = new LorenzoTokenType[lorenzo.getUsedDeck().size()];
        for (ActionToken token : lorenzo.getUsedDeck()) {
            discardedTokens[i++] = token.getType();
        }
    }

    private String drawFaithTrack () {
        //TODO NON HARDCODARE
        int[] vpFaithTiles = {3, 6, 9, 12, 15, 18, 21, 24};
        int[] vpFaithValues = {1, 2, 4, 6, 9, 12, 16, 20};
        int[] popeTriggerValues = {8, 16, 24};
        int[] popeSectionSizes = {4, 5, 6};

        String content = " ";
        int nextPopeTile = 0;
        int nextFaithTile = 0;
        for (int pos = 0; pos <= vpFaithTiles[vpFaithTiles.length - 1]; pos++) {

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


    // OBSERVER METHODS

    public void update(Object observable) {
        Gson gson = new Gson();
        Lorenzo lorenzo = (Lorenzo) observable;
        setFaithFromGame(lorenzo);
        setActiveTokensFromGame(lorenzo);
        setDiscardedTokensFromGame(lorenzo);

        controller.broadcastMessage(MessageType.LORENZO, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.LORENZO, gson.toJson(this));
    }

    public String printLine(int line) {
        line--;
        if(line < 0 || line > 2)
            throw new ParametersNotValidException();

        if(line == 0)
            return drawFaithTrack();
        if (line == 1)
            return " Active Tokens: " + Color.RESOURCE_STD + activeTokens.length + Color.RESET;
        if (line == 2)
            return " Discarded Tokens: " + Arrays.toString(
                    Arrays.stream(discardedTokens).map(LorenzoTokenType::iconPrint).toArray());
        else
            return "";
    }

    @Override
    public String toString() {
        return Color.HEADER + "Lorenzo:\n" + Color.RESET +
                drawFaithTrack() + "\n" +
                " Active Tokens: " + Color.RESOURCE_STD + activeTokens.length + Color.RESET + "\n" +
                " discardedTokens: " +
                Arrays.toString(
                        Arrays.stream(discardedTokens).map(LorenzoTokenType::iconPrint).toArray()) +
                "\n";
    }
}
