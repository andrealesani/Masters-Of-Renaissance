package model.card;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the development cards in the physical game. All its attributes are set once in the constructor
 * and all its methods are getters except for the addTo() method
 */
public class DevelopmentCard extends Card {
    private final int level;
    private final CardColor color;
    private final List<Resource> cost;
    private final Production production;

    /**
     * Constructor
     *
     * @param level      specifies the level of the card
     * @param color      specifies the color of the card
     * @param cost       specifies how many  Resources the player has to spend in order to buy the card
     * @param production specifies the Production that gets enabled when the player buys this card
     */
    public DevelopmentCard(int victoryPoints, int level, CardColor color, List<Resource> cost, Production production) {
        super(victoryPoints);
        this.level = level;
        this.color = color;
        this.cost = cost;
        this.production = production;
    }

    /**
     * Getter
     *
     * @return returns card level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter
     *
     * @return returns card color
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Getter
     *
     * @return returns card cost
     */
    public List<Resource> getCost() {
        return cost;
    }

    /**
     * Getter
     *
     * @return returns the Production that this card enables for the player
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Adds the DevelopmentCard to the specified PlayerBoard in the specified slot
     *
     * @param playerBoard    - specifies to which PlayerBoard the card has to be added
     * @param productionSlot - specifies in which slot the card has to be added
     */
    public void addTo(PlayerBoard playerBoard, int productionSlot) {
        //TODO
    }
}
