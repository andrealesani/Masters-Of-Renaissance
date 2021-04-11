package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
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

        discountLeaderCard.doAction(playerBoard);

    }
}