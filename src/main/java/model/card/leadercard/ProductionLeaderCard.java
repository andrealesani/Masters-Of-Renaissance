package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.card.DevelopmentCard;
import model.Production;
import model.resource.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user add the specified Production in his PlayerBoard
 */
public class ProductionLeaderCard extends LeaderCard {
    private final CardColor requiredColor;
    private final int requiredLevel;
    private final int requiredQuantity;
    private final ArrayList<ResourceType> inputType;
    private final int[] inputQuantities;
    private final ArrayList<ResourceType> outputType;
    private final int[] outputQuantities;

    /**
     * Constructor
     *
     * @param requiredColor    CardColor of the DevelopmentCards required to activate this card
     * @param requiredLevel    minimum card level accepted by the requirements
     * @param requiredQuantity number of DevelopmentCards of the specified CardColor and level required to activate this card
     */
    public ProductionLeaderCard(int victoryPoints, CardColor requiredColor, int requiredLevel, int requiredQuantity,
                                ArrayList<ResourceType> inputType, int[] inputQuantities,
                                ArrayList<ResourceType> outputType, int[] outputQuantities) {
        super(victoryPoints);
        this.requiredColor = requiredColor;
        this.requiredLevel = requiredLevel;
        this.requiredQuantity = requiredQuantity;
        this.inputType = inputType;
        this.inputQuantities = inputQuantities;
        this.outputType = outputType;
        this.outputQuantities = outputQuantities;
    }

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
        ResourceUnknown unknown = new ResourceUnknown();
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
