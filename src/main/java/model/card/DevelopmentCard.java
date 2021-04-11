package model.card;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.ResourceType;
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
    private final CardColor color;
    private final int level;
    private final ResourceType[] costType;
    private final int[] costQuantity;
    private final ResourceType[] inputType;
    private final int[] inputQuantity;
    private final ResourceType[] outputType;
    private final int[] outputQuantities;

    /**
     * Constructor
     *
     * @param level      specifies the level of the card
     * @param color      specifies the color of the card
     */
    public DevelopmentCard(int victoryPoints, int level, CardColor color,
                           ResourceType[] costType, int[] costQuantity,
                           ResourceType[] inputType, int[] inputQuantity,
                           ResourceType[] outputType, int[] outputQuantities) {
        super(victoryPoints);
        this.level = level;
        this.color = color;
        this.costType = costType;
        this.costQuantity = costQuantity;
        this.inputType = inputType;
        this.inputQuantity = inputQuantity;
        this.outputType = outputType;
        this.outputQuantities = outputQuantities;
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
        List<Resource> cost = new ArrayList<>();

        //TODO

        return cost;
    }

    /**
     * Getter
     *
     * @return returns the Production that this card enables for the player
     */
    public Production getProduction() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();

        //TODO

        return new Production(input, output);
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
