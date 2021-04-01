package model.card;

public abstract class Card {
    private int victoryPoints;
    private boolean isActive = false;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isActive() { return isActive; }

    public void activate(){ isActive = true; }
}
