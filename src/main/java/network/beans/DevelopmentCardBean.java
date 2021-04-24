package network.beans;

import model.CardColor;
import model.ResourceType;

import java.util.Arrays;

public class DevelopmentCardBean {
    private int victoryPoints;
    private CardColor color;
    private int level;
    private ResourceType[] costType;
    private int[] costQuantity;
    private ResourceType[] inputType;
    private int[] inputQuantities;
    private ResourceType[] outputType;
    private int[] outputQuantities;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ResourceType[] getCostType() {
        return costType;
    }

    public void setCostType(ResourceType[] costType) {
        this.costType = costType;
    }

    public int[] getCostQuantity() {
        return costQuantity;
    }

    public void setCostQuantity(int[] costQuantity) {
        this.costQuantity = costQuantity;
    }

    public ResourceType[] getInputType() {
        return inputType;
    }

    public void setInputType(ResourceType[] inputType) {
        this.inputType = inputType;
    }

    public int[] getInputQuantities() {
        return inputQuantities;
    }

    public void setInputQuantities(int[] inputQuantities) {
        this.inputQuantities = inputQuantities;
    }

    public ResourceType[] getOutputType() {
        return outputType;
    }

    public void setOutputType(ResourceType[] outputType) {
        this.outputType = outputType;
    }

    public int[] getOutputQuantities() {
        return outputQuantities;
    }

    public void setOutputQuantities(int[] outputQuantities) {
        this.outputQuantities = outputQuantities;
    }

    @Override
    public String toString() {
        return "DevelopmentCardBean{" +
                "victoryPoints=" + victoryPoints +
                ",\n color=" + color +
                ",\n level=" + level +
                ",\n costType=" + Arrays.toString(costType) +
                ",\n costQuantity=" + Arrays.toString(costQuantity) +
                ",\n inputType=" + Arrays.toString(inputType) +
                ",\n inputQuantities=" + Arrays.toString(inputQuantities) +
                ",\n outputType=" + Arrays.toString(outputType) +
                ",\n outputQuantities=" + Arrays.toString(outputQuantities) +
                '\n' + '}';
    }
}
