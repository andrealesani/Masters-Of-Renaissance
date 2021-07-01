package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This Exception is thrown when a resource is tried to be inserted in a storage that can't contain its type.
 */
public class WrongResourceInsertionException extends Exception {
    /**
     * The type of the resource
     */
    private final ResourceType resource;

    /**
     * Constructor
     *
     * @param resource the resource which cannot be inserted
     */
    public WrongResourceInsertionException(ResourceType resource) {
        this.resource = resource;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Resources of type " + resource + " cannot be inserted into this storage.");
    }
}

