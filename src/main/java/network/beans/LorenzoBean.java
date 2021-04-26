package network.beans;

import model.Game;
import model.PlayerBoard;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;

public class LorenzoBean {
    private int faith;
    private TokenType[] activeTokens = new TokenType[6];
    private TokenType[] discardedTokens = new TokenType[6];

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
        for(ActionToken token : ((Lorenzo) game.getLorenzo()).getActiveDeck()) {
            activeTokens[i++] = token.getType();
        }
    }

    public void setDiscardedTokensFromGame(Game game) {
        int i = 0;
        for(ActionToken token : ((Lorenzo) game.getLorenzo()).getUsedDeck()) {
            discardedTokens[i++] = token.getType();
        }
    }
}
