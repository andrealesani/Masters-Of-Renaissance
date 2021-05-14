package model;

/**
 * This enum contains all possible development card color
 */
public enum CardColor {
    GREEN {
        @Override
        public String toString() {
            return Color.GREEN_BG + "GREEN" + Color.DEFAULT;
        }
    },
    YELLOW {
        @Override
        public String toString() {
            return Color.GREEN_BG + "YELLOW" + Color.DEFAULT;
        }
    },
    BLUE {
        @Override
        public String toString() {
            return Color.GREEN_BG + "BLUE" + Color.DEFAULT;
        }
    },
    PURPLE{
        @Override
        public String toString() {
            return Color.GREEN_BG + "PURPLE" + Color.DEFAULT;
        }
    }
}
