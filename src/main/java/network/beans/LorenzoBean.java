package network.beans;

import model.CardTable;
import model.Game;
import model.Observable;
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
    private TokenType[] activeTokens;
    /**
     * Represents Lorenzo's usedDeck list
     */
    private TokenType[] discardedTokens;

    // GETTERS

    public int getFaith() {
        return faith;
    }

    public TokenType[] getActiveTokens() {
        return activeTokens;
    }

    public TokenType[] getDiscardedTokens() {
        return discardedTokens;
    }

    // SETTERS

    private void setFaithFromGame(Lorenzo lorenzo) {
        faith = lorenzo.getFaith();
    }

    private void setActiveTokensFromGame(Lorenzo lorenzo) {
        int i = 0;
        activeTokens = new TokenType[lorenzo.getActiveDeck().size()];
        for (ActionToken token : lorenzo.getActiveDeck()) {
            activeTokens[i++] = token.getType();
        }
    }

    private void setDiscardedTokensFromGame(Lorenzo lorenzo) {
        int i = 0;
        discardedTokens = new TokenType[lorenzo.getUsedDeck().size()];
        for (ActionToken token : lorenzo.getUsedDeck()) {
            discardedTokens[i++] = token.getType();
        }
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Lorenzo lorenzo = (Lorenzo) observable;
        setFaithFromGame(lorenzo);
        setActiveTokensFromGame(lorenzo);
        setDiscardedTokensFromGame(lorenzo);
    }
}
