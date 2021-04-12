package model;

import Exceptions.*;
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

                if ((market.getMarble(i, j) instanceof ResourceCoin) && (countRS < 2)) {
                    System.out.print("Marble type: Coin\n");
                    countRS++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceFaith) && (countRF < 1)) {
                    System.out.print("Marble type: Faith\n");
                    countRF++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceServant) && (countRSe < 2)) {
                    System.out.print("Marble type: Servant\n");
                    countRSe++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceShield) && (countRSh < 2)) {
                    System.out.print("Marble type: Shield\n");
                    countRSh++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceStone) && (countRSt < 2)){
                    System.out.print("Marble type: Stone\n");
                    countRSt++;
                }

                else if ((market.getMarble(i,j) instanceof ResourceWhite) && (countRW < 4)) {
                    System.out.print("Marble type: WhiteMarble\n");
                    countRW++;
                }
            }
        }

        assertTrue(countRS <= 2);
        assertTrue(countRF <= 1);
        assertTrue(countRSe <= 2);
        assertTrue(countRSh <= 3);
        assertTrue(countRSt <= 2);
        assertTrue(countRW <= 4);


        assertTrue((market.getSlideOrb() instanceof ResourceCoin)
                || (market.getSlideOrb() instanceof ResourceFaith)
                || (market.getSlideOrb() instanceof ResourceServant)
                || (market.getSlideOrb() instanceof ResourceShield)
                || (market.getSlideOrb() instanceof ResourceStone)
                || (market.getSlideOrb() instanceof ResourceWhite)
        ) ;

    }

    @Test
    public void selectResources() throws DepotNotPresentException, WrongResourceTypeException, NotEnoughSpaceException, NotEnoughResourceException, BlockedResourceException {

        Market market = new Market();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, null);
        int riga = 0;
        int countNormal = 0, countFaith = 0;

        for (int j = 0; j < 4; j++) {
            market.getMarble(riga, j).addResourceFromMarket(playerBoard);
            if ((market.getMarble(riga, j) instanceof ResourceCoin)){
                countNormal++;
            }
            else if ((market.getMarble(riga, j) instanceof ResourceFaith)){
                countFaith++;
            }
            else if ((market.getMarble(riga, j) instanceof ResourceServant)){
                countNormal++;
            }
            else if ((market.getMarble(riga, j) instanceof ResourceShield)){
                countNormal++;
            }
            else if ((market.getMarble(riga, j) instanceof ResourceStone)){
                countNormal++;
            }
        }

        assertEquals(countNormal, playerBoard.leftInWaitingRoom());
        assertEquals(countFaith, playerBoard.getFaith());
    }


}