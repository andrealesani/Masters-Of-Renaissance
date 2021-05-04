package network.beans;

import model.Market;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketBeanTest {
    @Test
    void test() {

        for (int i = 0; i < 100; i++) {
            Market market = new Market();
            MarketBean marketBean = new MarketBean();
            market.addObserver(marketBean);

            assertEquals(marketBean.getSlide(), market.getSlideOrb().getType());
            assertEquals(marketBean.getMarketBoard().length, market.getBoard().length);
            assertEquals(marketBean.getMarketBoard()[1][1], market.getBoard()[1][1].getType());
        }
    }
}