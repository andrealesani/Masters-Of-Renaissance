package model;

import model.resource.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTest {

    @Test
    void equals() {
        List<Resource> input1 = new ArrayList<>();
        List<Resource> input2 = new ArrayList<>();
        List<Resource> output1 = new ArrayList<>();
        List<Resource> output2 = new ArrayList<>();

        input1.add(new ResourceCoin());
        input2.add(new ResourceServant());
        output1.add(new ResourceFaith());
        output2.add(new ResourceShield());

        assertTrue(new Production(input1, output1).equals(new Production(input1, output1)));
        assertFalse(new Production(input1, output1).equals(new Production(input2, output2)));
        assertFalse(new Production(input1, output1).equals(new Production(input1, output2)));
        assertFalse(new Production(input1, output1).equals(new Production(input2, output1)));
    }
}