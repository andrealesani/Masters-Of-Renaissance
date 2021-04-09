package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.leadercard.*;
import model.resource.ResourceCoin;
import model.storage.BasicDepot;
import model.storage.ResourceDepot;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void initializeLeaders() {
        Game game = new Game();
    }

    @Test
    void testJSON() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/test.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BasicDepot basicDepot;
        basicDepot = gson.fromJson(reader, BasicDepot.class);

        assertTrue(basicDepot.getSize() == 10);
    }

    @Test
    void testJSONLeader() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/test2.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LeaderCardImpl leaderCard;
        leaderCard = gson.fromJson(reader, LeaderCardImpl.class);

        assertTrue(leaderCard.getVictoryPoints() == 10);
    }

    @Test
    void testJSONDecorator() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/test2.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DepotDecorator depotLeaderCard;
        depotLeaderCard = gson.fromJson(reader, DepotDecorator.class);

        assertTrue(depotLeaderCard.getVictoryPoints() == 10);
    }

    @Test
    void testJSONDepotDecoratorArray() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/test3.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Type DepotDecArray = new TypeToken<ArrayList<DepotDecorator>>() {
        }.getType();
        ArrayList<DepotDecorator> depotLeaderCards = gson.fromJson(reader, DepotDecArray);

        assertTrue(depotLeaderCards.get(0).getVictoryPoints() == 10
                && depotLeaderCards.get(0).getRequiredResource() == ResourceType.STONE
                && depotLeaderCards.get(0).getRequiredQuantity() == 1
                && depotLeaderCards.get(0).getStorableResource() == ResourceType.COIN
                && depotLeaderCards.get(0).getStorableQuantity() == 2
        );
        assertTrue(depotLeaderCards.get(1).getVictoryPoints() == 20
                && depotLeaderCards.get(1).getRequiredResource() == ResourceType.SHIELD
                && depotLeaderCards.get(1).getRequiredQuantity() == 3
                && depotLeaderCards.get(1).getStorableResource() == ResourceType.COIN
                && depotLeaderCards.get(1).getStorableQuantity() == 1
        );
        assertTrue(depotLeaderCards.get(2).getVictoryPoints() == 30
                && depotLeaderCards.get(2).getRequiredResource() == ResourceType.SERVANT
                && depotLeaderCards.get(2).getRequiredQuantity() == 2
                && depotLeaderCards.get(2).getStorableResource() == ResourceType.COIN
                && depotLeaderCards.get(2).getStorableQuantity() == 3
        );
    }

    @Test
    void testJSONDepotDiscountArray() {
        Gson gson = new Gson();
        JsonReader reader = null;

        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/test4.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountDecorator>>() {
        }.getType();
        ArrayList<DiscountDecorator> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        assertTrue(discountLeaderCards.get(0).getVictoryPoints() == 10
                && discountLeaderCards.get(0).getResourceType() == ResourceType.STONE
                && discountLeaderCards.get(0).getDiscount() == 1
                && discountLeaderCards.get(0).getRequiredColor() == CardColor.BLUE
                && discountLeaderCards.get(0).getRequiredQuantity() == 1
        );
    }
}