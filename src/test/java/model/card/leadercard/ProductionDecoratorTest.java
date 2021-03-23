package model.card.leadercard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionDecoratorTest {

    @Test
    void doAction() {
        LeaderCard leaderCard1 = new ProductionDecorator(new LeaderCardImpl());

        leaderCard1.doAction();
    }
}