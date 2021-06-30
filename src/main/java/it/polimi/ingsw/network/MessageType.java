package it.polimi.ingsw.network;

/**
 * This enum contains all possible types of messages sent by the server to the client
 */
public enum MessageType {
    //Server view update messages
    /**
     * The game status data
     */
    GAME_BEAN,
    /**
     * The player board data for one player
     */
    PLAYERBOARD_BEAN,
    /**
     * The market data
     */
    MARKET_BEAN,
    /**
     * The card table data
     */
    CARDTABLE_BEAN,
    /**
     * The lorenzo data
     */
    LORENZO_BEAN,
    /**
     * The strongbox data for one player
     */
    STRONGBOX_BEAN,
    /**
     * The warehouse data for one player
     */
    WAREHOUSE_BEAN,
    /**
     * The waiting room data for one player
     */
    WAITINGROOM_BEAN,
    /**
     * The productions data for one player
     */
    PRODUCTIONHANDLER_BEAN,

    //Server informative messages
    /**
     * Informative messages
     */
    INFO,
    /**
     * Error messages
     */
    ERROR,

    //Server event signaling messages
    /**
     * Signals the beginning of the game
     */
    GAME_START,
    /**
     * Signals the wait for more players to join
     */
    WAIT_PLAYERS,
    /**
     * Signals the registration of the client's username
     */
    CONFIRM_USERNAME,
    /**
     * Signals the end of the game
     */
    GAME_END,
    /**
     * Signals the connection of a player to the game
     */
    PLAYER_CONNECTED,
    /**
     * Signals the disconnection of a player from the game
     */
    PLAYER_DISCONNECTED,

    //Client messages
    /**
     * The player's username
     */
    USERNAME,
    /**
     * The game's number of players
     */
    NUM_OF_PLAYERS,
    /**
     * The player's game action command
     */
    COMMAND,

    //General messages
    /**
     * The ping to verify connection
     */
    PING,
}
