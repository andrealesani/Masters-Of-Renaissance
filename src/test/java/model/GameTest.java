package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.card.leadercard.DepotDecorator;
import model.card.leadercard.LeaderCard;
import model.card.leadercard.LeaderCardImpl;
import model.storage.BasicDepot;
import model.storage.Warehouse;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

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
            reader = new JsonReader(new FileReader("./src/main/java/model/test.json"));
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
            reader = new JsonReader(new FileReader("./src/main/java/model/test2.json"));
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
            reader = new JsonReader(new FileReader("./src/main/java/model/test2.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DepotDecorator depotLeaderCard;
        depotLeaderCard = gson.fromJson(reader, DepotDecorator.class);

        assertTrue(depotLeaderCard.getVictoryPoints() == 10);
    }
}