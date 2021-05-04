package network.beans;

import com.google.gson.Gson;
import model.Observer;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;

public class LorenzoBean implements Observer {
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

        BeanWrapper beanWrapper = new BeanWrapper(BeanType.LORENZO, gson.toJson(this));

        // TODO ask to the Controller to be sent to the clients
    }
}
