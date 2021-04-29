package network.beans;

import model.Game;
import model.Market;
import model.Observer;
import model.ResourceType;

public class MarketBean implements Observer {
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

    private void setMarketBoardFromGame(Market market) {
        marketBoard = new ResourceType[market.getBoard().length][market.getBoard()[0].length];
        for (int i = 0; i < market.getBoard().length; i++) {
            for (int j = 0; j < market.getBoard()[0].length; j++) {
                marketBoard[i][j] = market.getBoard()[i][j].getType();
            }
        }
    }

    public void setSlideFromGame(Market market) {
        slide = market.getSlideOrb().getType();
    }

    // OBSERVER METHODS

    public void update(Object observable) {
        Market market = (Market) observable;
        setMarketBoardFromGame(market);
        setSlideFromGame(market);
    }

}
