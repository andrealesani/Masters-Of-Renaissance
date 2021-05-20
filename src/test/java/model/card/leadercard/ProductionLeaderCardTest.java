package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.resource.ResourceType;
import model.resource.Resource;
import model.resource.ResourceFaith;
import model.resource.ResourceServant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLeaderCardTest {

    @Test
    void doAction() {
        // Creates Leader Card
        ResourceType[] inputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.UNKNOWN};
        int[] inputQuantities = {0, 2, 0, 0, 0};
        ResourceType[] outputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.UNKNOWN, ResourceType.FAITH};
        int[] outputQuantities = {0, 2, 0, 0, 0, 1};

        ProductionLeaderCard productionLeaderCard = new ProductionLeaderCard(5, CardColor.BLUE, 2, 2, inputType, inputQuantities, outputType, outputQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        // Creates the Production that the card should add to use equals in the test
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceServant());
        output.add(new ResourceServant());
        output.add(new ResourceServant());
        output.add(new ResourceFaith());

        // TEST
        productionLeaderCard.doAction(playerBoard);
        assertEquals(1, playerBoard.getProductionHandler().getProductions().size());
        assertTrue(playerBoard.getProductionHandler().getProductions().get(0).equals(new Production(-1, input, output)));
    }
}