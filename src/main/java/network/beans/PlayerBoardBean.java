package network.beans;

import model.PlayerBoard;
import model.PopeTileState;
import model.ResourceType;

import java.util.Arrays;

public class PlayerBoardBean {
    private String username;
    private int faith;
    private int whiteMarbles;
    private ResourceType[] waitingRoomType;
    private int[] waitingRoomQuantities;
    private ResourceType[] strongboxType;
    private int[] strongboxQuantities;
    private SlotBean[] cardSlots;
    private int[] leaderCards;
    private boolean[] activeLeaderCards;
    private int[] vpFaithTiles;
    private int[] vpFaithValues;
    private PopeTileState[] popeTileStates;
    private int[] popeTilePoints;

    // GETTERS

    public String getUsername() {
        return username;
    }

    public int getFaith() {
        return faith;
    }

    public int getWhiteMarbles() {
        return whiteMarbles;
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

    public int[] getLeaderCards() {
        return leaderCards;
    }

    public boolean[] getActiveLeaderCards() {
        return activeLeaderCards;
    }

    // SETTERS

    public void setFaithFromPB(PlayerBoard playerBoard) {
        faith = playerBoard.getFaith();
    }

    @Override
    public String toString() {
        return "PlayerBoardBean{" +
                "\n" + "username='" + username + '\'' +
                ",\n faith=" + faith +
                ",\n waitingRoomType=" + Arrays.toString(waitingRoomType) +
                ",\n waitingRoomQuantities=" + Arrays.toString(waitingRoomQuantities) +
                ",\n strongboxType=" + Arrays.toString(strongboxType) +
                ",\n strongboxQuantities=" + Arrays.toString(strongboxQuantities) +
                ",\n cardSlots=" + Arrays.toString(cardSlots) +
                ",\n leaderCards=" + Arrays.toString(leaderCards) +
                '\n' + '}';
    }
}
