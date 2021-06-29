package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.network.beans.MarketBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class MarketBeanTest {
    @Test
    void test() {

        Market market = new Market();

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        MarketBean marketBean = new MarketBean(new GameController("Gigi", printWriter));
        market.addObserver(marketBean);

        assertEquals(marketBean.getSlide(), market.getSlideOrb().getType());
        assertEquals(marketBean.getMarketBoard().length, market.getBoard().length);

        for (int i = 0; i < market.getBoard().length; i++) {
            for (int j = 0; j < market.getBoard()[0].length; j++) {
                assertEquals(marketBean.getMarketBoard()[i][j], market.getBoard()[i][j].getType());
            }
        }
    }
}