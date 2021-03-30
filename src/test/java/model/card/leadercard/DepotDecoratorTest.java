package model.card.leadercard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepotDecoratorTest {

    @Test
    void doAction() {
        LeaderCard leaderCard1 = new DepotDecorator(new LeaderCardImpl());
    }
}