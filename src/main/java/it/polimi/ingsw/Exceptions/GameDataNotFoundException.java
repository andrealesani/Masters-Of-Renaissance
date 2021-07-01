package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when a non-existent save game file is attempted to be accessed
 */
public class GameDataNotFoundException extends Exception {
    /**
     * The game's file id
     */
    private final int id;

    /**
     * Constructor
     *
     * @param id the id of the game's file
     */
    public GameDataNotFoundException(int id) {
        this.id = id;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Couldn't find the save game file with id " + id + ".");
    }
}
