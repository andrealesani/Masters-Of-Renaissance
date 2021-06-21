package model.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceJollyTest {

    @Test
    void equals() {
        assertTrue(new ResourceJolly().equals(new ResourceJolly()));
        assertFalse(new ResourceJolly().equals(new ResourceCoin()));
    }

}