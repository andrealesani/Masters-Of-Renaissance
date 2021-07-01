package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.network.beans.CardTableBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTableBeanTest {
    /**
     * Tests the updating of the bean
     */
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        list.add("Pipino");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        CardTableBean cardTableBean = new CardTableBean(new GameController("Gigi", printWriter));
        game.getCardTable().addObserver(cardTableBean);

        int i, j;
        for (i = 0; i < cardTableBean.getCards().length; i++) {
            j = 0;
            for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
                if (color.getValue().get(i).size() == 0)
                    assertEquals(-1, cardTableBean.getCards()[i][j]);
                else
                assertEquals(cardTableBean.getCards()[i][j], color.getValue().get(i).get(0).getId());
                j++;
            }
        }
    }
}