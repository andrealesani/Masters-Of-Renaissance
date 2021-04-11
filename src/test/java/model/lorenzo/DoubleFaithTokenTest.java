package model.lorenzo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleFaithTokenTest {
    /**
     * This method tests the token's action
     */
    @Test
    void doAction() {
        Lorenzo lorenzo = new Lorenzo(null, null);
        ActionToken token = new DoubleFaithToken(lorenzo);

        token.doAction();

        assertEquals(2, lorenzo.getFaith());
    }
}