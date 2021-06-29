package it.polimi.ingsw.model;

/**
 * This enum contains all possible states for players' pope's favor tiles
 */
public enum PopeTileState {
    /**
     * The inactive state
     */
    INACTIVE {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "INACTIVE" + Color.RESET;
        }
    },
    /**
     * The activated state
     */
    ACTIVE {
        @Override
        public String toString() {
            return Color.GREEN_FG + "ACTIVE" + Color.RESET;
        }
    },
    /**
     * The discarded state
     */
    DISCARDED {
        @Override
        public String toString() {
            return Color.RED_LIGHT_FG + "DISCARDED" + Color.RESET;
        }
    },
}
