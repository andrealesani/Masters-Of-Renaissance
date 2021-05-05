package model.card.leadercard;

import exceptions.BlockedResourceException;
import exceptions.NotEnoughSpaceException;
import exceptions.WrongResourceInsertionException;
import model.PlayerBoard;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepotLeaderCardTest {

    @Test
    void doAction() throws WrongResourceInsertionException, NotEnoughSpaceException, BlockedResourceException {
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

    @Test
    void areRequirementsMet() {
        DepotLeaderCard leaderCard = new DepotLeaderCard(500, ResourceType.STONE, 5, ResourceType.COIN, 5);
        PlayerBoard playerBoard = new PlayerBoard();

        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResource(ResourceType.COIN, 500);
        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResource(ResourceType.STONE, 4);
        assertFalse(leaderCard.areRequirementsMet(playerBoard));

        playerBoard.getStrongbox().addResource(ResourceType.STONE, 1);
        assertTrue(leaderCard.areRequirementsMet(playerBoard));
    }
}