package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.resource.ResourceType;
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
            reader = new JsonReader(new FileReader("./src/main/resources/json/cards/developmentcards/BlueCards.json"));
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
            reader = new JsonReader(new FileReader("./src/main/resources/json/cards/developmentcards/YellowCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DevelopmentArray = new TypeToken<ArrayList<DevelopmentCard>>() {}.getType();
        ArrayList<DevelopmentCard> blueDevCards = gson.fromJson(reader, DevelopmentArray);
        assertTrue(blueDevCards.get(0).getVictoryPoints() == 3);
        assertTrue(blueDevCards.get(0).getLevel() == 1);
        assertTrue(blueDevCards.get(0).getColor() == CardColor.YELLOW);
    }

    /**
     * Test for card constructor
     */
    @Test
    void devCardFromConstructor() {
        // Development card parameters
        ResourceType[] costType = {ResourceType.SHIELD, ResourceType.SERVANT};
        int[] costQuantity = {2, 1};
        ResourceType[] inputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY};
        int[] inputQuantity = {0, 2, 0, 0, 0};
        ResourceType[] outputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY, ResourceType.FAITH};
        int[] outputQuantity = {0, 2, 0, 0, 0, 1};

        DevelopmentCard developmentCard = new DevelopmentCard(10, 1, CardColor.GREEN, costType, costQuantity, inputType, inputQuantity, outputType, outputQuantity);
    }
}