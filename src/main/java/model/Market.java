package model;

import model.resource.*;

public class Market {
    private Resource board[][] = new Resource[3][4];
    private Resource slideOrb;

    public Market() {
        int totalOrbs = 13;
        int orb = 0;
        int countRS = 0, countRF = 0, countRSe = 0, countRSh = 0, countRSt = 0, countRW = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                orb = (int) (Math.random() * 6);
                if ((orb == 0) && (countRS < 2)) {
                    board[i][j] = new ResourceCoin();
                    countRS++;
                }
                else if ((orb == 1) && (countRF < 1)) {
                    board[i][j] = new ResourceFaith();
                    countRF++;
                }
                else if ((orb == 2) && (countRSe < 2)) {
                    board[i][j] = new ResourceServant();
                    countRSe++;
                }
                else if ((orb == 3) && (countRSh < 2)) {
                    board[i][j] = new ResourceShield();
                    countRSh++;
                }
                else if ((orb == 4) && (countRSt < 2)) {
                    board[i][j] = new ResourceStone();
                    countRSt++;
                }
                else if ((orb == 5) && (countRW < 4)) {
                    board[i][j] = new ResourceWhite();
                    countRW++;
                }
                else {
                    j--;
                }

            totalOrbs--;

            if(totalOrbs == 1) {
                if(countRS < 2)
                    slideOrb = new ResourceCoin();
                else if(countRF < 1)
                    slideOrb = new ResourceFaith();
                else if(countRSe < 2)
                    slideOrb = new ResourceServant();
                else if(countRSh < 2)
                    slideOrb = new ResourceShield();
                else if(countRSt < 2)
                    slideOrb = new ResourceStone();
                else if(countRW < 4)
                    slideOrb = new ResourceWhite();
                }
            }
        }

    }

    public Resource[][] getBoard() {
        return board;
    }

    public Resource getMarble(int row, int column) {
        return board[row][column];
    }

    public Resource getSlideOrb() {
        return slideOrb;
    }

    public void selectedResources(MarketScope marketScope, int numScope, PlayerBoard playerBoard) {
        //TODO
    }


    private void shiftResources(MarketScope marketScope, int numScope) {
        //TODO
    }

}
