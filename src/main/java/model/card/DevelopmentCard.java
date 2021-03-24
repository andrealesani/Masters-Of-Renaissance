package model.card;

import model.Production;
import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCard extends Card {
    private int level;
    private int color; /* change color to enum? */
    private Map<Resource, Integer> cost = new HashMap<>();
    private Production production;

    public int getLevel() {
        return level;
    }

    public int getColor() {
        return color;
    }
}
