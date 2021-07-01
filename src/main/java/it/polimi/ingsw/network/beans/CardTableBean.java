package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.CardNotPresentException;
import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import com.google.gson.Gson;
import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.StaticMethods;
import it.polimi.ingsw.server.GameController;

import java.util.*;

/**
 * Class used to serialize a CardTable object, send it over the network and store its information in the client
 */
public class CardTableBean implements Observer {
    /**
     * The Controller that will have to send the bean when it changes
     */
    private transient final GameController controller;
    /**
     * Holds the IDs of the cards in CardTable's cards attribute
     */
    private int[][] cards;
    /**
     * Map that holds the information about all the DevelopmentCards
     */
    private transient Map<CardColor, List<List<DevelopmentCard>>> developmentCards;
    /**
     * Boolean parameters that is used by methods that need cards data to check if the cards have already been initialized
     */
    private transient boolean cardsInitialized = false;

    // CONSTRUCTOR

    /**
     * Constructor
     *
     * @param controller the GameController for the bean's game
     */
    public CardTableBean(GameController controller) {
        this.controller = controller;
    }

    //PRIVATE METHODS

    //TODO spostare in clientView

    /**
     * Sets the developmentCards list reading them from DevelopmentCards.json file
     */
    private void setDevelopmentCardsFromJson() {
        developmentCards = new HashMap<>();
        int id = 17;
        List<List<DevelopmentCard>> greenCards = new ArrayList<>();
        List<List<DevelopmentCard>> blueCards = new ArrayList<>();
        List<List<DevelopmentCard>> yellowCards = new ArrayList<>();
        List<List<DevelopmentCard>> purpleCards = new ArrayList<>();

        // BLUE CARDS
        StaticMethods.createDecksFromJSON(blueCards, CardColor.BLUE);
        developmentCards.put(CardColor.BLUE, blueCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.BLUE))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // GREEN CARDS
        StaticMethods.createDecksFromJSON(greenCards, CardColor.GREEN);
        developmentCards.put(CardColor.GREEN, greenCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.GREEN))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // PURPLE CARDS
        StaticMethods.createDecksFromJSON(purpleCards, CardColor.PURPLE);
        developmentCards.put(CardColor.PURPLE, purpleCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.PURPLE))
            for (DevelopmentCard card : deck)
                card.setId(id++);

        // YELLOW CARDS
        StaticMethods.createDecksFromJSON(yellowCards, CardColor.YELLOW);
        developmentCards.put(CardColor.YELLOW, yellowCards);
        for (List<DevelopmentCard> deck : developmentCards.get(CardColor.YELLOW))
            for (DevelopmentCard card : deck)
                card.setId(id++);
    }

    // OBSERVER METHODS

    /**
     * Updates the bean with the information contained in the observed class, then broadcasts its serialized self to all players
     *
     * @param observable the observed class
     */
    public void update(Object observable) {
        Gson gson = new Gson();
        CardTable cardTable = (CardTable) observable;
        setCardTableFromGame(cardTable);

        controller.broadcastMessage(MessageType.CARDTABLE_BEAN, gson.toJson(this));
    }

    /**
     * Sends the serialized bean to the player with the given username
     *
     * @param username the username of the player to send the serialized bean to
     */
    public void updateSinglePlayer(String username) {
        Gson gson = new Gson();
        controller.playerMessage(username, MessageType.CARDTABLE_BEAN, gson.toJson(this));
    }

    //PRINTING METHODS

    /**
     * Prints only one line of the CardTable so that multiple objects can be printed in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line < 1 || line > cards.length)
            throw new ParametersNotValidException();

        String content = "";

        //Print the level of the cards in the row
        content += "Level " + (line) + "   ";

        line--;

        //Print the row of cards
        for (int cell : cards[line]) {

            //If there is no card in the cell
            if (cell == -1)
                content += " " + " " + "xx" + " " + " ";

            try {

                //Print the cell's card' id in the correct color
                if (getDevelopmentCardFromId(cell).getColor() == CardColor.BLUE)
                    content += " " + Color.BLUE_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.GREEN)
                    content += " " + Color.GREEN_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.PURPLE)
                    content += " " + Color.PURPLE_BG + " " + cell + " " + Color.RESET + " ";
                else if (getDevelopmentCardFromId(cell).getColor() == CardColor.YELLOW)
                    content += " " + Color.YELLOW_DARK_BG + " " + cell + " " + Color.RESET + " ";

            } catch (CardNotPresentException e) {
                System.out.println("Warning: tried to read an ID that doesn't correspond to any DevelopmentCard");
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

        if (!cardsInitialized) {
            setDevelopmentCardsFromJson();
            cardsInitialized = true;
        }

        String result = Color.HEADER + "CardTable:\n" + Color.RESET;

        for (int i = 1; i <= 3; i++) {
            result += printLine(i) + "\n\n";
        }

        return result;
    }

    // GETTERS

    /**
     * Getter for the card table
     *
     * @return a 2D matrix with the IDs of the cards on top of the CardTable
     */
    public int[][] getCards() {
        int[][] result = new int[cards.length][];

        for (int i = 0; i < cards.length; i++)
            result [i] = cards[i].clone();

        return result;
    }

    /**
     * Getter for a development card with a specific id
     *
     * @param id of the card to be returned
     * @return the DevelopmentCard associated to the specified ID
     * @throws CardNotPresentException when the given ID is not associated with any of the cards in the CardTable
     */
    public DevelopmentCard getDevelopmentCardFromId(int id) throws CardNotPresentException {
        if (!cardsInitialized) {
            setDevelopmentCardsFromJson();
            cardsInitialized = true;
        }
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : developmentCards.entrySet()) {
            for (List<DevelopmentCard> deck : color.getValue()) {
                for (DevelopmentCard developmentCard : deck) {
                    if (developmentCard.getId() == id) {
                        return developmentCard;
                    }
                }
            }
        }
        throw new CardNotPresentException(id);
    }

    // SETTERS

    /**
     * Sets the matrix representing the card table's top layer
     *
     * @param cardTable the object to take the information from
     */
    private void setCardTableFromGame(CardTable cardTable) {
        cards = new int[3][cardTable.getCards().entrySet().size()];
        int i, j;
        for (i = 0; i < cards.length; i++) {
            j = 0;
            for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cardTable.getCards().entrySet()) {
                if (color.getValue().get(i).size() == 0)
                    cards[i][j] = -1;
                else
                    cards[i][j] = color.getValue().get(i).get(0).getId();
                j++;
            }
        }
    }
}
