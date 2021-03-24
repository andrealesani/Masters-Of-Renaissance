package model.card.leadercard;

/**
 * This interface must be implemented by every LeaderCard
 */
public interface LeaderCard {
    void doAction(); /* this method should either be boolean or throw an exception */
    boolean areRequirementsMet();
}
