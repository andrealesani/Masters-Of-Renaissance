package it.polimi.ingsw.model.card.leadercard;

import it.polimi.ingsw.Exceptions.BlockedResourceException;
import it.polimi.ingsw.Exceptions.CardAlreadyActiveException;
import it.polimi.ingsw.Exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.Exceptions.WrongResourceInsertionException;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.card.leadercard.DepotLeaderCard;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DepotLeaderCardTest {

    /**
     * Test for the leader card's activation
     */
    @Test
    void doAction() throws WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException, CardAlreadyActiveException {
        DepotLeaderCard leaderCard = new DepotLeaderCard(500, ResourceType.STONE, 5, ResourceType.COIN, 5);
        PlayerBoard playerBoard = new PlayerBoard();

        leaderCard.doAction(playerBoard);
        assertEquals(4, playerBoard.getWarehouse().getNumOfDepots());

        playerBoard.getWarehouse().getDepot(4).addResource(ResourceType.COIN, 2);

        Exception ex = assertThrows(NotEnoughSpaceException.class, () -> {
            playerBoard.getWarehouse().getDepot(4).addResource(ResourceType.COIN, 6);
        });

        Exception ex1 = assertThrows(WrongResourceInsertionException.class, () -> {
            playerBoard.getWarehouse().getDepot(4).addResource(ResourceType.STONE, 3);
        });
    }

    /**
     * Test for the method that verifies if the leader card's requirements for activation are met
     */
    @Test
    void areRequirementsMet() {
        DepotLeaderCard leaderCard = new DepotLeaderCard(500, ResourceType.STONE, 5, ResourceType.COIN, 5);
        PlayerBoard playerBoard = new PlayerBoard();

        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResources(Map.of(ResourceType.COIN, 500));
        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResources(Map.of(ResourceType.STONE, 4));
        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResources(Map.of(ResourceType.STONE, 1));
        assertTrue(leaderCard.areRequirementsMet(playerBoard));
    }
}