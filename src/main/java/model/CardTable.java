package model;

import Exceptions.EmptyDeckException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.DevelopmentCard;
import model.card.leadercard.DepotDecorator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents what in the physical game is the grid that holds all the development cards still available for players to buy
 */
public class CardTable {
    private final List<List<DevelopmentCard>> greenCards;
    private final List<List<DevelopmentCard>> blueCards;
    private List<List<DevelopmentCard>> yellowCards;
    private List<List<DevelopmentCard>> purpleCards;

    public CardTable() {
        Gson gson = new Gson();
        JsonReader reader = null;
        Type DevCardArray = new TypeToken<ArrayList<DevelopmentCard>>() {
        }.getType();

        // GREEN CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/GreenCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        greenCards = gson.fromJson(reader, DevCardArray);

        // BLUE CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/BlueCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        blueCards = gson.fromJson(reader, DevCardArray);

        // YELLOW CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/YellowCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        yellowCards = gson.fromJson(reader, DevCardArray);

        // PURPLE CARDS
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/PurpleCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        purpleCards = gson.fromJson(reader, DevCardArray);
    }

    /**
     * Transforms the input CardColor into the corresponding column of the CardTable
     *
     * @param cardColor color received in input
     * @return the column in the grid that contains the cards of the specified color
     */
    private List<List<DevelopmentCard>> colorToColumn(CardColor cardColor) {
        List<List<DevelopmentCard>> deckColumn = null;
        if (cardColor.equals(CardColor.GREEN))
            deckColumn = greenCards;
        else if (cardColor.equals(CardColor.BLUE))
            deckColumn = blueCards;
        else if (cardColor.equals(CardColor.YELLOW))
            deckColumn = yellowCards;
        else if (cardColor.equals(CardColor.PURPLE))
            deckColumn = purpleCards;

        return deckColumn;
    }

    /**
     * Adds the selected card to the specified PlayerBoard in the specified slot
     *
     * @param cardColor   specifies the column of the card in the grid
     * @param row         specifies the row of the card in the grid
     * @param playerBoard specifies which player is buying the card
     * @param cardSlot    specifies in which production slot the player wants to put the card
     */
    public void buyTopCard(CardColor cardColor, int row, PlayerBoard playerBoard, int cardSlot) {
        playerBoard.addDevelopmentCard(cardSlot, colorToColumn(cardColor).get(row).get(0));
    }

    /**
     * Removes from the grid the card of the specified color with the lowest level (this method should only be called only in solo mode)
     *
     * @param cardColor specifies the color of the card that has to be removed
     */
    public void discardTop(CardColor cardColor) throws EmptyDeckException {
        List<List<DevelopmentCard>> deckColumn = colorToColumn(cardColor);

        if (deckColumn.get(2).size() > 0)
            deckColumn.get(0).remove(0);
        else if (deckColumn.get(1).size() > 0)
            deckColumn.get(1).remove(0);
        else if (deckColumn.get(0).size() > 0)
            deckColumn.get(0).remove(0);
        else throw new EmptyDeckException();
    }

    /**
     * Checks if there is at least one card of each color still available (this method should only be called in solo mode)
     *
     * @return true if there's at least one card of each color still available
     */
    public boolean checkAllColorsAvailable() {
        for (List<DevelopmentCard> deck : greenCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : blueCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : yellowCards) {
            if (deck.size() < 1)
                return false;
        }
        for (List<DevelopmentCard> deck : purpleCards) {
            if (deck.size() < 1)
                return false;
        }

        return true;
    }
}
