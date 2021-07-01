package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductionHandlerTest {

    /**
     * Tests the current input after production selection
     */
    @Test
    void getCurrentInputAfterSelect() throws ProductionNotPresentException {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        assertTrue(productionHandler.getCurrentInput().get(0) instanceof ResourceServant);
    }

    /**
     * Tests the current output after production selection
     */
    @Test
    void getCurrentOutputAfterSelect() throws ProductionNotPresentException {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceFaith());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        assertTrue(productionHandler.getCurrentOutput().get(0) instanceof ResourceFaith);
    }

    /**
     * Tests the choice of jollies in input for productions
     */
    @Test
    void chooseJollyInput() throws ProductionNotPresentException, NotEnoughResourceException {
        // Creates an instance of every resource type
        ResourceJolly unknown = new ResourceJolly();
        ResourceCoin coin = new ResourceCoin();
        ResourceServant servant = new ResourceServant();
        ResourceStone stone = new ResourceStone();
        ResourceFaith faith = new ResourceFaith();
        ResourceShield shield = new ResourceShield();

        // Then creates a ProductionHandler with one Production that has 1 ResourceJolly in input and 1 in output.
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(unknown);
        output.add(unknown);
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);

        // Then it selects the production
        productionHandler.selectProduction(1);

        // TEST
        productionHandler.chooseJollyInput(coin);

        assertTrue(productionHandler.getCurrentInput().size() == 1, "wrong output size");
        assertTrue(productionHandler.getCurrentInput().get(0) instanceof ResourceCoin, "wrong ResourceType in output");

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            productionHandler.chooseJollyInput(stone);
        });
    }

    /**
     * Tests the choice of jollies in output for productions
     */
    @Test
    void chooseJollyOutput() throws ProductionNotPresentException, NotEnoughResourceException {
        // Creates an instance of every resource type
        ResourceJolly unknown = new ResourceJolly();
        ResourceCoin coin = new ResourceCoin();
        ResourceServant servant = new ResourceServant();
        ResourceStone stone = new ResourceStone();
        ResourceFaith faith = new ResourceFaith();
        ResourceShield shield = new ResourceShield();

        // Then creates a ProductionHandler with one Production that has 1 ResourceJolly in input and 1 in output.
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(unknown);
        output.add(unknown);
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);

        // Then it selects the production
        productionHandler.selectProduction(1);

        // TEST
        productionHandler.chooseJollyOutput(coin);

        assertEquals(1, productionHandler.getCurrentOutput().size(), "wrong output size");
        assertTrue(productionHandler.getCurrentOutput().get(0) instanceof ResourceCoin, "wrong ResourceType in output");

        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            productionHandler.chooseJollyOutput(stone);
        });
    }

    /**
     * Tests the reset of chosen productions
     */
    @Test
    void resetProductionChoice() throws ProductionNotPresentException {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        assertFalse(productionHandler.getCurrentInput().isEmpty());
        assertFalse(productionHandler.getCurrentOutput().isEmpty());

        productionHandler.resetProductionChoice();

        for (Production prod: productionHandler.getProductions()) {
            assertFalse(prod.isSelectedByHandler());
        }
        assertTrue(productionHandler.getCurrentInput().isEmpty());
        assertTrue(productionHandler.getCurrentOutput().isEmpty());
    }

    /**
     * Tests the method which verifies whether the player has enough resources to pay for the selected productions
     */
    @Test
    void resourcesAreEnough() throws UndefinedJollyException, DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException, BlockedResourceException, ProductionNotPresentException {
        // Creates a PlayerBoard
        PlayerBoard playerBoard = new PlayerBoard();

        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceFaith());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        Exception ex = assertThrows(NotEnoughResourceException.class, () -> {
            productionHandler.arePlayerResourcesEnough(playerBoard);
        });
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.COIN, 1);
        playerBoard.addResourcesToStrongbox(resources);
        ex = assertThrows(NotEnoughResourceException.class, () -> {
            productionHandler.arePlayerResourcesEnough(playerBoard);
        });

        // Resources are divided into strongbox and depots and together they're enough to activated all the productions
        resources.clear();
        resources.put(ResourceType.SERVANT, 1);
        playerBoard.addResourcesToStrongbox(resources);
        resources.clear();
        resources.put(ResourceType.SHIELD, 2);
        playerBoard.addResourcesToWaitingRoom(resources);
        playerBoard.sendResourceToDepot(2, ResourceType.SHIELD, 2);
        assertTrue(productionHandler.arePlayerResourcesEnough(playerBoard));
    }

    /**
     * Tests the method used to calculate the amount of a given resource that the player has to pay for production activation
     */
    @Test
    void getDebt() throws ProductionNotPresentException {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceCoin());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        assertEquals(1, productionHandler.getDebt(new ResourceServant()));
        assertEquals(2, productionHandler.getDebt(new ResourceShield()));
        assertEquals(0, productionHandler.getDebt(new ResourceStone()));
        assertEquals(0, productionHandler.getDebt(new ResourceCoin()));
        assertEquals(0, productionHandler.getDebt(new ResourceJolly()));
        assertEquals(0, productionHandler.getDebt(new ResourceFaith()));
    }

    /**
     * Tests the payment of resources as part of the activation of productions
     */
    @Test
    void takeResource() throws ProductionNotPresentException {
        // Creates a PlayerBoard
        PlayerBoard playerBoard = new PlayerBoard();

        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceCoin());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.COIN, 1);
        playerBoard.addResourcesToStrongbox(resources);
        resources.clear();
        resources.put(ResourceType.SERVANT, 1);
        playerBoard.addResourcesToStrongbox(resources);
        resources.clear();
        resources.put(ResourceType.SHIELD, 2);
        playerBoard.addResourcesToWaitingRoom(resources);
    }

    /**
     * Tests the release of the activated productions' output
     */
    @Test
    void releaseOutputTest() throws ProductionNotPresentException {
        PlayerBoard playerBoard = new PlayerBoard();
        assertEquals(0, playerBoard.getFaith());

        ProductionHandler productionHandler = playerBoard.getProductionHandler();

        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceFaith());
        output.add(new ResourceFaith());
        output.add(new ResourceCoin());
        Production production = new Production(-1, input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        productionHandler.releaseOutput(playerBoard);

        assertEquals(2, playerBoard.getFaith());
    }
}
