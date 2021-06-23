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

    /**
     * Prints a String representation of the color's icon
     *
     * @return the color's String representation
     */
    public String iconPrint() {
        String result = null;

        switch(this) {
            case GREEN -> result = Color.GREEN_FG + "█" + Color.RESET;
            case YELLOW -> result = Color.YELLOW_LIGHT_FG + "█" + Color.RESET;
            case BLUE -> result = Color.LIGHT_BLUE_FG + "█" + Color.RESET;
            case PURPLE -> result = Color.PURPLE_FG + "█" + Color.RESET;
        }

        return result;
    }
}
