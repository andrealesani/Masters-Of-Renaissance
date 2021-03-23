package model.card.leadercard;

import model.card.Card;

public abstract class LeaderCardDecorator extends Card implements LeaderCard {
    private LeaderCard leaderCard;

    public LeaderCardDecorator(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    public void doAction(){} /* this method should either be boolean or throw an exception */

    public boolean areRequirementsMet(){ return false; }
}
