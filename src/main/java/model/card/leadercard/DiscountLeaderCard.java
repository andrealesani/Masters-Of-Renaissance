package model.card.leadercard;

import Exceptions.CardAlreadyActiveException;
import Exceptions.ParametersNotValidException;
import model.card.CardColor;
import model.Color;
import model.PlayerBoard;
import model.resource.ResourceType;

/**
 * This LeaderCard awards the user with a discount when buying DevelopmentCards upon activation
 */
public class DiscountLeaderCard extends LeaderCard {
    /**
     * This attribute stores the type of resource cost that the discount applies to
     */
    private final ResourceType discountType;
    /**
     * This attribute stores the amount by which the resource gets discounted
     */
    private final int discount;
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
     * @param discountType       Resource that gets discounted by this card
     * @param discount           number of Resources that get discounted to the player
     * @param requiredColors     CardColor of the DevelopmentCards required to activate this card
     * @param requiredQuantities number of DevelopmentCards of the specified CardColor required to activate this card
     */
    public DiscountLeaderCard(int victoryPoints, ResourceType discountType, int discount, CardColor[] requiredColors, int[] requiredQuantities) {
        super(victoryPoints);
        this.discountType = discountType;
        this.discount = discount;
        this.requiredColors = requiredColors;
        this.requiredQuantities = requiredQuantities;
    }

    //PUBLIC METHODS

    /**
     * Calls the specific method for this LeaderCard, applyDiscount()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) throws CardAlreadyActiveException { /* this method should either be boolean or throw an exception */
        if (isActive())
            throw new CardAlreadyActiveException();
        activate();
        applyDiscount(playerBoard);
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
     * Adds the specified discount to the specified PlayerBoard
     *
     * @param playerBoard specifies which player has activated the discount
     */
    private void applyDiscount(PlayerBoard playerBoard) {
        playerBoard.addDiscount(discountType, discount);
    }

    //GETTERS

    /**
     * Getter
     *
     * @return the type of the resource that gets discounted by activating this leader card
     */
    public ResourceType getDiscountType() {
        return discountType;
    }

    /**
     * Getter
     *
     * @return the number of resources discounted when this leader card gets activated
     */
    public int getDiscount() {
        return discount;
    }

    /**
     * Getter
     *
     * @return the color of the development cards required to activate this leader card
     */
    public CardColor[] getRequiredColors() {
        return requiredColors;
    }

    /**
     * Getter
     *
     * @return the number of development cards of the specified color required to activate this leader card
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

        switch(line) {

            //Row 1
            case 1 -> content +=    super.printLine(1);

            //Row 2
            case 2 -> content +=    super.printLine(2);

            //Row 3
            case 3 -> {
                content +=          " Required cards: ";
                for (int i = 0; i < requiredColors.length; i++) {
                    if (requiredQuantities[i] > 0)
                        content +=  requiredColors[i].iconPrint() + " x " + requiredQuantities[i] + " ";
                }
            }

            //Row 4
            case 4 -> content +=    " Discount: " + "-" + discount + " x " + discountType.iconPrint();

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

        String content =    Color.HEADER + "Discount Leader Card:" + Color.RESET +
                            "\n";

        for (int i = 1; i <= 4; i++)
            content +=      printLine(i) +
                            "\n";

        return content;
    }
}
