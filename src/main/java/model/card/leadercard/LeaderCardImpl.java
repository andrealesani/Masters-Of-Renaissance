package model.card.leadercard;

import model.PlayerBoard;

/**
 * Implementation of the LeaderCard interface
 */
public class LeaderCardImpl implements LeaderCard {
    private final int victoryPoints;

    public LeaderCardImpl(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }
    @Override
    public void doAction(PlayerBoard playerBoard) {} /* this method should either be boolean or throw an exception */

    @Override
    public boolean areRequirementsMet(PlayerBoard playerBoard) {
        return false;
    }
}
