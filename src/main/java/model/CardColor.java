package model;

/**
 * This enum contains all possible development card color
 */
public enum CardColor {
    GREEN {
        @Override
        public String toString() {
            return "\u001B[36mGREEN\u001B[0m";
        }
    },
    YELLOW {
        @Override
        public String toString() {
            return "\u001B[36mYELLOW\u001B[0m";
        }
    },
    BLUE {
        @Override
        public String toString() {
            return "\u001B[36mBLUE\u001B[0m";
        }
    },
    PURPLE{
        @Override
        public String toString() {
            return "\u001B[36mPURPLE\u001B[0m";
        }
    }
}
