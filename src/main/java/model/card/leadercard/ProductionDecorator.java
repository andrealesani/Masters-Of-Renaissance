package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.card.DevelopmentCard;
import model.Production;
import model.resource.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user add the specified Production in his PlayerBoard
 */
public class ProductionDecorator extends LeaderCardDecorator {
    private final CardColor requiredColor;
    private final int requiredLevel;
    private final int requiredQuantity;

    private final int inputCoin;
    private final int inputServant;
    private final int inputShield;
    private final int inputStone;
    private final int inputUnknown;
    private final int outputCoin;
    private final int outputServant;
    private final int outputShield;
    private final int outputStone;
    private final int outputUnknown;
    private final int outputFaith;

    /**
     * Constructor
     *
     * @param leaderCard       needed to implement the decorator Design Pattern
     * @param requiredColor    CardColor of the DevelopmentCards required to activate this card
     * @param requiredLevel    minimum card level accepted by the requirements
     * @param requiredQuantity number of DevelopmentCards of the specified CardColor and level required to activate this card
     */
    public ProductionDecorator(LeaderCard leaderCard, CardColor requiredColor, int requiredLevel, int requiredQuantity,
                               int inputCoin, int inputServant, int inputShield, int inputStone, int inputUnknown,
                               int outputCoin, int outputServant, int outputStone, int outputShield, int outputUnknown, int outputFaith) {
        super(leaderCard);
        this.requiredColor = requiredColor;
        this.requiredLevel = requiredLevel;
        this.requiredQuantity = requiredQuantity;
        this.inputCoin = inputCoin;
        this.inputServant = inputServant;
        this.inputShield = inputShield;
        this.inputStone = inputStone;
        this.inputUnknown = inputUnknown;
        this.outputCoin = outputCoin;
        this.outputServant = outputServant;
        this.outputStone = outputStone;
        this.outputShield = outputShield;
        this.outputUnknown = outputUnknown;
        this.outputFaith = outputFaith;
    }

    /**
     * Creates a Production from the parameters specified by the LeaderCard and then adds it to the specified PlayerBoard
     * NOTE: ATM it creates a new Resource for every cycle. We could declare the Resources at the beginning and always use them to save up some memory
     *
     * @param playerBoard specifies to which PlayerBoard the Production has to be added
     */
    private void addProduction(PlayerBoard playerBoard) {
        ArrayList<Resource> input = new ArrayList<>();
        ArrayList<Resource> output = new ArrayList<>();

        for (int i = 0; i < inputCoin; i++)
            input.add(new ResourceCoin());
        for (int i = 0; i < inputServant; i++)
            input.add(new ResourceServant());
        for (int i = 0; i < inputShield; i++)
            input.add(new ResourceShield());
        for (int i = 0; i < inputStone; i++)
            input.add(new ResourceStone());
        for (int i = 0; i < inputUnknown; i++)
            input.add(new ResourceUnknown());
        for (int i = 0; i < outputCoin; i++)
            output.add(new ResourceCoin());
        for (int i = 0; i < outputServant; i++)
            output.add(new ResourceServant());
        for (int i = 0; i < outputShield; i++)
            output.add(new ResourceShield());
        for (int i = 0; i < outputStone; i++)
            output.add(new ResourceStone());
        for (int i = 0; i < outputUnknown; i++)
            output.add(new ResourceUnknown());
        for (int i = 0; i < outputFaith; i++)
            output.add(new ResourceFaith());

        playerBoard.addProduction(new Production(input, output));
    }

    /**
     * Calls the specific method for this LeaderCard, addProduction()
     *
     * @param playerBoard specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard) {
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
}
