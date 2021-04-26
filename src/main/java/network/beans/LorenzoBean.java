package network.beans;

public class LorenzoBean {
    private int faith;
    private TokenType[] activeTokens;
    private TokenType[] discardedTokens;

    public int getFaith() {
        return faith;
    }

    public TokenType[] getActiveTokens() {
        return activeTokens;
    }

    public TokenType[] getDiscardedTokens() {
        return discardedTokens;
    }
}
