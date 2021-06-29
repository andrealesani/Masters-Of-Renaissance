package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import it.polimi.ingsw.network.beans.LorenzoBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoBeanTest {
    @Test
    void test() {
        Set<String> list = new HashSet<>();
        list.add("Gigi");
        Game game = new Game(list);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        LorenzoBean lorenzoBean = new LorenzoBean(new GameController("Gigi", printWriter));
        ((Lorenzo) game.getLorenzo()).addObserver(lorenzoBean);

        assertEquals(game.getLorenzo().getFaith(), lorenzoBean.getFaith());

        assertEquals(((Lorenzo) game.getLorenzo()).getActiveDeck().size(), lorenzoBean.getActiveTokenNumber());

        int i = 0;
        for (ActionToken token : ((Lorenzo) game.getLorenzo()).getUsedDeck()){
            assertEquals(token.getType(), lorenzoBean.getDiscardedTokens()[i++]);
        }
    }
}