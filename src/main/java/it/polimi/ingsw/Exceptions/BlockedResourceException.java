package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This Exception is thrown when a resource is attempted to be added to a depot that can't accept it because a different depot is blocking that resource type.
 */
public class BlockedResourceException extends Exception {
    /**
     * The type of the blocked resource
     */
    private final ResourceType resource;

    /**
     * Constructor
     *
     * @param resource the resource that is blocked
     */
    public BlockedResourceException(ResourceType resource) {
        this.resource = resource;

    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Resource " + resource + " can't be added to depot because it is blocked by a different one.");
    }
}
