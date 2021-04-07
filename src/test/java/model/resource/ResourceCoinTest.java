package model.resource;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCoinTest {
    @Test
    void equals() {
        ResourceCoin coin = new ResourceCoin();
        ResourceCoin coin1 = new ResourceCoin();

        assertTrue(coin.equals(coin1));
    }

    @Test
    void removeList(){
        ArrayList<Resource> list = new ArrayList<>();
        list.add(new ResourceCoin());
        list.add(new ResourceFaith());
        list.remove(new ResourceCoin());

        assertTrue(list.get(0) instanceof ResourceFaith);
    }
}