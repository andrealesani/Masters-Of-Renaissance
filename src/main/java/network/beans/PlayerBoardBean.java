package network.beans;

import model.PlayerBoard;
import model.PopeFavorTile;
import model.PopeTileState;
import model.ResourceType;
import model.card.DevelopmentCard;
import model.card.leadercard.LeaderCard;
import model.resource.Resource;

import java.util.Arrays;
import java.util.List;

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

    public int[] getVpFaithTiles() { return vpFaithTiles;}

    public int[] getVpFaithValues() { return vpFaithValues; }

    public PopeTileState[] getPopeTileStates() { return popeTileStates; }

    public int[] getPopeTilePoints() { return popeTilePoints; }

    // SETTERS

    public void setUsernameFromPB(PlayerBoard playerBoard) {
        username = playerBoard.getUsername();
    }

    public void setFaithFromPB(PlayerBoard playerBoard) {
        faith = playerBoard.getFaith();
    }

    public void setWhiteMarblesFromPB(PlayerBoard playerBoard) { whiteMarbles = playerBoard.getWhiteMarbles(); }

    public void setWaitingRoomTypeFromPB(PlayerBoard playerBoard) {
        int i = 0;
        waitingRoomType = new ResourceType[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            if(resourceType == ResourceType.COIN)
                waitingRoomType[i++] = ResourceType.valueOf("COIN");
            else if(resourceType == ResourceType.SERVANT)
                waitingRoomType[i++] = ResourceType.valueOf("SERVANT");
            else if(resourceType == ResourceType.SHIELD)
                waitingRoomType[i++] = ResourceType.valueOf("SHIELD");
            else if(resourceType == ResourceType.STONE)
                waitingRoomType[i++] = ResourceType.valueOf("STONE");
        }

    }

    public void setWaitingRoomQuantitiesFromPB(PlayerBoard playerBoard) {
        int i = 0;
        waitingRoomQuantities = new int[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            if (resourceType == ResourceType.COIN)
                waitingRoomQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.COIN);
            else if (resourceType == ResourceType.SERVANT)
                waitingRoomQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.SERVANT);
            else if (resourceType == ResourceType.SHIELD)
                waitingRoomQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.SHIELD);
            else if (resourceType == ResourceType.STONE)
                waitingRoomQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.STONE);
            else
                waitingRoomQuantities[i++] = 0;
        }
    }

    public void setStrongboxTypeFromPB(PlayerBoard playerBoard) {
        int i = 0;
        strongboxType = new ResourceType[(int) playerBoard.getStrongbox().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getStrongbox().getStoredResources()) {
            if(resourceType == ResourceType.COIN)
                strongboxType[i++] = ResourceType.valueOf("COIN");
            else if(resourceType == ResourceType.SERVANT)
                strongboxType[i++] = ResourceType.valueOf("SERVANT");
            else if(resourceType == ResourceType.SHIELD)
                strongboxType[i++] = ResourceType.valueOf("SHIELD");
            else if(resourceType == ResourceType.STONE)
                strongboxType[i++] = ResourceType.valueOf("STONE");
        }
    }

    public void setStrongboxQuantitiesFromPB(PlayerBoard playerBoard) {
        int i = 0;
        strongboxQuantities = new int[(int) playerBoard.getWaitingRoom().getStoredResources().stream().distinct().count()];
        for (ResourceType resourceType : playerBoard.getWaitingRoom().getStoredResources()) {
            if (resourceType == ResourceType.COIN)
                strongboxQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.COIN);
            else if (resourceType == ResourceType.SERVANT)
                strongboxQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.SERVANT);
            else if (resourceType == ResourceType.SHIELD)
                strongboxQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.SHIELD);
            else if (resourceType == ResourceType.STONE)
                strongboxQuantities[i++] = playerBoard.getWaitingRoom().getNumOfResource(ResourceType.STONE);
            else
                strongboxQuantities[i++] = 0;
        }
    }

    public void setCardSlotsFromPB(PlayerBoard playerBoard) {
        int i;
        cardSlots = new SlotBean[playerBoard.getCardSlots().size()];
        for(i = 0; i < 3; i++) {
            cardSlots[i] = new SlotBean();
            cardSlots[i].setDevelopmentCardsFromPB(playerBoard, i+1);
        }

    }


    public void setLeaderCardsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        leaderCards = new int[playerBoard.getLeaderCards().size()];
        for(LeaderCard leaderCard : playerBoard.getLeaderCards()) {
           leaderCards[i++] = leaderCard.getId();
        }
    }

    public void setActiveLeaderCardsFromPB(PlayerBoard playerBoard) {
        int i = 0;
        activeLeaderCards = new boolean[playerBoard.getLeaderCards().size()];
        for(LeaderCard leaderCard : playerBoard.getLeaderCards()) {
            if(leaderCard.isActive())
                activeLeaderCards[i++] = true;
            else
                activeLeaderCards[i++] = false;
        }
    }

    public void setVpFaithTilesFromPB(PlayerBoard playerBoard) {
        int i;
        int[] current = playerBoard.getVpFaithTiles();
        vpFaithTiles = new int[current.length];
        for(i = 0; i < vpFaithTiles.length; i++)
            vpFaithTiles[i] = current[i];

    }

    public void setVpFaithValuesFromPB(PlayerBoard playerBoard) {
        int i;
        int[] current = playerBoard.getVpFaithValues();
        vpFaithValues = new int[current.length];
        for(i = 0; i < vpFaithValues.length; i++)
            vpFaithValues[i] = current[i];
    }

    public void setPopeTileStatesFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTileStates = new PopeTileState[current.size()];
        for(i = 0; i < popeTileStates.length; i++)
            popeTileStates[i] = current.get(i).getState();
    }

    public void setPopeTilePointsFromPB(PlayerBoard playerBoard) {
        int i;
        List<PopeFavorTile> current = playerBoard.getPopeFavorTiles();
        popeTilePoints = new int[current.size()];
        for(i = 0; i < popeTilePoints.length; i++)
            popeTilePoints[i] = current.get(i).getVictoryPoints();
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
