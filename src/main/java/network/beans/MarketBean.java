package network.beans;

import model.ResourceType;

public class MarketBean {
    private ResourceType[][] marketBoard;
    private ResourceType slide;

    public ResourceType[][] getMarketBoard() {
        return marketBoard;
    }

    public ResourceType getSlide() {
        return slide;
    }
}
