package model.card.leadercard;

import model.CardColor;
import model.resource.ResourceStone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountDecoratorTest {

    @Test
    void doAction() {
        LeaderCard leaderCard1 = new DiscountDecorator(new LeaderCardImpl(), new ResourceStone(), 3, CardColor.BLUE, 2);
    }
}