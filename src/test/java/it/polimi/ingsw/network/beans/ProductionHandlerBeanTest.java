package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.Exceptions.ProductionNotPresentException;
import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceCoin;
import it.polimi.ingsw.model.resource.ResourceServant;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.network.beans.ProductionHandlerBean;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.server.GameController;

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

        Gson gson = new Gson();
        String beanJson = gson.toJson(productionHandlerBean, ProductionHandlerBean.class);
        productionHandlerBean = gson.fromJson(beanJson, ProductionHandlerBean.class);

        assertEquals(1, productionHandlerBean.getInput().get(ResourceType.COIN));
        assertEquals(1, productionHandlerBean.getInput().get(ResourceType.SERVANT));
    }

    @Test
    void setProductionsFromPH() throws ProductionNotPresentException {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        ProductionHandlerBean productionHandlerBean = new ProductionHandlerBean(new GameController("Gigi", printWriter), "Gigi");
        game.getCurrentPlayer().getProductionHandler().addObserver(productionHandlerBean);

        assertEquals(game.getCurrentPlayer().getUsername(), productionHandlerBean.getUsername());

        List<Resource> list = new ArrayList<>();
        list.add(new ResourceServant());
        list.add(new ResourceCoin());
        game.getCurrentPlayer().getProductionHandler().getProductions().add(new Production(17, list, list));
        game.getCurrentPlayer().getProductionHandler().notifyObservers();

        assertEquals(2, productionHandlerBean.getProductions().length);
        assertEquals(0, productionHandlerBean.getProductions()[0]);
        assertEquals(17, productionHandlerBean.getProductions()[1]);
        assertEquals(false, productionHandlerBean.getActiveProductions()[0]);

        game.getCurrentPlayer().getProductionHandler().selectProductionByID(17);
        assertEquals(false, productionHandlerBean.getActiveProductions()[0]);
        assertEquals(true, productionHandlerBean.getActiveProductions()[1]);
    }
}