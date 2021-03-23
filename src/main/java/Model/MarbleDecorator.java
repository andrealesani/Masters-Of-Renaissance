package Model;

import java.util.HashMap;
import java.util.Map;

public class MarbleDecorator extends LeaderCardDecorator{
    private Resource resource;
    private Map<DevelopmentCard, Integer> requiredCards = new HashMap<>();

    private void activateMarbleEffect(){
        //TODO
    }

    @Override
    public void doAction(){
        activateMarbleEffect();
    }
}
