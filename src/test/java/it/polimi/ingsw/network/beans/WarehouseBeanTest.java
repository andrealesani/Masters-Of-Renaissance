package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.BlockedResourceException;
import it.polimi.ingsw.Exceptions.DepotNotPresentException;
import it.polimi.ingsw.Exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.Exceptions.WrongResourceInsertionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.network.beans.WarehouseBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WarehouseBeanTest {
    /**
     * Tests the updating of the bean
     */
    @Test
    void test() throws DepotNotPresentException, NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        WarehouseBean warehouseBean = new WarehouseBean(new GameController("Gigi", printWriter), game.getCurrentPlayer().getUsername(), 3);
        game.getCurrentPlayer().getWarehouse().addObserver(warehouseBean);

        game.getCurrentPlayer().getWarehouse().addToDepot(1, ResourceType.STONE, 1);

        assertEquals(game.getCurrentPlayer().getUsername(), warehouseBean.getUsername());

        for (int i = 0; i < warehouseBean.getBasicDepotNum(); i++) {
            assertEquals(game.getCurrentPlayer().getWarehouse().getDepot(i+1).getAcceptedResource(), warehouseBean.getDepotType()[i]);
            assertEquals(game.getCurrentPlayer().getWarehouse().getDepot(i+1).getStoredResources().size(), warehouseBean.getDepotQuantity()[i]);
        }
    }
}