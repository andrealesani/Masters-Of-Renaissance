package network.beans;

import model.Game;
import model.ResourceType;

public class MarketBean {
    /**
     * Represents Market's board attribute
     */
    private ResourceType[][] marketBoard;
    /**
     * Represents Market's slideMarble
     */
    private ResourceType slide;

    // GETTERS

    public ResourceType[][] getMarketBoard() {
        return marketBoard;
    }

    public ResourceType getSlide() {
        return slide;
    }

    // SETTERS

    public void setMarketBoardFromGame(Game game) {
        marketBoard = new ResourceType[game.getMarket().getBoard().length][game.getMarket().getBoard()[0].length];
        for(int i = 0; i < game.getMarket().getBoard().length; i++) {
            for(int j = 0; j < game.getMarket().getBoard()[0].length; j++) {
                marketBoard[i][j] = game.getMarket().getBoard()[i][j].getType();
            }
        }
    }

    public void setSlideFromGame(Game game) {
        slide = game.getMarket().getSlideOrb().getType();
    }

}
