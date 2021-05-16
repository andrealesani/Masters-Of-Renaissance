package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Observer;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import network.GameController;

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
            return " Faith: " + faith;
        if (line == 1)
            return " ActiveTokens: " + Arrays.toString(activeTokens);
        else
            return " DiscardedTokens: " + Arrays.toString(discardedTokens);
    }

    @Override
    public String toString() {
        return Color.HEADER + "Lorenzo:\n" + Color.RESET +
                " faith: " + faith +
                "\n activeTokens: " + Arrays.toString(activeTokens) +
                "\n discardedTokens: " + Arrays.toString(discardedTokens) + "\n";
    }
}
