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

        int i = 0, j;
        for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : game.getCardTable().getCards().entrySet()) {
            j = 0;
            for (List<DevelopmentCard> deck : color.getValue()) {
                //assertEquals(game.getCardTable().getTopCardId(deck), cardTableBean.getCards()[i][j]);
                j++;
            }
            i++;
        }
    }
}