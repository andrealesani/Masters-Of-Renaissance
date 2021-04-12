package model.card;

import model.CardColor;
import model.PlayerBoard;
import model.Production;
import model.ResourceType;
import model.resource.*;

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
    private final List<ResourceType> cost;
    private final Production production;

    /**
     * Constructor
     *
     * @param level      specifies the level of the card
     * @param color      specifies the color of the card
     */
    public DevelopmentCard(int victoryPoints, int level, CardColor color,
                           ResourceType[] costType, int[] costQuantity,
                           ResourceType[] inputType, int[] inputQuantity,
                           ResourceType[] outputType, int[] outputQuantity) {
        super(victoryPoints);
        this.level = level;
        this.color = color;

        cost = InitializeCost(costType, costQuantity);
        production = InitializeProduction(inputType, inputQuantity, outputType, outputQuantity);
    }

    private List<ResourceType> InitializeCost (ResourceType[] costType, int[] costQuantity) {
        List<ResourceType> costList = new ArrayList<>();
        for (int i=0; i<costType.length; i++) {
            while (costQuantity[i]>0) {
                costList.add(costType[i]);
            }
        }
        return costList;
    }

    private Production InitializeProduction (ResourceType[] inputType, int[] inputQuantity,ResourceType[] outputType, int[] outputQuantity){
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();

        Resource stone = new ResourceStone();
        Resource coin = new ResourceCoin();
        Resource shield = new ResourceShield();
        Resource servant = new ResourceServant();

        for (int i=0; i<inputType.length; i++) {
            while (inputQuantity[i]>0) {
                if (inputType[i]==ResourceType.SHIELD) {
                    input.add(shield);
                } else if (inputType[i]==ResourceType.COIN) {
                    input.add(coin);
                } else if (inputType[i]==ResourceType.STONE) {
                    input.add(stone);
                }else if (inputType[i]==ResourceType.SERVANT) {
                    input.add(servant);
                }
            }
        }

        for (int i=0; i<inputType.length; i++) {
            while (outputQuantity[i]>0) {
                if (outputType[i]==ResourceType.SHIELD) {
                    output.add(shield);
                } else if (outputType[i]==ResourceType.COIN) {
                    output.add(coin);
                } else if (outputType[i]==ResourceType.STONE) {
                    output.add(stone);
                }else if (outputType[i]==ResourceType.SERVANT) {
                    output.add(servant);
                }
            }
        }

        return new Production(input, output);
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
        List<ResourceType> cost = new ArrayList<>();

        //TODO
        // RICORDA: NON RESTITUIRE DIRETTAMENTE LA LISTA COST, FANNE UNA COPIA

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
