package model.card;

import model.PlayerBoard;
import model.Production;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/* NOTE: in order to fully respect the purpose of this class, we could make all attributes final
 * and only set them once in the constructor */
public class DevelopmentCard extends Card {
    private final int level;
    private final int color; /* change color to enum? */
    private final Map<Resource, Integer> cost = new HashMap<>();
    private final Production production;

    public DevelopmentCard(int level, int color, Production production) {
        this.level = level;
        this.color = color;
        this.production = production;
    }

    public int getLevel() { return level; }

    public int getColor() { return color; }

    public Production getProduction(){ return production; }

    public void addTo(int quantity, PlayerBoard playerBoard){
        //TODO
    }
}
