package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.resource.ResourceCoin;
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
        assertEquals(1, playerBoard.getMarbleConversions().size());
        assertEquals(ResourceType.COIN, playerBoard.getMarbleConversions().get(0));
    }
}