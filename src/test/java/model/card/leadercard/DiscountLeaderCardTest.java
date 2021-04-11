package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.card.DevelopmentCard;
import model.resource.ResourceStone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountLeaderCardTest {

    @Test
    void doAction() {
        ArrayList<CardColor> requiredColors = new ArrayList<>();
        requiredColors.add(CardColor.GREEN);
        requiredColors.add(CardColor.BLUE);
        int[] requiredQuantities = {1,2};

        DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(5, ResourceType.COIN, 2, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertTrue(playerBoard.getDiscounts().isEmpty());
        discountLeaderCard.doAction(playerBoard);
        assertFalse(playerBoard.getDiscounts().isEmpty());
        assertEquals(2, playerBoard.getDiscounts().get(ResourceType.COIN));
    }

    @Test
    void areRequirementsMet() {
        ArrayList<CardColor> requiredColors = new ArrayList<>();
        requiredColors.add(CardColor.GREEN);
        requiredColors.add(CardColor.BLUE);
        int[] requiredQuantities = {1,2};
        DevelopmentCard developmentCard = new DevelopmentCard(10, 1, CardColor.GREEN, null, null);

        DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(5, ResourceType.COIN, 2, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertFalse(discountLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCard(1, developmentCard);
        assertFalse(discountLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCard(1, new DevelopmentCard(10, 2, CardColor.BLUE, null, null));
        //assertTrue(discountLeaderCard.areRequirementsMet(playerBoard));
    }
}