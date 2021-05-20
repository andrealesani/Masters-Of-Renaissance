package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.card.DevelopmentCard;
import model.card.leadercard.*;
import model.resource.ResourceType;
import model.storage.BasicDepot;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.resource.ResourceType.STONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void initializeLeaders() {
        Game game = new Game();
    }

    // Verifies that card IDs are unique consistently between both DevelopmentCards and LeaderCards
    @Test
    void newIds() {
        Game game = new Game();
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> entry : game.getCardTable().getCards().entrySet()) {
            for(List<DevelopmentCard> deck : entry.getValue()) {
                for(DevelopmentCard card : deck) {
                    for(LeaderCard lc : game.getLeaderCards()){
                        assertNotEquals(card.getId(), lc.getId());
                    }
                }
            }
        }
    }
}