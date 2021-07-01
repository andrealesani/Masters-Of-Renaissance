package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.resource.ResourceCoin;
import it.polimi.ingsw.model.resource.ResourceJolly;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceJollyTest {
    /**
     * Tests the equals method for a specific resource
     */
    @Test
    void equals() {
        assertTrue(new ResourceJolly().equals(new ResourceJolly()));
        assertFalse(new ResourceJolly().equals(new ResourceCoin()));
    }

}