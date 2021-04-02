package model.card.leadercard;

import model.CardColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountDecoratorTest {

    @Test
    void doAction() {
        LeaderCard leaderCard1 = new DiscountDecorator(new LeaderCardImpl(), CardColor.BLUE, 2);
    }
}