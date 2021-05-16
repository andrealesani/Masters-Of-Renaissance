package network.beans;

import model.Color;

public enum LorenzoTokenType {
    SingleFaith { @Override public String toString() { return Color.RESOURCE_STD + "SINGLE_FAITH" + Color.RESET; }},
    DoubleFaith { @Override public String toString() { return Color.RESOURCE_STD + "DOUBLE_FAITH" + Color.RESET; }},
    RemoveBlue { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_BLUE" + Color.RESET; }},
    RemoveGreen { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_GREEN" + Color.RESET; }},
    RemovePurple { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_PURPLE" + Color.RESET; }},
    RemoveYellow { @Override public String toString() { return Color.RESOURCE_STD + "REMOVE_YELLOW" + Color.RESET; }},
}
