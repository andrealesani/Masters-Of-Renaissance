package Exceptions;

import model.resource.ResourceType;

/**
 * This Exception is thrown when a white orb is asked to be converted into a resource for which a conversion is not available
 */
public class ConversionNotAvailableException extends Exception{
    private final ResourceType conversion;

    /**
     * Constructor
     *
     * @param conversion the resource to which the white marble was attempted to be converted
     */
    public ConversionNotAvailableException(ResourceType conversion) {
        this.conversion = conversion;
    }

    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Conversion of white marbles to resource " + conversion + " is not available.");
    }
}
