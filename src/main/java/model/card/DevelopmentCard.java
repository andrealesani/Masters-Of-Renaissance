package model.card;

import model.Production;
import model.Resource;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCard extends Card {
    private int level;
    private int color; /* change color to enum? */
    private Map<Resource, Integer> cost = new HashMap<>();
    private Production production;
}
