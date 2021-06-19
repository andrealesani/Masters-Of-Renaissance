package model;

import Exceptions.ParametersNotValidException;
import model.resource.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the game's market
 */
//TODO rendere dimensioni matrice parametrici?
public class Market implements Observable {
    /**
     * This matrix stores the market's marbles
     */
    private final Resource[][] board = new Resource[3][4];
    /**
     * This attribute stores the marble in the market's slide
     */
    private Resource slideMarble;

    //CONSTRUCTORS

    /**
     * Constructor
     */
    //TODO togliere hardcoding?
    public Market() {
        int rowNum = 3, colNum = 4;
        int numCoin = 2, numStone = 2, numShield = 2, numServant = 2, numFaith = 1, numWhite = 4;

        Resource coin = new ResourceCoin(), stone = new ResourceStone(), shield = new ResourceShield(), servant = new ResourceServant();
        Resource faith = new ResourceFaith(), whiteMarble = new ResourceWhite();
        List<Resource> resources = new ArrayList<>();

        for (int i = 0; i<numCoin; i++) {
            resources.add(coin);
        }
        for (int i = 0; i<numStone; i++) {
            resources.add(stone);
        }
        for (int i = 0; i<numShield; i++) {
            resources.add(shield);
        }
        for (int i = 0; i<numServant; i++) {
            resources.add(servant);
        }
        for (int i = 0; i<numFaith; i++) {
            resources.add(faith);
        }
        for (int i = 0; i<numWhite; i++) {
            resources.add(whiteMarble);
        }

        Collections.shuffle(resources);

        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                board[row][col] = resources.remove(0);
            }
        }
        slideMarble = resources.remove(0);

        notifyObservers();

    }

    //PUBLIC METHODS

    /**
     * Activates addResourceFromMarket methods on all resources in the selected row
     *
     * @param numScope    the number of the selected row
     * @param playerBoard the player's board
     */
    public void selectRow(int numScope, PlayerBoard playerBoard) {
        if ((numScope <= 0) || (numScope > 3) || (playerBoard == null))
            throw new ParametersNotValidException();

        int riga;

        riga = numScope - 1;
        for (int j = 0; j < 4; j++)
            board[riga][j].addResourceFromMarket(playerBoard);
        shiftRow(numScope);

        notifyObservers();
    }

    /**
     * Activates addResourceFromMarket methods on all resources in the selected column
     *
     * @param numScope    the number of the selected column
     * @param playerBoard the player's board
     */
    public void selectColumn(int numScope, PlayerBoard playerBoard) {
        if ((numScope <= 0) || (numScope > 4) || (playerBoard == null))
            throw new ParametersNotValidException();

        int col;

        col = numScope - 1;
        for (int i = 0; i < 3; i++)
            board[i][col].addResourceFromMarket(playerBoard);
        shiftColumn(numScope);

        notifyObservers();
    }

    public void restoreBoard(ResourceType[][] board) {
        for (int i = 0; i < this.board.length; i++)
            for (int j = 0; j < this.board[0].length; j++)
                this.board[i][j] = board[i][j].toResource();
    }

    public void restoreSlideMarble(ResourceType slideMarble) {
        this.slideMarble = slideMarble.toResource();
    }

    //PRIVATE METHODS

    /**
     * Shifts the given row based on the market's rules, substituting the marble on the slide
     *
     * @param numScope the number of the selected row
     */
    private void shiftRow(int numScope) {
        if ((numScope <= 0) || (numScope > 3))
            throw new ParametersNotValidException();

        int riga;
        Resource temp;


        riga = numScope - 1;
        int j = 0;
        temp = board[riga][j];
        for (j = 0; j < 3; j++)
            board[riga][j] = board[riga][j + 1];


        board[riga][j] = slideMarble;
        slideMarble = temp;
    }

    /**
     * Shifts the given column based on the market's rules, substituting the marble on the slide
     *
     * @param numScope the number of the selected column
     */
    private void shiftColumn(int numScope) {
        if ((numScope <= 0) || (numScope > 4))
            throw new ParametersNotValidException();

        int col;
        Resource temp;


        col = numScope - 1;
        int i = 0;
        temp = board[i][col];
        for (i = 0; i < 2; i++)
            board[i][col] = board[i + 1][col];

        board[i][col] = slideMarble;
        slideMarble = temp;
    }

    //GETTERS

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
     * @param row    the row number
     * @param column the column number
     * @return the marble in the given spot
     */
    public Resource getMarble(int row, int column) {
        if ((row < 0) || (column < 0) || (row >= 3) || (column >= 4))
            throw new ParametersNotValidException();

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


    // OBSERVABLE ATTRIBUTES AND METHODS

    /**
     * List of observers that need to get updated when the object state changes
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * This method calls the update() on every object observing this object
     */
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        notifyObservers();
    }

    public List<Observer> getObservers() {
        return observers;
    }
}
