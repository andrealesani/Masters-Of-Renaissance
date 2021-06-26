package model;

import model.card.CardColor;
import model.card.DevelopmentCard;
import model.card.leadercard.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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