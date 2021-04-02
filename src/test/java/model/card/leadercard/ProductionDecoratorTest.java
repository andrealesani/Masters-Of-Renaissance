package model.card.leadercard;

import model.CardColor;
import model.Production;
import model.resource.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductionDecoratorTest {

    @Test
    void doAction() {
        LeaderCard leaderCard1 = new ProductionDecorator(new LeaderCardImpl(), new Production(new ArrayList<Resource>(), new ArrayList<Resource>()), CardColor.BLUE, 2, 3);
    }
}