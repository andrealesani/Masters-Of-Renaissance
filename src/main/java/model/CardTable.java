package model;

import model.card.DevelopmentCard;

import java.util.List;

public class CardTable {
    private List<List<DevelopmentCard>> greenCards;
    private List<List<DevelopmentCard>> blueCards;
    private List<List<DevelopmentCard>> yellowCards;
    private List<List<DevelopmentCard>> purpleCards;

    public void buyTopCard(CardColor color, int position, int idk, PlayerBoard playerBoard){
        //TODO
    }

    public void discardTop(CardColor color, int idk){
        //TODO
    }

    public boolean checkAllColorAvailable(){
        //TODO
        return false;
    }
}
