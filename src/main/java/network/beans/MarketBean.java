package network.beans;

import com.google.gson.Gson;
import model.Market;
import model.Observer;
import model.ResourceType;
import network.GameController;

import java.util.Arrays;

public class MarketBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Represents Market's board attribute
     */
    private ResourceType[][] marketBoard;
    /**
     * Represents Market's slideMarble
     */
    private ResourceType slide;

    // CONSTRUCTOR

    public MarketBean(GameController controller) {
        this.controller = controller;
    }

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
        Gson gson = new Gson();
        Market market = (Market) observable;
        setMarketBoardFromGame(market);
        setSlideFromGame(market);

        controller.broadcastMessage(MessageType.MARKET, gson.toJson(this));
    }

    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.MARKET, gson.toJson(this));
    }

    @Override
    public String toString() {
        String board = "";
        for(ResourceType[] row : marketBoard) {
            for (ResourceType cell : row) {
                board += cell.formattedString() + "\u001B[0m ";
            }
            board += "\n\n   ";
        }
        /* for (int i = 0; i < marketBoard.length; i++) {
            for (int j = 0; j < marketBoard[0].length; j++) {
                board += marketBoard[i][j].formattedString() + "\u001B[0m ";
            }
            board += "\n\n   ";
        } */
        return "\u001B[32;1mMarket:\u001B[0m\n   " +
                board +
                "Slide: " + slide.formattedString() + "\n";
    }
}
