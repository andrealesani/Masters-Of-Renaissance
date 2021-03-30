package model.card.leadercard;

import model.PlayerBoard;
import model.card.Card;

/**
 * This decorator implements the LeaderCard interface as well as hold the same object.
 * The implemented method from the same interface will simply call the doAction() and areRequirementsMet()
 * methods from the interface
 */
public abstract class LeaderCardDecorator extends Card implements LeaderCard {
    private LeaderCard leaderCard;

    public LeaderCardDecorator(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    public void doAction(PlayerBoard playerBoard){} /* this method should either be boolean or throw an exception */

    public boolean areRequirementsMet(PlayerBoard playerBoard){ return false; }
}
