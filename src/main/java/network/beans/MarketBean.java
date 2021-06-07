package network.beans;

import Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import model.Color;
import model.Market;
import model.Observer;
import model.resource.ResourceType;
import network.MessageType;
import server.GameController;

/**
 * Class used to serialize a Market object, send it over the network and store its information in the client
 */
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

    /**
     * This method is used to print only one line of the Market so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {
        line--;
        if (line < 0 || line > marketBoard.length)
            throw new ParametersNotValidException();
        String content = "";

        if (line == 3)
            content += " Slide: " + slide.marblePrint();
        else
            for (ResourceType cell : marketBoard[line]) {
                content += " " + Color.RESET + cell.marblePrint() + Color.RESET + " ";
            }

        return content;
    }

    @Override
    public String toString() {
        String board = "";
        for (ResourceType[] row : marketBoard) {
            for (ResourceType cell : row) {
                board += " " + Color.RESET + cell.marblePrint() + Color.RESET + " ";
            }
            board += "\n\n ";
        }
        /* for (int i = 0; i < marketBoard.length; i++) {
            for (int j = 0; j < marketBoard[0].length; j++) {
                board += marketBoard[i][j].formattedString() + "\u001B[0m ";
            }
            board += "\n\n   ";
        } */
        return Color.HEADER + "Market:\n " + Color.RESET +
                board +
                "Slide: " + slide.marblePrint() + "\n";
    }
}
