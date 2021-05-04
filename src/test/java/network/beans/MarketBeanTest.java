package network.beans;

import model.Market;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketBeanTest {
    @Test
    void test() {

        Market market = new Market();
        MarketBean marketBean = new MarketBean();
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