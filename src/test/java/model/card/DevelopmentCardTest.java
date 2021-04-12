package model.card;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.CardColor;
import model.ResourceType;
import model.card.leadercard.DiscountLeaderCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardTest {

    /**
     * Test for JSON instantiation
     */
    @Test
    void JSONBlueDevelopmentArray() {
        Gson gson = new Gson();
        JsonReader reader = null;

        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/BlueCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DevelopmentArray = new TypeToken<ArrayList<DevelopmentCard>>() {}.getType();
        ArrayList<DevelopmentCard> blueDevCards = gson.fromJson(reader, DevelopmentArray);
        assertTrue(blueDevCards.get(0).getVictoryPoints() == 12);
        assertTrue(blueDevCards.get(0).getLevel() == 3);
        assertTrue(blueDevCards.get(0).getColor() == CardColor.BLUE);
    }

    /**
     * Test for JSON instantiation
     */
    @Test
    void JSONYellowDevelopmentArray() {
        Gson gson = new Gson();
        JsonReader reader = null;

        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/developmentcards/YellowCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DevelopmentArray = new TypeToken<ArrayList<DevelopmentCard>>() {}.getType();
        ArrayList<DevelopmentCard> blueDevCards = gson.fromJson(reader, DevelopmentArray);
        assertTrue(blueDevCards.get(0).getVictoryPoints() == 3);
        assertTrue(blueDevCards.get(0).getLevel() == 1);
        assertTrue(blueDevCards.get(0).getColor() == CardColor.YELLOW);
    }
}