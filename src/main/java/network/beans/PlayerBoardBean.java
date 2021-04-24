package network.beans;

import model.ResourceType;

import java.util.Arrays;

public class PlayerBoardBean {
    private String username;
    private int faith;
    private ResourceType[] marbleConversions;
    private ResourceType[] discountsType;
    private int[] discountsQuantities;
    private ResourceType[] waitingRoomType;
    private int[] waitingRoomQuantities;
    private ResourceType[] strongboxType;
    private int[] strongboxQuantities;
    private SlotBean[] cardSlots;
    private LeaderCardBean[] leaderCards;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFaith() {
        return faith;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }

    public ResourceType[] getMarbleConversions() {
        return marbleConversions;
    }

    public void setMarbleConversions(ResourceType[] marbleConversions) {
        this.marbleConversions = marbleConversions;
    }

    public ResourceType[] getDiscountsType() {
        return discountsType;
    }

    public void setDiscountsType(ResourceType[] discountsType) {
        this.discountsType = discountsType;
    }

    public int[] getDiscountsQuantities() {
        return discountsQuantities;
    }

    public void setDiscountsQuantities(int[] discountsQuantities) {
        this.discountsQuantities = discountsQuantities;
    }

    public ResourceType[] getWaitingRoomType() {
        return waitingRoomType;
    }

    public void setWaitingRoomType(ResourceType[] waitingRoomType) {
        this.waitingRoomType = waitingRoomType;
    }

    public int[] getWaitingRoomQuantities() {
        return waitingRoomQuantities;
    }

    public void setWaitingRoomQuantities(int[] waitingRoomQuantities) {
        this.waitingRoomQuantities = waitingRoomQuantities;
    }

    public ResourceType[] getStrongboxType() {
        return strongboxType;
    }

    public void setStrongboxType(ResourceType[] strongboxType) {
        this.strongboxType = strongboxType;
    }

    public int[] getStrongboxQuantities() {
        return strongboxQuantities;
    }

    public void setStrongboxQuantities(int[] strongboxQuantities) {
        this.strongboxQuantities = strongboxQuantities;
    }

    public SlotBean[] getCardSlots() {
        return cardSlots;
    }

    public void setCardSlots(SlotBean[] cardSlots) {
        this.cardSlots = cardSlots;
    }

    public LeaderCardBean[] getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(LeaderCardBean[] leaderCards) {
        this.leaderCards = leaderCards;
    }

    @Override
    public String toString() {
        return "PlayerBoardBean{" +
                "\n" + "username='" + username + '\'' +
                ",\n faith=" + faith +
                ",\n marbleConversions=" + Arrays.toString(marbleConversions) +
                ",\n discountsType=" + Arrays.toString(discountsType) +
                ",\n discountsQuantities=" + Arrays.toString(discountsQuantities) +
                ",\n waitingRoomType=" + Arrays.toString(waitingRoomType) +
                ",\n waitingRoomQuantities=" + Arrays.toString(waitingRoomQuantities) +
                ",\n strongboxType=" + Arrays.toString(strongboxType) +
                ",\n strongboxQuantities=" + Arrays.toString(strongboxQuantities) +
                ",\n cardSlots=" + Arrays.toString(cardSlots) +
                ",\n leaderCards=" + Arrays.toString(leaderCards) +
                '\n' + '}';
    }
}
