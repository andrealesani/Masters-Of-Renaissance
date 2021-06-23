package model;

/**
 * This enum contains all possible development card color
 */
public enum CardColor {
    GREEN {
        public String coloredPrint() {
            return Color.GREEN_BG + "GREEN" + Color.RESET;
        }
    },
    YELLOW {
        public String coloredPrint() {
            return Color.YELLOW_DARK_BG + "YELLOW" + Color.RESET;
        }
    },
    BLUE {
        public String coloredPrint() {
            return Color.BLUE_BG + "BLUE" + Color.RESET;
        }
    },
    PURPLE {
        public String coloredPrint() {
            return Color.PURPLE_BG + "PURPLE" + Color.RESET;
        }
    };

    public String iconPrint() {
        if (this == CardColor.GREEN) {
            return Color.GREEN_FG + "█" + Color.RESET;
        }
        if (this == CardColor.YELLOW) {
            return Color.YELLOW_LIGHT_FG + "█" + Color.RESET;
        }
        if (this == CardColor.BLUE) {
            return Color.LIGHT_BLUE_FG + "█" + Color.RESET;
        }
        if (this == CardColor.PURPLE) {
            return Color.PURPLE_FG + "█" + Color.RESET;
        }
        //this should never happen
        return null;
    }
}
