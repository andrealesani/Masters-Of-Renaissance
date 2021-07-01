package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTest {

    /**
     * Tests the equals method for productions
     */
    @Test
    public void equals() {
        List<Resource> input1 = new ArrayList<>();
        List<Resource> input2 = new ArrayList<>();
        List<Resource> output1 = new ArrayList<>();
        List<Resource> output2 = new ArrayList<>();

        input1.add(new ResourceCoin());
        input2.add(new ResourceServant());
        output1.add(new ResourceFaith());
        output2.add(new ResourceShield());

        assertEquals(new Production(-1, input1, output1), new Production(-1, input1, output1));
        assertNotEquals(new Production(-1, input1, output1), new Production(-1, input2, output2));
        assertNotEquals(new Production(-1, input1, output1), new Production(-1, input1, output2));
        assertNotEquals(new Production(-1, input1, output1), new Production(-1, input2, output1));
    }

    /**
     * Tests the constructor for the default production
     */
    @Test
    public void defaultProduction() {
        Production defaultProduction = new Production();

        assertEquals(new ResourceJolly(), defaultProduction.getInput().get(0));
        assertEquals(new ResourceJolly(), defaultProduction.getInput().get(1));
        assertEquals(new ResourceJolly(), defaultProduction.getOutput().get(0));

        assertEquals (2, defaultProduction.getInput().size());
        assertEquals (1, defaultProduction.getOutput().size());
        assertEquals (0, defaultProduction.getId());
    }

    /**
     * Tests selection and deselection of a production
     */
    @Test
    public void selectAndUnselect() {
        Production production = new Production(0, new ArrayList<>(), new ArrayList<>());

        assertFalse(production.isSelectedByHandler());

        production.select();

        assertTrue(production.isSelectedByHandler());

        production.unselect();

        assertFalse(production.isSelectedByHandler());
    }
}