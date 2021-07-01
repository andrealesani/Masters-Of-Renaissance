package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomBeanTest {
    /**
     * Tests the updating of the bean
     */
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        WaitingRoomBean waitingRoomBean = new WaitingRoomBean(new GameController("Gigi", printWriter), game.getCurrentPlayer().getUsername());
        game.getCurrentPlayer().getWaitingRoom().addObserver(waitingRoomBean);

        assertEquals(game.getCurrentPlayer().getUsername(), waitingRoomBean.getUsername());

        for (ResourceType resourceType : ResourceType.values()) {
            if(resourceType != ResourceType.FAITH && resourceType != ResourceType.JOLLY && resourceType != ResourceType.WHITE_MARBLE)
                assertEquals(game.getCurrentPlayer().getStrongbox().getNumOfResource(resourceType), waitingRoomBean.getQuantity()[java.util.Arrays.asList(waitingRoomBean.getType()).indexOf(resourceType)]);
        }
    }
}