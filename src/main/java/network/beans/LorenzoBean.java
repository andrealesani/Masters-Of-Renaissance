package network.beans;

import model.Game;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;

public class LorenzoBean {
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

    public void setFaithFromGame(Game game) {
        faith = game.getLorenzo().getFaith();
    }

    public void setActiveTokensFromGame(Game game) {
        int i = 0;
        activeTokens = new TokenType[((Lorenzo) game.getLorenzo()).getActiveDeck().size()];
        for(ActionToken token : ((Lorenzo) game.getLorenzo()).getActiveDeck()) {
            activeTokens[i++] = token.getType();
        }
    }

    public void setDiscardedTokensFromGame(Game game) {
        int i = 0;
        discardedTokens = new TokenType[((Lorenzo) game.getLorenzo()).getUsedDeck().size()];
        for(ActionToken token : ((Lorenzo) game.getLorenzo()).getUsedDeck()) {
            discardedTokens[i++] = token.getType();
        }
    }
}
