package network.beans;

import model.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        GameBean gameBean = new GameBean();
        game.addObserver(gameBean);

        assertEquals(game.getCurrentPlayer().getUsername(), gameBean.getCurrentPlayer());
        assertEquals(game.getTurnPhase(), gameBean.getTurnPhase());
    }
}