package model.card;

import model.Color;

/**
 * This enum contains all possible development card color
 */
public enum CardColor {
    /**
     * The green card color
     */
    GREEN,
    /**
     * The yellow card color
     */
    YELLOW,
    /**
     * The blue card color
     */
    BLUE,
    /**
     * The purple card color
     */
    PURPLE;

    /**
     * Prints a colored String representation  of the color's name
     *
     * @return the colored String
     */
    public String colorPrint() {
        String result = null;

        switch (this) {
            case GREEN -> result = Color.GREEN_BG + this.toString() + Color.RESET;
            case YELLOW -> result = Color.YELLOW_DARK_BG + this.toString() + Color.RESET;
            case BLUE -> result = Color.BLUE_BG + this.toString() + Color.RESET;
            case PURPLE -> result = Color.PURPLE_BG + this.toString() + Color.RESET;
        }

        return result;
    }

    /**
     * Prints a String representation of the color's icon
     *
     * @return the color's String representation
     */
    public String iconPrint() {
        String result = null;

        switch (this) {
            case GREEN -> result = Color.GREEN_FG + "█" + Color.RESET;
            case YELLOW -> result = Color.YELLOW_LIGHT_FG + "█" + Color.RESET;
            case BLUE -> result = Color.LIGHT_BLUE_FG + "█" + Color.RESET;
            case PURPLE -> result = Color.PURPLE_FG + "█" + Color.RESET;
        }

        return result;
    }
}
