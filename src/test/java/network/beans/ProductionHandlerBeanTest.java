package network.beans;

import model.Game;
import model.resource.Resource;
import model.resource.ResourceCoin;
import model.resource.ResourceServant;
import model.resource.ResourceType;
import org.junit.jupiter.api.Test;
import server.GameController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductionHandlerBeanTest {

    @Test
    void setInputFromPH() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        ProductionHandlerBean productionHandlerBean = new ProductionHandlerBean(new GameController("Gigi", printWriter), "Gigi");
        game.getCurrentPlayer().getProductionHandler().addObserver(productionHandlerBean);

        assertEquals(game.getCurrentPlayer().getUsername(), productionHandlerBean.getUsername());

        game.getCurrentPlayer().getProductionHandler().getCurrentInput().add(new ResourceCoin());
        game.getCurrentPlayer().getProductionHandler().notifyObservers();
        game.getCurrentPlayer().getProductionHandler().getCurrentInput().add(new ResourceServant());
        game.getCurrentPlayer().getProductionHandler().notifyObservers();

        assertEquals(1, productionHandlerBean.getInput().get(ResourceType.COIN));
        assertEquals(1, productionHandlerBean.getInput().get(ResourceType.SERVANT));
    }
}