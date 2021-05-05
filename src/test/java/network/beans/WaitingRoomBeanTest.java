package network.beans;

import model.Game;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        WaitingRoomBean waitingRoomBean = new WaitingRoomBean(game.getCurrentPlayer().getUsername());
        game.getCurrentPlayer().getWaitingRoom().addObserver(waitingRoomBean);

        assertEquals(game.getCurrentPlayer().getUsername(), waitingRoomBean.getUsername());

        for (ResourceType resourceType : ResourceType.values()) {
            if(resourceType != ResourceType.FAITH && resourceType != ResourceType.UNKNOWN && resourceType != ResourceType.WHITEORB)
                assertEquals(game.getCurrentPlayer().getStrongbox().getNumOfResource(resourceType), waitingRoomBean.getQuantity()[java.util.Arrays.asList(waitingRoomBean.getType()).indexOf(resourceType)]);
        }
    }
}