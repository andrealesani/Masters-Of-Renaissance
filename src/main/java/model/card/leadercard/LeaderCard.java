package model.card.leadercard;

import model.PlayerBoard;
import model.card.Card;

/**
 * This interface must be implemented by every LeaderCard
 */
public class LeaderCard extends Card {
    public void doAction(PlayerBoard playerBoard){} /* this method should either be boolean or throw an exception */
    public boolean areRequirementsMet(PlayerBoard playerBoard) {return false;}

    public LeaderCard(){}

    public LeaderCard(int victoryPoints) {
        super(victoryPoints);
    }
}
