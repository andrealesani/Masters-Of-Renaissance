package network.beans;

import model.CardColor;
import model.ResourceType;

import java.util.Arrays;

public class LeaderCardBean {
    private LeaderCardType type;
    private int victoryPoints;

    //DEPOT specific
    private ResourceType requiredResource;
    private int requiredQuantity;
    private ResourceType storableResource;
    private int storableQuantity;

    //DISCOUNT specific
    private ResourceType discountType;
    private int discount;
    private CardColor[] requiredColors;
    private int[] requiredQuantities;

    //MARBLE specific
    private ResourceType resourceType;
    /* requiredColors and requiredQuantities are already declared in DISCOUNT section */

    //PRODUCTION specific
    private CardColor requiredColor;
    private int requiredLevel;
    /* requiredQuantity is already declared in DEPOT section */
    private ResourceType[] inputType;
    private int[] inputQuantities;
    private ResourceType[] outputType;
    private int[] outputQuantities;

    public LeaderCardType getType() {
        return type;
    }

    public void setType(LeaderCardType type) {
        this.type = type;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public ResourceType getRequiredResource() {
        return requiredResource;
    }

    public void setRequiredResource(ResourceType requiredResource) {
        this.requiredResource = requiredResource;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public ResourceType getStorableResource() {
        return storableResource;
    }

    public void setStorableResource(ResourceType storableResource) {
        this.storableResource = storableResource;
    }

    public int getStorableQuantity() {
        return storableQuantity;
    }

    public void setStorableQuantity(int storableQuantity) {
        this.storableQuantity = storableQuantity;
    }

    public ResourceType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(ResourceType discountType) {
        this.discountType = discountType;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public CardColor[] getRequiredColors() {
        return requiredColors;
    }

    public void setRequiredColors(CardColor[] requiredColors) {
        this.requiredColors = requiredColors;
    }

    public int[] getRequiredQuantities() {
        return requiredQuantities;
    }

    public void setRequiredQuantities(int[] requiredQuantities) {
        this.requiredQuantities = requiredQuantities;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public CardColor getRequiredColor() {
        return requiredColor;
    }

    public void setRequiredColor(CardColor requiredColor) {
        this.requiredColor = requiredColor;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
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
        return "LeaderCardBean{" +
                "type=" + type +
                ",\n victoryPoints=" + victoryPoints +
                ",\n requiredResource=" + requiredResource +
                ",\n requiredQuantity=" + requiredQuantity +
                ",\n storableResource=" + storableResource +
                ",\n storableQuantity=" + storableQuantity +
                ",\n discountType=" + discountType +
                ",\n discount=" + discount +
                ",\n requiredColors=" + Arrays.toString(requiredColors) +
                ",\n requiredQuantities=" + Arrays.toString(requiredQuantities) +
                ",\n resourceType=" + resourceType +
                ",\n requiredColor=" + requiredColor +
                ",\n requiredLevel=" + requiredLevel +
                ",\n inputType=" + Arrays.toString(inputType) +
                ",\n inputQuantities=" + Arrays.toString(inputQuantities) +
                ",\n outputType=" + Arrays.toString(outputType) +
                ",\n outputQuantities=" + Arrays.toString(outputQuantities) +
                +'\n' + '}';
    }
}
