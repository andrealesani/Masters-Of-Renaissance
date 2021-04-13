package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.ResourceType;
import model.resource.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLeaderCardTest {

    @Test
    void doAction() {

        ResourceType[] inputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.UNKNOWN};
        int[] inputQuantities = {0, 2, 0, 0, 0};
        ResourceType[] outputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.UNKNOWN, ResourceType.FAITH};
        int[] outputQuantities = {0, 2, 0, 0, 1};

        ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(5, CardColor.BLUE, 2, 2, inputType, inputQuantities, outputType, outputQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        productionLeaderCard.doAction(playerBoard);
    }
}