package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.beans.GameBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        GameBean gameBean = new GameBean(new GameController("Gigi", printWriter));
        game.addObserver(gameBean);

        assertEquals(game.getCurrentPlayer().getUsername(), gameBean.getCurrentPlayer());
        assertEquals(game.getTurnPhase(), gameBean.getTurnPhase());
    }
}