package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This Exception is thrown when an operation is attempted using a resource that is not present in a sufficient quantity for the required operation.
 */
public class NotEnoughResourceException extends Exception {
    /**
     * The type of the resource
     */
    private final ResourceType resource;

    /**
     * Constructor
     *
     * @param resource the resource which is not present in sufficient quantity
     */
    public NotEnoughResourceException(ResourceType resource) {
        this.resource = resource;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Resource " + resource + " is not present in sufficient quantity for the selected action.");
    }
}