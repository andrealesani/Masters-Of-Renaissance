package model.card.leadercard;

import model.CardColor;
import model.PlayerBoard;
import model.card.DevelopmentCard;
import model.Production;

import java.util.HashMap;
import java.util.Map;

/**
 * This LeaderCard lets the user add the specified Production in his PlayerBoard
 */
public class ProductionDecorator extends LeaderCardDecorator {
    private final Production production;
    private final CardColor requiredColor;
    private final int requiredLevel;
    private final int requiredQuantity;

    /**
     * Constructor
     *
     * @param leaderCard - needed to implement the decorator Design Pattern
     * @param production - Production that is added to the PlayerBoard by this card
     * @param requiredColor - CardColor of the DevelopmentCards required to activate this card
     * @param requiredLevel - Minimum card level accepted by the requirements
     * @param requiredQuantity - number of DevelopmentCards of the specified CardColor and level required to activate this card
     */
    public ProductionDecorator(LeaderCard leaderCard, Production production, CardColor requiredColor, int requiredLevel, int requiredQuantity) {
        super(leaderCard);
        this.production = production;
        this.requiredColor = requiredColor;
        this.requiredLevel = requiredLevel;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Adds the specified Production to the specified PlayerBoard
     *
     * @param playerBoard - specifies to which PlayerBoard the Production has to be added
     */
    private void addProduction(PlayerBoard playerBoard){
        playerBoard.addProduction(production);
    }

    /**
     * Calls the specific method for this LeaderCard, addProduction()
     *
     * @param playerBoard - specifies to which PlayerBoard the discount has to be added
     */
    @Override
    public void doAction(PlayerBoard playerBoard){
        addProduction(playerBoard);
    }

    /**
     * Checks if the player has enough DevelopmentCards of the required ColorType and level
     *
     * @param playerBoard - specifies which PlayerBoard to check
     * @return - returns true if the requirements are met, false otherwise
     */
    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard){
        if(playerBoard.getNumOfCards(requiredColor, requiredLevel) >= requiredQuantity) /* ATTENTION atm it's only counting the exact level required and not levels above */
                return true;
        return false;
    }
}
