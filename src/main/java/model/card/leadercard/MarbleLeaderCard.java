package model.card.leadercard;

import Exceptions.CardAlreadyActiveException;
import Exceptions.ParametersNotValidException;
import model.card.CardColor;
import model.Color;
import model.PlayerBoard;
import model.resource.ResourceType;

/**
 * This LeaderCard awards the user a conversion for white marbles to a specified Resource type (when they are picked from the Market) upon activation
 */
public class MarbleLeaderCard extends LeaderCard {
    /**
     * This attribute stores the resource type to which the white marble can be converted
     */
    private final ResourceType resourceType;
    /**
     * This array stores the development card colors required for activation
     */
    private final CardColor[] requiredColors;
    /**
     * This array stores the amounts required for each card color
     */
    private final int[] requiredQuantities;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param resourceType       Resource that the WhiteOrb can be transformed into by this card
     * @param requiredColors     CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantities number of DevelopmentCards of the specified CardColor required to activate this card
     * @param victoryPoints      the card's number of victory points
     */
    public MarbleLeaderCard(int victoryPoints, ResourceType resourceType, CardColor[] requiredColors, int[] requiredQuantities) {
        super(victoryPoints);
        this.resourceType = resourceType;
        this.requiredColors = requiredColors;
        this.requiredQuantities = requiredQuantities;
    }

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, activateMarbleEffect()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) throws CardAlreadyActiveException {
        if (isActive())
            throw new CardAlreadyActiveException();
        activate();
        activateMarbleEffect(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorTypes
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        for (int i = 0; i < requiredColors.length; i++) {
            if (playerBoard.getNumOfCards(requiredColors[i]) < requiredQuantities[i])
                return false;
        }
        return true;
    }

    //PRIVATE METHODS

    /**
     * Adds the specified marble conversion to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the marble conversion has to be added
     */
    private void activateMarbleEffect(PlayerBoard playerBoard) {
        playerBoard.addMarbleConversion(resourceType);
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the ResourceType of the Resource to which the white marble can be converted
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Getter
     *
     * @return an array with the colors of the DevelopmentCards that the player needs to own in order to activate this LeaderCard
     */
    public CardColor[] getRequiredColors() {
        return requiredColors;
    }

    /**
     * Getter
     *
     * @return an array with the number of DevelopmentCards for each color that the player needs to own in order to activate this LeaderCard
     */
    public int[] getRequiredQuantities() {
        return requiredQuantities;
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
            case 3 -> {
                content += " Required cards: ";
                for (int i = 0; i < requiredColors.length; i++) {
                    if (requiredQuantities[i] > 0)
                        content += requiredColors[i].iconPrint() + " x " + requiredQuantities[i] + " ";
                }
            }

            //Row 4
            case 4 -> content += " Conversion: " + ResourceType.WHITEORB.iconPrint() + " -> " + resourceType.iconPrint();

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

        String content = Color.HEADER + "Marble Leader Card:" + Color.RESET +
                "\n";

        for (int i = 1; i <= 4; i++)
            content += printLine(i) +
                    "\n";

        return content;
    }
}
