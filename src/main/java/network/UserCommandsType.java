package network;

/**
 * This enum contains all the commands the player can send to the server after the login phase.
 * They correspond to in-game actions
 */
public enum UserCommandsType {
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    chooseBonusResourceType,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    chooseLeaderCard,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    playLeaderCard,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    discardLeaderCard,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    selectMarketRow,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    selectMarketColumn,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    sendResourceToDepot,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    chooseMarbleConversion,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    swapDepotContent,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    moveDepotContent,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    takeDevelopmentCard,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    selectProduction,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    resetProductionChoice,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    chooseJollyInput,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    chooseJollyOutput,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    confirmProductionChoice,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    payFromWarehouse,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    payFromStrongbox,
    /**
     * Indicates that the message contains the parameters for the homonymous game method
     */
    endTurn
}
