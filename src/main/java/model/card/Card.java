package model.card;

public abstract class Card {

    private final int victoryPoints;

    private boolean isActive = false;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isActive() { return isActive; }

    public void activate(){ isActive = true; }

    public void deActivate(){ isActive = false; }

    public Card(){
        this.victoryPoints = 0;
    }

    public Card(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
