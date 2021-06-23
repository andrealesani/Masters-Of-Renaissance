package network;

/**
 * This enum contains all possible types of messages sent by the server to the client
 */
public enum ServerMessageType {
    //View update messages
    GAME,
    PLAYERBOARD,
    MARKET,
    CARDTABLE,
    LORENZO,
    STRONGBOX,
    WAREHOUSE,
    WAITINGROOM,
    PRODUCTIONHANDLER,

    //Informative messages
    INFO,
    ERROR,

    //Event signaling messages
    GAME_START,
    WAIT_PLAYERS,
    SET_USERNAME,
    GAME_END,
    PLAYER_CONNECTED,
    PLAYER_DISCONNECTED,
}
