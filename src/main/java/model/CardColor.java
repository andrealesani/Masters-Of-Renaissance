package model;

/**
 * This enum contains all possible development card color
 */
public enum CardColor {
    GREEN {
        @Override
        public String toString() {
            return Color.GREEN_BG + "GREEN" + Color.RESET;
        }
        public String iconPrint() {
            return Color.GREEN_FG + "▮" + Color.RESET;
        }
    },
    YELLOW {
        @Override
        public String toString() {
            return Color.YELLOW_DARK_BG + "YELLOW" + Color.RESET;
        }
        public String iconPrint() {
            return Color.YELLOW_LIGHT_FG + "▮" + Color.RESET;
        }
    },
    BLUE {
        @Override
        public String toString() {
            return Color.BLUE_BG + "BLUE" + Color.RESET;
        }
        public String iconPrint() {
            return Color.LIGHT_BLUE_FG + "▮" + Color.RESET;
        }
    },
    PURPLE {
        @Override
        public String toString() {
            return Color.PURPLE_BG + "PURPLE" + Color.RESET;
        }
        public String iconPrint() {
            return Color.PURPLE_FG + "▮" + Color.RESET;
        }

    }
}
