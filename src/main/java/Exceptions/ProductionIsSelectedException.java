package Exceptions;

public class ProductionIsSelectedException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Can't remove a production when it's selected, please use resetProductionChoice() before calling this method");
    }
}
