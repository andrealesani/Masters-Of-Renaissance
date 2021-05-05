package model.lorenzo.tokens;

import exceptions.ParametersNotValidException;
import model.lorenzo.Lorenzo;
import network.beans.LorenzoTokenType;

/**
 * Represents the token that increases Lorenzo's faith by 2 points
 */
public class DoubleFaithToken extends ActionToken {
    /**
     * This attribute stores the token's owning a.i.
     */
    private final Lorenzo lorenzo;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param lorenzo reference to Lorenzo instance
     */
    public DoubleFaithToken(Lorenzo lorenzo) {
        super(LorenzoTokenType.DoubleFaith);

        if (lorenzo == null) {
            throw new ParametersNotValidException();
        }
        this.lorenzo = lorenzo;
    }

    //PUBLIC METHODS

    /**
     * Standard method in the interface that calls the class-specific method doubleIncrease()
     */
    @Override
    public void doAction() {
        doubleIncrease();
    }

    //PRIVATE METHODS

    /**
     * Increases Lorenzo's faith by 2 points
     */
    private void doubleIncrease() {
        lorenzo.addFaith(2);
    }
}
