package model;

import Exceptions.*;
import model.resource.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionHandlerTest {

    @Test
    void getCurrentInputAfterSelect() {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        assertTrue(productionHandler.getCurrentInput().get(0) instanceof ResourceServant);
    }

    @Test
    void getCurrentOutputAfterSelect() {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        assertTrue(productionHandler.getCurrentOutput().get(0) instanceof ResourceCoin);
    }


    @Test
    void chooseJollyInput() {
        // Creates an instance of every resource type
        ResourceUnknown unknown = new ResourceUnknown();
        ResourceCoin coin = new ResourceCoin();
        ResourceServant servant = new ResourceServant();
        ResourceStone stone = new ResourceStone();
        ResourceFaith faith = new ResourceFaith();
        ResourceShield shield = new ResourceShield();

        // Then creates a ProductionHandler with one Production that has 1 ResourceUnknown in input and 1 in output.
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(unknown);
        output.add(unknown);
        Production production = new Production(input, output);
        productionHandler.addProduction(production);

        // Then it selects the production
        productionHandler.selectProduction(1);

        // TEST
        productionHandler.chooseJollyInput(coin);

        assertTrue(productionHandler.getCurrentInput().size() == 1, "wrong output size");
        assertTrue(productionHandler.getCurrentInput().get(0) instanceof ResourceCoin, "wrong ResourceType in output");

        Exception ex = assertThrows(RuntimeException.class, () -> {
            productionHandler.chooseJollyInput(stone);
        });
    }

    @Test
    void chooseJollyOutput() {
        // Creates an instance of every resource type
        ResourceUnknown unknown = new ResourceUnknown();
        ResourceCoin coin = new ResourceCoin();
        ResourceServant servant = new ResourceServant();
        ResourceStone stone = new ResourceStone();
        ResourceFaith faith = new ResourceFaith();
        ResourceShield shield = new ResourceShield();

        // Then creates a ProductionHandler with one Production that has 1 ResourceUnknown in input and 1 in output.
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(unknown);
        output.add(unknown);
        Production production = new Production(input, output);
        productionHandler.addProduction(production);

        // Then it selects the production
        productionHandler.selectProduction(1);

        // TEST
        productionHandler.chooseJollyOutput(coin);

        assertEquals(1, productionHandler.getCurrentOutput().size(), "wrong output size");
        assertTrue(productionHandler.getCurrentOutput().get(0) instanceof ResourceCoin, "wrong ResourceType in output");

        Exception ex = assertThrows(RuntimeException.class, () -> {
            productionHandler.chooseJollyOutput(stone);
        });
    }

    @Test
    void removeSelectedProductionRunTimeException() {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        Exception ex = assertThrows(RuntimeException.class, () -> {
            productionHandler.removeProduction(production);
        });

        assertEquals(1, productionHandler.getProductions().size());
    }

    @Test
    void resetProductionChoice() {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
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

    @Test
    void resourcesAreEnough() throws UnknownResourceException, DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException, BlockedResourceException {
        // Creates a PlayerBoard
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);

        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        assertFalse(productionHandler.resourcesAreEnough(playerBoard));

        playerBoard.addResourceToStrongbox(ResourceType.COIN, 1);
        assertFalse(productionHandler.resourcesAreEnough(playerBoard));

        // Resources are divided into strongbox and depots and together they're enough to activated all the productions
        playerBoard.addResourceToStrongbox(ResourceType.SERVANT, 1);
        playerBoard.addResourceToWarehouse(ResourceType.SHIELD, 2);
        playerBoard.sendResourceToDepot(2, ResourceType.SHIELD, 2);
        assertTrue(productionHandler.resourcesAreEnough(playerBoard));
    }

    @Test
    void getDebt() {
        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        // TEST
        assertEquals(1, productionHandler.getDebt(new ResourceServant()));
        assertEquals(2, productionHandler.getDebt(new ResourceShield()));
        assertEquals(0, productionHandler.getDebt(new ResourceStone()));
        assertEquals(0, productionHandler.getDebt(new ResourceCoin()));
        assertEquals(0, productionHandler.getDebt(new ResourceUnknown()));
        assertEquals(0, productionHandler.getDebt(new ResourceFaith()));
    }

    @Test
    void takeResource() throws UnknownResourceException, ResourceNotPresentException {
        // Creates a PlayerBoard
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);

        // Creates a ProductionHandler with one Production that has 1 ResourceServant and 2 ResourceShield in input and 1 ResourceCoin in output.
        // Then it selects the production
        ProductionHandler productionHandler = new ProductionHandler();
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        input.add(new ResourceServant());
        input.add(new ResourceShield());
        input.add(new ResourceShield());
        output.add(new ResourceCoin());
        Production production = new Production(input, output);
        productionHandler.addProduction(production);
        productionHandler.selectProduction(1);

        playerBoard.addResourceToStrongbox(ResourceType.COIN, 1);
        playerBoard.addResourceToStrongbox(ResourceType.SERVANT, 1);
        playerBoard.addResourceToWarehouse(ResourceType.SHIELD, 2);

        productionHandler.takeResource(playerBoard, new ResourceServant(), 1);
        productionHandler.takeResource(playerBoard, new ResourceShield(), 2);
    }


}
