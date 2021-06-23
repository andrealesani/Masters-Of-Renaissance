package model.lorenzo.tokens;

import model.Color;
import model.resource.ResourceType;

/**
 * Enum used to indicate the type of a Lorenzo single player action token
 */
public enum LorenzoTokenType {
    SingleFaith { @Override public String toString() { return Color.RESOURCE_STD + "SINGLE_FAITH" + Color.RESET; }},
    DoubleFaith { @Override public String toString() { return Color.RESOURCE_STD + "DOUBLE_FAITH" + Color.RESET; }},
    RemoveBlue { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_BLUE" + Color.RESET; }},
    RemoveGreen { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_GREEN" + Color.RESET; }},
    RemovePurple { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_PURPLE" + Color.RESET; }},
    RemoveYellow { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_YELLOW" + Color.RESET; }};

    /**
     * Prints a String representation of the token's icon
     *
     * @return the token's icon
     */
    public String iconPrint() {
        String result = null;

        switch(this) {
            case DoubleFaith -> result = "+2" + Color.RED_LIGHT_FG + "┼" + Color.RESET;
            case SingleFaith -> result = "+1" + Color.RED_LIGHT_FG + "┼" + Color.RESET + "««";
            case RemoveBlue -> result = "-2x" + Color.LIGHT_BLUE_FG + "█" + Color.RESET;
            case RemoveGreen -> result = "-2x" + Color.GREEN_FG + "█" + Color.RESET;
            case RemoveYellow -> result = "-2x" + Color.YELLOW_LIGHT_FG + "█" + Color.RESET;
            case RemovePurple -> result = "-2x" + Color.PURPLE_FG + "█" + Color.RESET;
        }

        return result;
    }

    /**
     * Prints the name of the token's image file
     *
     * @return the name of the png file containing the token's image
     */
    public String getTokenImage() {
        String result = null;

        switch(this) {
            case DoubleFaith -> result = "token_2faith.png";
            case SingleFaith -> result = "token_1faith_reset.png";
            case RemoveBlue -> result = "token_blue.png";
            case RemoveGreen -> result = "token_green.png";
            case RemoveYellow -> result = "token_yellow.png";
            case RemovePurple -> result = "token_purple.png";
        }

        return result;
    }
}
