package model;

import model.resource.*;

/**
 * This class represents the game's market
 */
//TODO rendere dimensioni matrice parametrici?
public class Market {
    /**
     * This matrix stores the market's marbles
     */
    private Resource board[][] = new Resource[3][4];
    /**
     * This attribute stores the marble in the market's slide
     */
    private Resource slideMarble;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    //TODO togliere else/ifs?
    public Market() {
        int totalOrbs = 13;
        int orb;
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
                    slideMarble = new ResourceCoin();
                else if(countRF < 1)
                    slideMarble = new ResourceFaith();
                else if(countRSe < 2)
                    slideMarble = new ResourceServant();
                else if(countRSh < 2)
                    slideMarble = new ResourceShield();
                else if(countRSt < 2)
                    slideMarble = new ResourceStone();
                else if(countRW < 4)
                    slideMarble = new ResourceWhite();
                }
            }
        }

    }

    //PUBLIC METHODS

    /**
     * Activates addResourceFromMarket methods on all resources in the selected row or column
     *
     * @param marketScope either row or column
     * @param numScope the number of the selected row or column
     * @param playerBoard the player's board
     */
    //TODO dividere in 2 metodi diversi per riga e colonna?
    public void selectResources(MarketScope marketScope, int numScope, PlayerBoard playerBoard) {
        int riga, col;

        if(marketScope == MarketScope.ROW) {
            riga = numScope;
            for (int j = 0; j < 4; j++)
                board[riga][j].addResourceFromMarket(playerBoard);
            shiftResources(MarketScope.ROW, riga);
        }

        else if(marketScope == MarketScope.COLUMN) {
            col = numScope;
            for (int i = 0; i < 3; i++)
                board[i][col].addResourceFromMarket(playerBoard);
            shiftResources(MarketScope.COLUMN, col);
        }


    }

    //PRIVATE METHODS

    /**
     * Shifts the given row or column based on the market's rules, substituting the marble on the slide
     *
     * @param marketScope either row or column
     * @param numScope the number of the selected row or column
     */
    private void shiftResources(MarketScope marketScope, int numScope) {
        int riga, col;
        Resource temp;

        if(marketScope == MarketScope.ROW) {
            riga = numScope;
            int j = 0;
            temp = board[riga][j];
            for(j = 0; j < 3; j++)
                board[riga][j] = board[riga][j+1];

            board[riga][j] = slideMarble;
            slideMarble = temp;
        }

        else if(marketScope == MarketScope.COLUMN) {
            col = numScope;
            int i = 0;
            temp = board[i][col];
            for(i = 0; i < 2; i++)
                board[i][col] = board[i+1][col];

            board[i][col] = slideMarble;
            slideMarble = temp;
        }

    }

    //GETTERS

    //TODO return copie, non l'oggetto stesso gigi

    /**
     * Getter
     *
     * @return the marble matrix
     */
    public Resource[][] getBoard() {
        return board;
    }

    /**
     * Getter
     *
     * @param row the row number
     * @param column the column number
     * @return the marble in the given spot
     */
    public Resource getMarble(int row, int column) {
        return board[row][column];
    }

    /**
     * Getter
     *
     * @return the marble on the market's slide
     */
    public Resource getSlideOrb() {
        return slideMarble;
    }
}
