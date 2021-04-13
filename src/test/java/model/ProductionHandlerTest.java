package model;

import Exceptions.NotEnoughSpaceException;
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
        productionHandler.selectProduction(0);

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
        productionHandler.selectProduction(0);

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
        productionHandler.selectProduction(0);

        // TEST
        productionHandler.chooseJollyInput(coin);

        assertTrue(productionHandler.getCurrentInput().size() == 1, "wrong size");
        assertTrue(productionHandler.getCurrentInput().get(0) instanceof ResourceCoin, "wrong ResourceType");
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
        productionHandler.selectProduction(0);

        // TEST
        productionHandler.chooseJollyOutput(coin);

        assertTrue(productionHandler.getCurrentOutput().size() == 1, "wrong size");
        assertTrue(productionHandler.getCurrentOutput().get(0) instanceof ResourceCoin, "wrong ResourceType");
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
        productionHandler.selectProduction(0);

        Exception ex = assertThrows(RuntimeException.class, () -> {
            productionHandler.removeProduction(production);
        });

        assertEquals(1, productionHandler.getProductions().size());
    }

}
