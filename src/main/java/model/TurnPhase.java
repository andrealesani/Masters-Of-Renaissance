package model;

/**
 * This enum contains all possible phases for the players' turn
 */
public enum TurnPhase {
    /**
     * The phase for choosing leader cards or bonus resources
     */
    LEADERCHOICE,
    /**
     * The phase for choosing a specific game action
     */
    ACTIONSELECTION,
    /**
     * The phase for choosing the destination for market resources
     */
    MARKETDISTRIBUTION,
    /**
     * The phase for choosing from where to pay the resources for card purchasing
     */
    CARDPAYMENT,
    /**
     * The phase for choosing from where to pay the resources for production activation
     */
    PRODUCTIONPAYMENT;

    /**
     * Prints the turn phase name with special colors
     *
     * @return the name of the name phase
     */
    public String colorPrint() {
        return Color.AQUA_GREEN_FG + "" + this + Color.RESET;
    }

    /**
     * Prints the turn phase as a more intuitive definition
     *
     * @return the turn phase definition
     */
    public String definitionPrint() {
        String result = null;
        switch (this) {
            case LEADERCHOICE -> result = "choose leaders";
            case ACTIONSELECTION -> result = "choose action";
            case MARKETDISTRIBUTION -> result = "store resources";
            case CARDPAYMENT -> result = "pay for card";
            case PRODUCTIONPAYMENT -> result = "pay for productions";
        }
        return result;
    }

    /**
     * Prints the turn phase as a more intuitive definition, with colors
     *
     * @return the turn phase definition
     */
    public String colorDefinitionPrint() {
        return Color.RESOURCE_STD + definitionPrint() + Color.RESET;
    }
}
