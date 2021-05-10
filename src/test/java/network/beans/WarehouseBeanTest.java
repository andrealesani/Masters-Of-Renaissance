package network.beans;

import model.Game;
import network.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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