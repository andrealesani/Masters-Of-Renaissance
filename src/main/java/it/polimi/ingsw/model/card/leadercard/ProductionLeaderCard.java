package it.polimi.ingsw.model.card.leadercard;

import it.polimi.ingsw.Exceptions.CardIsActiveException;
import it.polimi.ingsw.Exceptions.ParametersNotValidException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.resource.*;

import java.util.ArrayList;

/**
 * This LeaderCard awards the user with the permanent addition of a specific Production in their PlayerBoard upon activation
 */
public class ProductionLeaderCard extends LeaderCard {
    /**
     * This attribute stores the development card color required for activation
     */
    private final CardColor requiredColor;
    /**
     * This attribute stores the development card level required for activation
     */
    private final int requiredLevel;
    /**
     * This attribute stores the amount of development cards of the given color required for activation
     */
    private final int requiredQuantity;
    /**
     * This array stores the types of the resources in the card's production's input
     */
    private final ResourceType[] inputType;
    /**
     * This array stores the amounts of each type of resource in the card's production's input
     */
    private final int[] inputQuantities;
    /**
     * This array stores the types of the resources in the card's production's output
     */
    private final ResourceType[] outputType;
    /**
     * This array stores the amounts of each type of resource in the card's production's input
     */
    private final int[] outputQuantities;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param requiredColor    CardColor of the DevelopmentCards required to activate this card
     * @param requiredLevel    minimum card level accepted by the requirements
     * @param requiredQuantity number of DevelopmentCards of the specified CardColor and level required to activate this card
     * @param victoryPoints    the card's number of victory points
     * @param inputType        the card's production input resource types
     * @param inputQuantities  the card's production input resource quantities
     * @param outputType       the card's production output resource types
     * @param outputQuantities the card's production output resource quantities
     */
    public ProductionLeaderCard(int victoryPoints, CardColor requiredColor, int requiredLevel, int requiredQuantity,
                                ResourceType[] inputType, int[] inputQuantities,
                                ResourceType[] outputType, int[] outputQuantities) {
        super(victoryPoints);
        this.requiredColor = requiredColor;
        this.requiredLevel = requiredLevel;
        this.requiredQuantity = requiredQuantity;
        this.inputType = inputType;
        this.inputQuantities = inputQuantities;
        this.outputType = outputType;
        this.outputQuantities = outputQuantities;
    }

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, addProduction()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) throws CardIsActiveException {
        if (isActive())
            throw new CardIsActiveException();
        activate();
        addProduction(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorType and level
     *
     * @param playerBoard specifies which PlayerBoard to check
     * @return returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        if (playerBoard.getNumOfCards(requiredColor, requiredLevel) >= requiredQuantity) /* ATTENTION atm it's only counting the exact level required and not levels above */
            return true;
        return false;
    }

    //PRIVATE METHODS

    /**
     * Creates a Production from the parameters specified by the LeaderCard and then adds it to the specified PlayerBoard
     *
     * @param playerBoard specifies to which PlayerBoard the Production has to be added
     */
    private void addProduction(PlayerBoard playerBoard) {
        ResourceCoin coin = new ResourceCoin();
        ResourceServant servant = new ResourceServant();
        ResourceShield shield = new ResourceShield();
        ResourceStone stone = new ResourceStone();
        ResourceJolly unknown = new ResourceJolly();
        ResourceFaith faith = new ResourceFaith();

        ArrayList<Resource> input = new ArrayList<>();
        ArrayList<Resource> output = new ArrayList<>();

        for (int i = 0; i < inputQuantities[0]; i++)
            input.add(coin);
        for (int i = 0; i < inputQuantities[1]; i++)
            input.add(servant);
        for (int i = 0; i < inputQuantities[2]; i++)
            input.add(shield);
        for (int i = 0; i < inputQuantities[3]; i++)
            input.add(stone);
        for (int i = 0; i < inputQuantities[4]; i++)
            input.add(unknown);

        for (int i = 0; i < outputQuantities[0]; i++)
            output.add(coin);
        for (int i = 0; i < outputQuantities[1]; i++)
            output.add(servant);
        for (int i = 0; i < outputQuantities[2]; i++)
            output.add(shield);
        for (int i = 0; i < outputQuantities[3]; i++)
            output.add(stone);
        for (int i = 0; i < outputQuantities[4]; i++)
            output.add(unknown);
        for (int i = 0; i < outputQuantities[5]; i++)
            output.add(faith);

        playerBoard.addProduction(new Production(getId(), input, output));
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the CardColor of the DevelopmentCard required to activate this LeaderCard
     */
    public CardColor getRequiredColor() {
        return requiredColor;
    }

    /**
     * Getter
     *
     * @return the level of the DevelopmentCard required to activate this LeaderCard
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * Getter
     *
     * @return the amount of DevelopmentCards of the given color required to activate this LeaderCard
     */
    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    /**
     * Getter
     *
     * @return an array that contains the ResourceTypes of the resources in the card's production's input
     */
    public ResourceType[] getInputType() {
        return inputType;
    }

    /**
     * Getter
     *
     * @return an array that contains the amounts of each type of resource in the card's production's input
     */
    public int[] getInputQuantities() {
        return inputQuantities;
    }

    /**
     * Getter
     *
     * @return an array that contains the ResourceTypes of the resources in the card's production's output
     */
    public ResourceType[] getOutputType() {
        return outputType;
    }

    /**
     * Getter
     *
     * @return an array that contains the amounts of each type of resource in the card's production's output
     */
    public int[] getOutputQuantities() {
        return outputQuantities;
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
            case 3 -> content += " Required card: " + requiredQuantity + " x " + requiredColor.iconPrint() +
                    " of level " + Color.RESOURCE_STD + requiredLevel + Color.RESET;

            //Row 4
            case 4 -> {
                content += " Production Input: ";
                for (int i = 0; i < inputType.length; i++) {
                    if (inputQuantities[i] > 0)
                        content += inputType[i].iconPrint() + " x " + inputQuantities[i] + "  ";
                }
            }

            //Row 5
            case 5 -> {
                content += " Production Output: ";
                for (int i = 0; i < outputType.length; i++) {
                    if (outputQuantities[i] > 0)
                        content += outputType[i].iconPrint() + " x " + outputQuantities[i] + "  ";
                }
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

        String content = Color.HEADER + "Production Leader Card:" + Color.RESET +
                "\n";

        for (int i = 1; i <= 5; i++)
            content += printLine(i) +
                    "\n";

        return content;
    }
}
