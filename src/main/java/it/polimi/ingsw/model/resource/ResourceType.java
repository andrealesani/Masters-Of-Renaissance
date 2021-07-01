package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.Color;

/**
 * This enum contains all possible types for the game's resources and marbles
 */
public enum ResourceType {
    /**
     * The shield resource, and the blue marble
     */
    SHIELD,
    /**
     * The stone resource, and the grey marble
     */
    STONE,
    /**
     * The servant resource, and the purple marble
     */
    SERVANT,
    /**
     * The coin resource, and the yellow marble
     */
    COIN,
    /**
     * The faith special resource, and the red marble
     */
    FAITH,
    /**
     * The white marble
     */
    WHITE_MARBLE,
    /**
     * The prodcution's jolly resource, which can be converted into any basic resource (coin, servant, stone, shield)
     */
    JOLLY;

    //PUBLIC METHODS

    /**
     * Returns the Resource object corresponding to the ResourceType
     *
     * @return The corresponding Resource object
     */
    public Resource toResource() {
        Resource result = null;

        switch (this) {
            case COIN -> result = new ResourceCoin();
            case SERVANT -> result = new ResourceServant();
            case SHIELD -> result = new ResourceShield();
            case STONE -> result = new ResourceStone();
            case JOLLY -> result = new ResourceJolly();
            case FAITH -> result = new ResourceFaith();
            case WHITE_MARBLE -> result = new ResourceWhite();
        }

        return result;
    }

    /**
     * Returns the name of the resource, without any special color formatting
     *
     * @return a String containing the ResourceType name
     */
    public String vanillaToString() {
        return super.toString();
    }

    /**
     * Returns whether or not the resource type can be stored
     *
     * @return true if the resource type can be held in UnlimitedStorages or Depots
     */
    public boolean canBeStored() {
        boolean result = false;

        switch (this) {
            case COIN, STONE, SERVANT, SHIELD -> result = true;
        }

        return result;
    }

    //CLI PRINTING METHODS

    /**
     * Returns the colored ASCII representation of the resource's name
     *
     * @return the resource's ASCII name representation
     */
    public String formattedString() {
        String result = null;

        switch (this) {
            case COIN -> result = Color.YELLOW_LIGHT_BG + "" + Color.GREY_DARK_FG + "   COIN   " + Color.RESET;
            case SERVANT -> result = Color.PURPLE_BG + " SERVANT  " + Color.RESET;
            case SHIELD -> result = Color.LIGHT_BLUE_BG + "  SHIELD  " + Color.RESET;
            case STONE -> result = Color.GREY_LIGHT_BG + "  STONE   " + Color.RESET;
            case JOLLY -> result = Color.RESOURCE_STD + " JOLLY  " + Color.RESET;
            case FAITH -> result = Color.RED_DARK_BG + "  FAITH   " + Color.RESET;
            case WHITE_MARBLE -> result = Color.WHITE_BG + "" + Color.GREY_DARK_FG + " WHITE_MARBLE " + Color.RESET;
        }

        return result;
    }

    /**
     * Returns the colored ASCII representation of the resource
     *
     * @return the resource's ASCII resource representation
     */
    public String iconPrint() {
        String result = null;

        switch (this) {
            case COIN -> result = Color.YELLOW_LIGHT_FG + "coin" + Color.RESET;
            case SERVANT -> result = Color.PURPLE_FG + "servant" + Color.RESET;
            case SHIELD -> result = Color.LIGHT_BLUE_FG + "shield" + Color.RESET;
            case STONE -> result = Color.GREY_LIGHT_FG + "stone" + Color.RESET;
            case JOLLY -> result = Color.RESOURCE_STD + "jolly" + Color.RESET;
            case FAITH -> result = Color.RED_LIGHT_FG + "faith" + Color.RESET;
            case WHITE_MARBLE -> result = Color.WHITE_FG + "white" + Color.RESET;
        }

        return result;
    }

    /**
     * Returns the colored ASCII representation of the resource as a market marble
     *
     * @return the resource's ASCII marble representation
     */
    public String marblePrint() {
        String result = null;

        switch (this) {
            case COIN -> result = Color.YELLOW_LIGHT_FG + "@" + Color.RESET;
            case SERVANT -> result = Color.PURPLE_FG + "@" + Color.RESET;
            case SHIELD -> result = Color.LIGHT_BLUE_FG + "@" + Color.RESET;
            case STONE -> result = Color.GREY_LIGHT_FG + "@" + Color.RESET;
            case JOLLY -> result = Color.RESOURCE_STD + "@" + Color.RESET;
            case FAITH -> result = Color.RED_LIGHT_FG + "@" + Color.RESET;
            case WHITE_MARBLE -> result = Color.WHITE_FG + "@" + Color.RESET;
        }

        return result;
    }

    //GUI PRINTING METHODS

    /**
     * Returns the name of the png file for the resource's icon
     *
     * @return the name of the image file, with the extension
     */
    public String getResourceImage() {
        String result = null;

        switch (this) {
            case COIN -> result = "coin.png";
            case SERVANT -> result = "servant.png";
            case SHIELD -> result = "shield.png";
            case STONE -> result = "stone.png";
            case JOLLY -> result = "jolly.png";
            case FAITH -> result = "faithMarker.png";
            case WHITE_MARBLE -> result = "white_marble.png";
        }

        return result;
    }

    /**
     * Returns the name of the png file for the resource's market marble icon
     *
     * @return the name of the image file, with the extension
     */
    public String getMarbleImage() {
        String result = null;

        switch (this) {
            case COIN -> result = "coin_marble.png";
            case SERVANT -> result = "servant_marble.png";
            case SHIELD -> result = "shield_marble.png";
            case STONE -> result = "stone_marble.png";
            case JOLLY -> result = "jolly.png";
            case FAITH -> result = "faith_marble.png";
            case WHITE_MARBLE -> result = "white_marble.png";
        }

        return result;
    }

}
