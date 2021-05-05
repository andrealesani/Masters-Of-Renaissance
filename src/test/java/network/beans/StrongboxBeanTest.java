package network.beans;

import model.Game;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StrongboxBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        StrongboxBean strongboxBean = new StrongboxBean(game.getCurrentPlayer().getUsername());
        game.getCurrentPlayer().getStrongbox().addObserver(strongboxBean);

        assertEquals(game.getCurrentPlayer().getUsername(), strongboxBean.getUsername());

        for (ResourceType resourceType : ResourceType.values()) {
            if(resourceType != ResourceType.FAITH && resourceType != ResourceType.UNKNOWN && resourceType != ResourceType.WHITEORB)
                assertEquals(game.getCurrentPlayer().getStrongbox().getNumOfResource(resourceType), strongboxBean.getQuantity()[java.util.Arrays.asList(strongboxBean.getType()).indexOf(resourceType)]);
        }
    }
}