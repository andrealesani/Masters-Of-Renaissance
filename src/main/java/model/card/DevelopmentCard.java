package model.card;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.ResourceType;
import model.resource.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the development cards in the physical game. All its attributes are set once in the constructor and all its methods are getters
 */
public class DevelopmentCard extends Card {
    /**
     * This attribute stores the card's color
     */
    private final CardColor color;
    /**
     * This attribute stores the card's level
     */
    private final int level;
    /**
     * This array stores the types of the resources in the card's cost
     */
    private final ResourceType[] costType;
    /**
     * This array stores the amounts of each type of resource in the card's cost
     */
    private final int[] costQuantity;
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
    private final  ResourceType[] outputType;
    /**
     * This array stores the amounts of each type of resource in the card's production's input
     */
    private final int[] outputQuantities;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param level specifies the level of the card
     * @param color specifies the color of the card
     */
    public DevelopmentCard(int victoryPoints, int level, CardColor color,
                           ResourceType[] costType, int[] costQuantity,
                           ResourceType[] inputType, int[] inputQuantities,
                           ResourceType[] outputType, int[] outputQuantities) {
        super(victoryPoints);
        this.level = level;
        this.color = color;
        this.costType = costType;
        this.costQuantity = costQuantity;
        this.inputType = inputType;
        this.inputQuantities = inputQuantities;
        this.outputType = outputType;
        this.outputQuantities = outputQuantities;
    }

    //GETTERS

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
     * @return returns a list of ResourceTypes that represents the card's cost. Each ResourceType represents one single Resource unit that has to be paid
     */
    public List<ResourceType> getCost() {
        List<ResourceType> costList = new ArrayList<>();
        for (int i = 0; i < costType.length; i++) {
            for (int j = 0; j < costQuantity[i]; j++) {
                costList.add(costType[i]);
            }
        }
        return costList;
    }

    /**
     * Getter
     *
     * @return returns the Production that this card enables for the player
     */
    //TODO make it not use else/ifs
    public Production getProduction() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();

        Resource stone = new ResourceStone();
        Resource coin = new ResourceCoin();
        Resource shield = new ResourceShield();
        Resource servant = new ResourceServant();
        Resource unknown = new ResourceUnknown();
        Resource faith = new ResourceFaith();

        for (int i = 0; i < inputType.length; i++) {
            for(int j = 0; j < inputQuantities[i]; j++) {
                if (inputType[i] == ResourceType.SHIELD) {
                    input.add(shield);
                } else if (inputType[i] == ResourceType.COIN) {
                    input.add(coin);
                } else if (inputType[i] == ResourceType.STONE) {
                    input.add(stone);
                } else if (inputType[i] == ResourceType.SERVANT) {
                    input.add(servant);
                } else if (inputType[i] == ResourceType.UNKNOWN) {
                    input.add(unknown);
                }
            }
        }

        for (int i = 0; i < inputType.length; i++) {
            for(int j = 0; j < outputQuantities[i]; j++) {
                if (outputType[i] == ResourceType.SHIELD) {
                    output.add(shield);
                } else if (outputType[i] == ResourceType.COIN) {
                    output.add(coin);
                } else if (outputType[i] == ResourceType.STONE) {
                    output.add(stone);
                } else if (outputType[i] == ResourceType.SERVANT) {
                    output.add(servant);
                } else if (inputType[i] == ResourceType.UNKNOWN) {
                    input.add(unknown);
                } else if (inputType[i] == ResourceType.FAITH) {
                    input.add(faith);
                }
            }
        }

        return new Production(input, output);
    }
}
