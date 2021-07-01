package it.polimi.ingsw.model.card.leadercard;

import it.polimi.ingsw.Exceptions.CardAlreadyActiveException;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.card.leadercard.ProductionLeaderCard;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFaith;
import it.polimi.ingsw.model.resource.ResourceServant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductionLeaderCardTest {
    /**
     * Test for the leader card's activation
     */
    @Test
    void doAction() throws CardAlreadyActiveException {
        // Creates Leader Card
        ResourceType[] inputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY};
        int[] inputQuantities = {0, 2, 0, 0, 0};
        ResourceType[] outputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY, ResourceType.FAITH};
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