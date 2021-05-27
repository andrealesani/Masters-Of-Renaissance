package network;

/**
 * This enum contains all the commands the player can send to the server after the login phase
 */
public enum UserCommandsType {
    chooseBonusResourceType,
    chooseLeaderCard,
    playLeaderCard,
    discardLeaderCard,
    selectMarketRow,
    selectMarketColumn,
    sendResourceToDepot,
    chooseMarbleConversion,
    swapDepotContent,
    moveDepotContent,
    takeDevelopmentCard,
    selectProduction,
    resetProductionChoice,
    chooseJollyInput,
    chooseJollyOutput,
    confirmProductionChoice,
    payFromWarehouse,
    payFromStrongbox,
    endTurn
}
