package model.card;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.ResourceType;
import model.resource.*;

import java.util.ArrayList;
import java.util.List;

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
    private final int[] inputQuantities;
    private final  ResourceType[] outputType;
    private final int[] outputQuantities;

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
