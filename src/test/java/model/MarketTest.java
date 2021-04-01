package model;

import model.resource.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {
    @Test
    public void testConstructor(){
        int countRS = 0, countRF = 0, countRSe = 0, countRSh = 0, countRSt = 0, countRW = 0;

        Market market = new Market();
        assertTrue((market.getMarble(0,0) instanceof ResourceCoin)
                || (market.getMarble(0,0) instanceof ResourceFaith)
                || (market.getMarble(0,0) instanceof ResourceServant)
                || (market.getMarble(0,0) instanceof ResourceShield)
                || (market.getMarble(0,0) instanceof ResourceStone)
                || (market.getMarble(0,0) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(0,1) instanceof ResourceCoin)
                || (market.getMarble(0,1) instanceof ResourceFaith)
                || (market.getMarble(0,1) instanceof ResourceServant)
                || (market.getMarble(0,1) instanceof ResourceShield)
                || (market.getMarble(0,1) instanceof ResourceStone)
                || (market.getMarble(0,1) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(0,2) instanceof ResourceCoin)
                || (market.getMarble(0,2) instanceof ResourceFaith)
                || (market.getMarble(0,2) instanceof ResourceServant)
                || (market.getMarble(0,2) instanceof ResourceShield)
                || (market.getMarble(0,2) instanceof ResourceStone)
                || (market.getMarble(0,2) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(0,3) instanceof ResourceCoin)
                || (market.getMarble(0,3) instanceof ResourceFaith)
                || (market.getMarble(0,3) instanceof ResourceServant)
                || (market.getMarble(0,3) instanceof ResourceShield)
                || (market.getMarble(0,3) instanceof ResourceStone)
                || (market.getMarble(0,3) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(1,0) instanceof ResourceCoin)
                || (market.getMarble(1,0) instanceof ResourceFaith)
                || (market.getMarble(1,0) instanceof ResourceServant)
                || (market.getMarble(1,0) instanceof ResourceShield)
                || (market.getMarble(1,0) instanceof ResourceStone)
                || (market.getMarble(1,0) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(1,1) instanceof ResourceCoin)
                || (market.getMarble(1,1) instanceof ResourceFaith)
                || (market.getMarble(1,1) instanceof ResourceServant)
                || (market.getMarble(1,1) instanceof ResourceShield)
                || (market.getMarble(1,1) instanceof ResourceStone)
                || (market.getMarble(1,1) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(1,2) instanceof ResourceCoin)
                || (market.getMarble(1,2) instanceof ResourceFaith)
                || (market.getMarble(1,2) instanceof ResourceServant)
                || (market.getMarble(1,2) instanceof ResourceShield)
                || (market.getMarble(1,2) instanceof ResourceStone)
                || (market.getMarble(1,2) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(1,3) instanceof ResourceCoin)
                || (market.getMarble(1,3) instanceof ResourceFaith)
                || (market.getMarble(1,3) instanceof ResourceServant)
                || (market.getMarble(1,3) instanceof ResourceShield)
                || (market.getMarble(1,3) instanceof ResourceStone)
                || (market.getMarble(1,3) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(2,0) instanceof ResourceCoin)
                || (market.getMarble(2,0) instanceof ResourceFaith)
                || (market.getMarble(2,0) instanceof ResourceServant)
                || (market.getMarble(2,0) instanceof ResourceShield)
                || (market.getMarble(2,0) instanceof ResourceStone)
                || (market.getMarble(2,0) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(2,1) instanceof ResourceCoin)
                || (market.getMarble(2,1) instanceof ResourceFaith)
                || (market.getMarble(2,1) instanceof ResourceServant)
                || (market.getMarble(2,1) instanceof ResourceShield)
                || (market.getMarble(2,1) instanceof ResourceStone)
                || (market.getMarble(2,1) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(2,2) instanceof ResourceCoin)
                || (market.getMarble(2,2) instanceof ResourceFaith)
                || (market.getMarble(2,2) instanceof ResourceServant)
                || (market.getMarble(2,2) instanceof ResourceShield)
                || (market.getMarble(2,2) instanceof ResourceStone)
                || (market.getMarble(2,2) instanceof ResourceWhite)
        ) ;

        assertTrue((market.getMarble(2,3) instanceof ResourceCoin)
                || (market.getMarble(2,3) instanceof ResourceFaith)
                || (market.getMarble(2,3) instanceof ResourceServant)
                || (market.getMarble(2,3) instanceof ResourceShield)
                || (market.getMarble(2,3) instanceof ResourceStone)
                || (market.getMarble(2,3) instanceof ResourceWhite)
        ) ;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {

                if ((market.getMarble(i,j) instanceof ResourceCoin) && (countRS < 2))
                    countRS++;

                else if ((market.getMarble(i,j) instanceof ResourceFaith) && (countRF < 1))
                    countRF++;

                else if ((market.getMarble(i,j) instanceof ResourceServant) && (countRSe < 2))
                    countRSe++;

                else if ((market.getMarble(i,j) instanceof ResourceShield) && (countRSh < 2))
                    countRSh++;

                else if ((market.getMarble(i,j) instanceof ResourceStone) && (countRSt < 2))
                    countRSt++;

                else if ((market.getMarble(i,j) instanceof ResourceWhite) && (countRW < 4))
                    countRW++;

            }
        }

        assertEquals(countRS, 2);
        assertEquals(countRF, 1);
        assertEquals(countRSe, 2);
        assertEquals(countRSh, 2);
        assertEquals(countRSt, 2);
        assertEquals(countRW, 4);

    }

}