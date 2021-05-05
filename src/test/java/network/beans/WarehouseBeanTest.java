package network.beans;

import model.Game;
import org.junit.jupiter.api.Test;

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

        WarehouseBean warehouseBean = new WarehouseBean(game.getCurrentPlayer().getUsername(), 3);
        game.getCurrentPlayer().getWarehouse().addObserver(warehouseBean);

        // TODO
    }
}