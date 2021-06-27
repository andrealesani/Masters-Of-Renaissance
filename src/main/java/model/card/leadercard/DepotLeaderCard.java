package model.card.leadercard;

import Exceptions.CardAlreadyActiveException;
import Exceptions.ParametersNotValidException;
import model.Color;
import model.PlayerBoard;
import model.resource.ResourceType;
import model.storage.LeaderDepot;

/**
 * This LeaderCard awards the user with a new Leader Depot in his warehouse upon activation
 */
public class DepotLeaderCard extends LeaderCard {
    /**
     * This attribute stores the card's required resource for activation
     */
    private final ResourceType requiredResource;
    /**
     * This attribute stores the card's required quantity for the resource
     */
    private final int requiredQuantity;
    /**
     * This attribute stores the resource type that can be stored by the card's depot
     */
    private final ResourceType storableResource;
    /**
     * This attribute stores the maximum capacity of the card's depot
     */
    private final int storableQuantity;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param requiredResource ResourceType of the Resources required to activate this card
     * @param requiredQuantity number of Resources of the specified ResourceType required to activate this card
     * @param victoryPoints    the card's victory points number
     * @param storableResource the type of resource the leader's depot can store
     * @param storableQuantity the amount of resource the leader's depot can store
     */
    public DepotLeaderCard(int victoryPoints, ResourceType requiredResource, int requiredQuantity, ResourceType storableResource, int storableQuantity) {
        super(victoryPoints);
        this.requiredResource = requiredResource;
        this.requiredQuantity = requiredQuantity;
        this.storableResource = storableResource;
        this.storableQuantity = storableQuantity;
    }

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, activateSpecialDepot()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) throws CardAlreadyActiveException { /* this method should either be boolean or throw an exception */
        if (isActive())
            throw new CardAlreadyActiveException();
        activate();
        activateSpecialDepot(playerBoard);
    }

    /**
     * Checks if the player has enough Resources of the required type
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if (playerBoard.getNumOfResource(requiredResource) >= requiredQuantity)
            return true;
        return false;
    }

    //PRIVATE METHODS

    /**
     * Adds a SpecialDepot with the parameters specified by the card to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the SpecialDepot has to be added
     */
    private void activateSpecialDepot(PlayerBoard playerBoard) {
        playerBoard.addNewDepot(new LeaderDepot(storableQuantity, storableResource, getId()));
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the Resource required to activate the card
     */
    public ResourceType getRequiredResource() {
        return requiredResource;
    }

    /**
     * Getter
     *
     * @return the quantity of the specified Resource required to activate the card
     */
    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    /**
     * Getter
     *
     * @return the Resource of the depot
     */
    public ResourceType getStorableResource() {
        return storableResource;
    }

    /**
     * Getter
     *
     * @return the quantity of the specified Resource of the depot
     */
    public int getStorableQuantity() {
        return storableQuantity;
    }

    //PRINTING METHODS

    /**
     * This method is used to print only one line of the Warehouse so that multiple objects can be printed
     * in parallel in the CLI
     *
     * @param line the line to print (starts from 1)
     * @return the String with the line to print
     */
    public String printLine(int line) {

        if (line < 1)
            throw new ParametersNotValidException();

        String content = "";

        switch (line) {

            //Row 1
            case 1 -> content += super.printLine(1);

            //Row 2
            case 2 -> content += super.printLine(2);

            //Row 3
            case 3 -> content += " Required resources: " + requiredResource.iconPrint() + " x " + requiredQuantity;

            //Row 4
            case 4 -> {
                content += " Depot: " +
                        "[ ";

                for (int j = 0; j < storableQuantity; j++) {
                    content += Color.GREY_LIGHT_FG + "■ " + Color.RESET;
                }

                content += "(" + storableResource.iconPrint() + ")" +
                        "]";
            }

            default -> content += "";
        }

        return content;
    }

    /**
     * Prints a String representation of the card
     *
     * @return the card's String representation
     */
    @Override
    public String toString() {

        String content = Color.HEADER + "Depot Leader Card:" + Color.RESET +
                "\n";

        for (int i = 1; i <= 4; i++)
            content += printLine(i) +
                    "\n";

        return content;
    }

    //PRIVATE PRINTING METHODS
}


