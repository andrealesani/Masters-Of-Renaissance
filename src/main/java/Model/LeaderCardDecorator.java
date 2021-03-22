package Model;

public abstract class LeaderCardDecorator extends Card implements LeaderCard{

    public void doAction(){} /* this method should either be boolean or throw an exception */

    public boolean areRequirementsMet(){ return false; }
}
