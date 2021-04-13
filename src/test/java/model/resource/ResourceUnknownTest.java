package model.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceUnknownTest {

    @Test
    void equals() {
        assertTrue(new ResourceUnknown().equals(new ResourceUnknown()));
        assertFalse(new ResourceUnknown().equals(new ResourceCoin()));
    }

}