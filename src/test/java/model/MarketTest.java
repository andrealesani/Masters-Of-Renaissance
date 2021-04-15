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
                    System.out.print("Coin ");
                    countRS++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceFaith) && (countRF < 1)) {
                    System.out.print("Faith ");
                    countRF++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceServant) && (countRSe < 2)) {
                    System.out.print("Servant ");
                    countRSe++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceShield) && (countRSh < 2)) {
                    System.out.print("Shield ");
                    countRSh++;
                }

                else if ((market.getMarble(i, j) instanceof ResourceStone) && (countRSt < 2)){
                    System.out.print("Stone ");
                    countRSt++;
                }

                else if ((market.getMarble(i,j) instanceof ResourceWhite) && (countRW < 4)) {
                    System.out.print("WhiteMarble ");
                    countRW++;
                }
            }
            System.out.print("\n");
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

        if(market.getSlideOrb() instanceof ResourceCoin)
            System.out.print("SlideOrb: Coin\n");
        else if(market.getSlideOrb() instanceof ResourceFaith)
            System.out.print("SlideOrb: Faith\n");
        else if(market.getSlideOrb() instanceof ResourceServant)
            System.out.print("SlideOrb: Servant\n");
        else if(market.getSlideOrb() instanceof ResourceShield)
            System.out.print("SlideOrb: Shield\n");
        else if(market.getSlideOrb() instanceof ResourceStone)
            System.out.print("SlideOrb: Stone\n");
        else if(market.getSlideOrb() instanceof ResourceWhite)
            System.out.print("SlideOrb: WhiteMarble\n");

    }

    @Test
    public void selectResourcesRow() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException, BlockedResourceException {

        Market market = new Market();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);
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

    @Test
    public void selectResourcesColumn() throws DepotNotPresentException, WrongResourceInsertionException, NotEnoughSpaceException, NotEnoughResourceException, BlockedResourceException {

        Market market = new Market();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);
        int col = 0;
        int countNormal = 0, countFaith = 0;

        for (int i = 0; i < 3; i++) {
            market.getMarble(i, col).addResourceFromMarket(playerBoard);
            if ((market.getMarble(i, col) instanceof ResourceCoin)){
                countNormal++;
            }
            else if ((market.getMarble(i, col) instanceof ResourceFaith)){
                countFaith++;
            }
            else if ((market.getMarble(i, col) instanceof ResourceServant)){
                countNormal++;
            }
            else if ((market.getMarble(i, col) instanceof ResourceShield)){
                countNormal++;
            }
            else if ((market.getMarble(i, col) instanceof ResourceStone)){
                countNormal++;
            }
        }

        assertEquals(countNormal, playerBoard.leftInWaitingRoom());
        assertEquals(countFaith, playerBoard.getFaith());
    }

    @Test
    public void shiftResourcesRow(){
        Market market = new Market();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);

        Resource[][] actualBoard = market.getBoard();
        Resource oldSlideOrb = market.getSlideOrb();

        Resource [][] board = new Resource[actualBoard.length][];
        for(int i = 0; i < actualBoard.length; i++)
            board[i] = actualBoard[i].clone();

        /*
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {

                if ((market.getMarble(i, j) instanceof ResourceCoin))
                    System.out.print("Coin ");
                else if ((market.getMarble(i, j) instanceof ResourceFaith))
                    System.out.print("Faith ");
                else if ((market.getMarble(i, j) instanceof ResourceServant))
                    System.out.print("Servant ");
                else if ((market.getMarble(i, j) instanceof ResourceShield))
                    System.out.print("Shield ");
                else if ((market.getMarble(i, j) instanceof ResourceStone))
                    System.out.print("Stone ");
                else if ((market.getMarble(i,j) instanceof ResourceWhite))
                    System.out.print("WhiteMarble ");

            }
            System.out.print("\n");
        }

        if(market.getSlideOrb() instanceof ResourceCoin)
            System.out.print("SlideOrb: Coin\n");
        else if(market.getSlideOrb() instanceof ResourceFaith)
            System.out.print("SlideOrb: Faith\n");
        else if(market.getSlideOrb() instanceof ResourceServant)
            System.out.print("SlideOrb: Servant\n");
        else if(market.getSlideOrb() instanceof ResourceShield)
            System.out.print("SlideOrb: Shield\n");
        else if(market.getSlideOrb() instanceof ResourceStone)
            System.out.print("SlideOrb: Stone\n");
        else if(market.getSlideOrb() instanceof ResourceWhite)
            System.out.print("SlideOrb: WhiteMarble\n");


         */

        market.selectResources(MarketScope.ROW, 0, playerBoard);

        Resource[][] shifted = market.getBoard();
        Resource newSlideOrb = market.getSlideOrb();

        /*
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {

                if ((market.getMarble(i, j) instanceof ResourceCoin))
                    System.out.print("Coin ");
                else if ((market.getMarble(i, j) instanceof ResourceFaith))
                    System.out.print("Faith ");
                else if ((market.getMarble(i, j) instanceof ResourceServant))
                    System.out.print("Servant ");
                else if ((market.getMarble(i, j) instanceof ResourceShield))
                    System.out.print("Shield ");
                else if ((market.getMarble(i, j) instanceof ResourceStone))
                    System.out.print("Stone ");
                else if ((market.getMarble(i,j) instanceof ResourceWhite))
                    System.out.print("WhiteMarble ");

            }
            System.out.print("\n");
        }

        if(market.getSlideOrb() instanceof ResourceCoin)
            System.out.print("SlideOrb: Coin\n");
        else if(market.getSlideOrb() instanceof ResourceFaith)
            System.out.print("SlideOrb: Faith\n");
        else if(market.getSlideOrb() instanceof ResourceServant)
            System.out.print("SlideOrb: Servant\n");
        else if(market.getSlideOrb() instanceof ResourceShield)
            System.out.print("SlideOrb: Shield\n");
        else if(market.getSlideOrb() instanceof ResourceStone)
            System.out.print("SlideOrb: Stone\n");
        else if(market.getSlideOrb() instanceof ResourceWhite)
            System.out.print("SlideOrb: WhiteMarble\n");

         */

        assertEquals(oldSlideOrb, shifted[0][3]);
        assertEquals(board[0][3], shifted[0][2]);
        assertEquals(board[0][2], shifted[0][1]);
        assertEquals(board[0][1], shifted[0][0]);
        assertEquals(board[0][0], newSlideOrb);

    }

    @Test
    public void shiftResourcesColumn(){
        Market market = new Market();
        PlayerBoard playerBoard = new PlayerBoard(null, null, 3, 100, 100, null, null, null, null);

        Resource[][] actualBoard = market.getBoard();
        Resource oldSlideOrb = market.getSlideOrb();

        Resource [][] board = new Resource[actualBoard.length][];
        for(int i = 0; i < actualBoard.length; i++)
            board[i] = actualBoard[i].clone();

        market.selectResources(MarketScope.COLUMN, 0, playerBoard);
        Resource[][] shifted = market.getBoard();
        Resource newSlideOrb = market.getSlideOrb();

        assertEquals(oldSlideOrb, shifted[2][0]);
        assertEquals(board[2][0], shifted[1][0]);
        assertEquals(board[1][0], shifted[0][0]);
        assertEquals(board[0][0], newSlideOrb);

    }

}