package model;

import model.resource.*;

public final class UtilsForModel {
    private UtilsForModel() {}

    /**
     * Useful method that returns a specific Resource from a given the ResourceType
     *
     * @param resourceType is the type of the Resource you need to use
     * @return the Resource that corresponds to the specified type
     */
    public static Resource typeToResource(ResourceType resourceType) {
        if(resourceType == ResourceType.COIN) {
            return new ResourceCoin();
        } else if(resourceType == ResourceType.SERVANT) {
            return new ResourceServant();
        } else if (resourceType == ResourceType.SHIELD) {
            return new ResourceShield();
        } else if(resourceType == ResourceType.STONE) {
            return new ResourceStone();
        } else if(resourceType == ResourceType.UNKNOWN) {
            return new ResourceUnknown();
        } else if (resourceType == ResourceType.FAITH) {
            return new ResourceFaith();
        }
        // This should never happen
        return null;
    }
}
