package model.storage;

import model.resource.Resource;

/**
 * This interface indicates that the implementing class can be used as a depot in the player board warehouse.
 */
public interface ResourceDepot {
    boolean canHold (Resource resource, int quantity);
    boolean isBlocking (Resource resource);
    void empty();
}
