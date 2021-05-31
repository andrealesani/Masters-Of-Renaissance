package model.resource;

import model.Color;

/**
 * This enum contains all possible types for the game's resources and marbles
 */
public enum ResourceType {
    SHIELD {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "SHIELD" + Color.RESET;
        }
    },
    STONE {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "STONE" + Color.RESET;
        }
    },
    SERVANT {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "SERVANT" + Color.RESET;
        }
    },
    COIN {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "COIN" + Color.RESET;
        }
    },
    FAITH {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "FAITH" + Color.RESET;
        }
    },
    WHITEORB {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "WHITEORB" + Color.RESET;
        }
    },
    UNKNOWN {
        @Override
        public String toString() {
            return Color.RESOURCE_STD + "UNKNOWN" + Color.RESET;
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
            return Color.YELLOW_LIGHT_BG + "" + Color.GREY_DARK_FG + "   COIN   " + Color.RESET;
        }
        if (this == ResourceType.SERVANT) {
            return Color.PURPLE_BG + " SERVANT  " + Color.RESET;
        }
        if (this == ResourceType.SHIELD) {
            return Color.LIGHT_BLUE_BG + "  SHIELD  " + Color.RESET;
        }
        if (this == ResourceType.STONE) {
            return Color.GREY_LIGHT_BG + "  STONE   " + Color.RESET;
        }
        if (this == ResourceType.UNKNOWN) {
            return Color.RESOURCE_STD + " UNKNOWN  " + Color.RESET;
        }
        if (this == ResourceType.FAITH) {
            return Color.RED_DARK_BG + "  FAITH   " + Color.RESET;
        }
        if (this == ResourceType.WHITEORB) {
            return Color.WHITE_BG + "" + Color.GREY_DARK_FG + " WHITEORB " + Color.RESET;
        }
        // This should never happen
        return null;
    }

    public String iconPrint() {
        if (this == ResourceType.COIN) {
            return Color.YELLOW_LIGHT_FG + "coin" + Color.RESET;
        }
        if (this == ResourceType.SERVANT) {
            return Color.PURPLE_FG + "servant" + Color.RESET;
        }
        if (this == ResourceType.SHIELD) {
            return Color.LIGHT_BLUE_FG + "shield" + Color.RESET;
        }
        if (this == ResourceType.STONE) {
            return Color.GREY_LIGHT_FG + "stone" + Color.RESET;
        }
        if (this == ResourceType.UNKNOWN) {
            return Color.RESOURCE_STD + "jolly" + Color.RESET;
        }
        if (this == ResourceType.FAITH) {
            return Color.RED_LIGHT_FG + "faith" + Color.RESET;
        }
        if (this == ResourceType.WHITEORB) {
            return Color.WHITE_FG + "white" + Color.RESET;
        }
        // This should never happen
        return null;
    }

    public String marblePrint() {
        if (this == ResourceType.COIN) {
            return Color.YELLOW_LIGHT_FG + "@" + Color.RESET;
        }
        if (this == ResourceType.SERVANT) {
            return Color.PURPLE_FG + "@" + Color.RESET;
        }
        if (this == ResourceType.SHIELD) {
            return Color.LIGHT_BLUE_FG + "@" + Color.RESET;
        }
        if (this == ResourceType.STONE) {
            return Color.GREY_LIGHT_FG + "@" + Color.RESET;
        }
        if (this == ResourceType.UNKNOWN) {
            return Color.RESOURCE_STD + "@" + Color.RESET;
        }
        if (this == ResourceType.FAITH) {
            return Color.RED_LIGHT_FG + "@" + Color.RESET;
        }
        if (this == ResourceType.WHITEORB) {
            return Color.WHITE_FG + "@" + Color.RESET;
        }
        // This should never happen
        return null;
    }

    public String getMarbleImage() {
        if (this == ResourceType.COIN) {
            return "coin_marble.png";
        }
        if (this == ResourceType.SERVANT) {
            return "servant_marble.png";
        }
        if (this == ResourceType.SHIELD) {
            return "shield_marble.png";
        }
        if (this == ResourceType.STONE) {
            return "stone_marble.png";
        }
        if (this == ResourceType.UNKNOWN) {
            return "";
        }
        if (this == ResourceType.FAITH) {
            return "faith_marble.png";
        }
        if (this == ResourceType.WHITEORB) {
            return "white_marble.png";
        }
        // This should never happen
        return null;
    }
}
