package model;

public class PopeFavorTile {
    private PopeTileState state;
    private final int victoryPoints;
    private final int activeSectionSize;
    private final int popeSpaceValue;

    public PopeFavorTile(int victoryPoints, int popeSpaceValue, int activeSectionSize) {
        this.victoryPoints = victoryPoints;
        this.activeSectionSize = activeSectionSize;
        this.popeSpaceValue = popeSpaceValue;
        state = PopeTileState.INACTIVE;
    }

    public boolean isTriggered (int playerFaith) {
        if (state == PopeTileState.INACTIVE && playerFaith>=popeSpaceValue) {
            return true;
        }
        return false;
    }

    public void checkActivation (int playerFaith) {
        if (state == PopeTileState.INACTIVE) {
            if (playerFaith>=popeSpaceValue-activeSectionSize+1) {
                state = PopeTileState.ACTIVE;
            } else {
                state = PopeTileState.DISCARDED;
            }
        }
    }

    public int getVictoryPoints() {
        if (state == PopeTileState.ACTIVE) {
            return victoryPoints;
        }
        return 0;
    }
}
