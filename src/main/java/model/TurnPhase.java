package model;

/**
 * This enum contains all possible phases for the players' turn
 */
public enum TurnPhase {
    LEADERCHOICE,
    ACTIONSELECTION,
    MARKETDISTRIBUTION,
    CARDPAYMENT,
    PRODUCTIONPAYMENT;

    /**
     * Prints the turn phase name with special colors
     *
     * @return the name of the name phase
     */
    public String colorPrint() {
        return "\u001B[36m" + this + "\u001B[0m";
    }

    /**
     * Prints the turn phase as a more intuitive definition
     *
     * @return the turn phase definition
     */
    public String definitionPrint() {
        String result = null;
        switch(this) {
            case LEADERCHOICE -> result = "Choose leaders";
            case ACTIONSELECTION -> result = "Choose action";
            case MARKETDISTRIBUTION -> result = "Store resources";
            case CARDPAYMENT -> result = "Pay for card";
            case PRODUCTIONPAYMENT -> result = "Pay for productions";
        }
        return result;
    }

    /**
     * Prints the turn phase as a more intuitive definition, with colors
     *
     * @return the turn phase definition
     */
    public String colorDefinitionPrint() {
        return "\u001B[36m" + definitionPrint() + "\u001B[0m";
    }
}
