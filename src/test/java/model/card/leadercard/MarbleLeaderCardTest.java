package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.resource.ResourceStone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarbleLeaderCardTest {

    @Test
    void doAction() {
        CardColor[] requiredColors = {CardColor.GREEN, CardColor.BLUE};
        int[] requiredQuantities = {1,2};

        MarbleLeaderCard marbleLeaderCard = new MarbleLeaderCard(5, ResourceType.COIN, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertTrue(playerBoard.getMarbleConversions().isEmpty());
        marbleLeaderCard.doAction(playerBoard);
        assertFalse(playerBoard.getDiscounts().isEmpty());
        assertEquals(2, playerBoard.getDiscounts().get(ResourceType.COIN));
    }
}