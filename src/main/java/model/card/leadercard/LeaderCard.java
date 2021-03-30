package model.card.leadercard;

import model.PlayerBoard;

/**
 * This interface must be implemented by every LeaderCard
 */
public interface LeaderCard {
    void doAction(PlayerBoard playerBoard); /* this method should either be boolean or throw an exception */
    boolean areRequirementsMet(PlayerBoard playerBoard);
}
