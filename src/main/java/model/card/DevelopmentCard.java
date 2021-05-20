package model.card;

import model.*;
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

        for (int i = 0; i < inputType.length; i++) {
            for(int j = 0; j < inputQuantities[i]; j++) {
                input.add(inputType[i].toResource());
            }
        }

        for (int i = 0; i < inputType.length; i++) {
            for(int j = 0; j < outputQuantities[i]; j++) {
                output.add(outputType[i].toResource());
            }
        }

        return new Production(getId(), input, output);
    }

    @Override
    public String toString() {
        String content = "";
        content += Color.HEADER + "Development Card:" + Color.RESET +
                super.toString() +
                "\n Level: " + level;

        content += "\n Cost: ";
        for (int i = 0; i<costType.length; i++) {
            content += " " + costType[i].iconPrint() + " x " + costQuantity[i] + "  ";
        }

        content += "\n Production Input: ";
        for (int i = 0; i<inputType.length; i++) {
            content += " " + inputType[i].iconPrint() + " x " + inputQuantities[i] + "  ";
        }

        content += "\n Production Output: ";
        for (int i = 0; i<outputType.length; i++) {
            content += " " + outputType[i].iconPrint() + " x " + outputQuantities[i] + "  ";

        }

        return content;
    }
}
