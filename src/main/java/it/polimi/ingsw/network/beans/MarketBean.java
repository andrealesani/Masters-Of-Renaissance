package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.GameController;

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

    // CONSTRUCTORS

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     */
    public MarketBean(GameController controller) {
        this.controller = controller;
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        Market market = (Market) observable;
        setMarketBoardFromGame(market);
        setSlideFromGame(market);

        controller.broadcastMessage(MessageType.MARKET, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.MARKET, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * This method is used to print only one line of the Market so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line < 1 || line > marketBoard.length + 1)
            throw new ParametersNotValidException();

        String content = "";

        line--;

        if (line == marketBoard.length)
            //Row 4
            content += " Slide: " + slide.marblePrint();
        else {
            //Rows 1, 2, 3
            for (ResourceType cell : marketBoard[line]) {
                content += " " + Color.RESET + cell.marblePrint() + Color.RESET + " ";
            }
        }

        return content;
    }

    /**
     * Prints a String representation of the bean's data
     *
     * @return the String representation
     */
    @Override
    public String toString() {

        String result = Color.HEADER + "Market:\n " + Color.RESET;

        for (int i = 1; i <= 4; i++) {
            result += printLine(i) + "\n\n";
        }

        return result;
    }

    // GETTERS

    /**
     * Getter for the market's board
     *
     * @return a matrix of ResourceType representing the market's marbles
     */
    public ResourceType[][] getMarketBoard() {
        ResourceType[][] result = new ResourceType[marketBoard.length][];

        for (int i = 0; i < marketBoard.length; i++)
            result[i] = marketBoard[i].clone();

        return result;
    }

    /**
     * Getter for the market's slide marble
     *
     * @return a ResourceType representing the market's slide marble
     */
    public ResourceType getSlide() {
        return slide;
    }

    // SETTERS

    /**
     * Sets the market's board
     *
     * @param market the object to take the information from
     */
    private void setMarketBoardFromGame(Market market) {
        marketBoard = new ResourceType[market.getBoard().length][market.getBoard()[0].length];
        for (int i = 0; i < market.getBoard().length; i++) {
            for (int j = 0; j < market.getBoard()[0].length; j++) {
                marketBoard[i][j] = market.getBoard()[i][j].getType();
            }
        }
    }

    /**
     * Sets the market's slide marble
     *
     * @param market the object to take the information from
     */
    private void setSlideFromGame(Market market) {
        slide = market.getSlideOrb().getType();
    }
}
