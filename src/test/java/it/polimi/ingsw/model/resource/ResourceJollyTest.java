package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.resource.ResourceCoin;
import it.polimi.ingsw.model.resource.ResourceJolly;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceJollyTest {

    @Test
    void equals() {
        assertTrue(new ResourceJolly().equals(new ResourceJolly()));
        assertFalse(new ResourceJolly().equals(new ResourceCoin()));
    }

}