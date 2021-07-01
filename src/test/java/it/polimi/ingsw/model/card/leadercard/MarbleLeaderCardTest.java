package it.polimi.ingsw.model.card.leadercard;

import it.polimi.ingsw.Exceptions.CardAlreadyActiveException;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.card.leadercard.MarbleLeaderCard;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarbleLeaderCardTest {

    /**
     * Test for the leader card's activation
     */
    @Test
    void doAction() throws CardAlreadyActiveException {
        CardColor[] requiredColors = {CardColor.GREEN, CardColor.BLUE};
        int[] requiredQuantities = {1,2};

        MarbleLeaderCard marbleLeaderCard = new MarbleLeaderCard(5, ResourceType.COIN, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertTrue(playerBoard.getMarbleConversions().isEmpty());
        marbleLeaderCard.doAction(playerBoard);
        assertEquals(1, playerBoard.getMarbleConversions().size());
        assertEquals(ResourceType.COIN, playerBoard.getMarbleConversions().get(0));
    }

    /**
     * Test for the method that verifies if the leader card's requirements for activation are met
     */
    @Test
    void areRequirementsMet() {
        // Discount leader card parameters
        CardColor[] requiredColors = {CardColor.GREEN, CardColor.BLUE};
        int[] requiredQuantities = {1,2};

        // Development card parameters
        ResourceType[] costType = {ResourceType.SHIELD, ResourceType.SERVANT};
        int[] costQuantity = {2, 1};
        ResourceType[] inputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY};
        int[] inputQuantity = {0, 2, 0, 0, 0};
        ResourceType[] outputType = {ResourceType.COIN, ResourceType.SERVANT, ResourceType.SHIELD, ResourceType.STONE, ResourceType.JOLLY, ResourceType.FAITH};
        int[] outputQuantity = {0, 2, 0, 0, 1};

        DevelopmentCard developmentCard = new DevelopmentCard(10, 1, CardColor.GREEN, costType, costQuantity, inputType, inputQuantity, outputType, outputQuantity);
        MarbleLeaderCard marbleLeaderCard = new MarbleLeaderCard(5, ResourceType.COIN, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertFalse(marbleLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCardNoCheck(1, developmentCard);
        assertFalse(marbleLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCardNoCheck(1, new DevelopmentCard(10, 2, CardColor.BLUE, costType, costQuantity, inputType, inputQuantity, outputType, outputQuantity));
        assertFalse(marbleLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCardNoCheck(1, new DevelopmentCard(10, 2, CardColor.BLUE, costType, costQuantity, inputType, inputQuantity, outputType, outputQuantity));
        assertTrue(marbleLeaderCard.areRequirementsMet(playerBoard));
    }
}