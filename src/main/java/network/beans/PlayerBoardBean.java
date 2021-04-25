package network.beans;

import model.PlayerBoard;
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

    public int getFaith() {
        return faith;
    }

    public ResourceType[] getMarbleConversions() {
        return marbleConversions;
    }

    public ResourceType[] getDiscountsType() {
        return discountsType;
    }

    public int[] getDiscountsQuantities() {
        return discountsQuantities;
    }

    public ResourceType[] getWaitingRoomType() {
        return waitingRoomType;
    }

    public int[] getWaitingRoomQuantities() {
        return waitingRoomQuantities;
    }

    public ResourceType[] getStrongboxType() {
        return strongboxType;
    }

    public int[] getStrongboxQuantities() {
        return strongboxQuantities;
    }

    public SlotBean[] getCardSlots() {
        return cardSlots;
    }

    public LeaderCardBean[] getLeaderCards() {
        return leaderCards;
    }

    public void setFaithFromPB(PlayerBoard playerBoard) {
        faith = playerBoard.getFaith();
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
