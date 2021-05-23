package network.beans;

import model.Game;
import server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

class WarehouseBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        WarehouseBean warehouseBean = new WarehouseBean(new GameController("Gigi", printWriter), game.getCurrentPlayer().getUsername(), 3);
        game.getCurrentPlayer().getWarehouse().addObserver(warehouseBean);

        // TODO
    }
}