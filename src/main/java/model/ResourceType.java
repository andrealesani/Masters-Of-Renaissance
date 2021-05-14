package model;

import model.resource.*;

/**
 * This enum contains all possible types for the game's resources and marbles
 */
public enum ResourceType {
    SHIELD {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "SHIELD" + Color.DEFAULT;
        }
    },
    STONE {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "STONE" + Color.DEFAULT;
        }
    },
    SERVANT {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "SERVANT" + Color.DEFAULT;
        }
    },
    COIN {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "COIN" + Color.DEFAULT;
        }
    },
    FAITH {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "FAITH" + Color.DEFAULT;
        }
    },
    WHITEORB {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "WHITEORB" + Color.DEFAULT;
        }
    },
    UNKNOWN {
        @Override
        public String toString() {
            return Color.ORANGE_FG + "UNKNOWN" + Color.DEFAULT;
        }
    };

    public Resource toResource() {
        if (this == ResourceType.COIN) {
            return new ResourceCoin();
        }
        if (this == ResourceType.SERVANT) {
            return new ResourceServant();
        }
        if (this == ResourceType.SHIELD) {
            return new ResourceShield();
        }
        if (this == ResourceType.STONE) {
            return new ResourceStone();
        }
        if (this == ResourceType.UNKNOWN) {
            return new ResourceUnknown();
        }
        if (this == ResourceType.FAITH) {
            return new ResourceFaith();
        }
        // This should never happen
        return null;
    }

    public String formattedString() {
        if (this == ResourceType.COIN) {
            return Color.AQUA_GREEN_BG + "   COIN   " + Color.DEFAULT;
        }
        if (this == ResourceType.SERVANT) {
            return Color.AQUA_GREEN_BG + " SERVANT  " + Color.DEFAULT;
        }
        if (this == ResourceType.SHIELD) {
            return Color.AQUA_GREEN_BG + "  SHIELD  " + Color.DEFAULT;
        }
        if (this == ResourceType.STONE) {
            return Color.AQUA_GREEN_BG + "  STONE   " + Color.DEFAULT;
        }
        if (this == ResourceType.UNKNOWN) {
            return Color.AQUA_GREEN_BG + " UNKNOWN  " + Color.DEFAULT;
        }
        if (this == ResourceType.FAITH) {
            return Color.AQUA_GREEN_BG + "  FAITH   " + Color.DEFAULT;
        }
        if (this == ResourceType.WHITEORB) {
            return Color.AQUA_GREEN_BG + " WHITEORB " + Color.DEFAULT;
        }
        // This should never happen
        return null;
    }
}
