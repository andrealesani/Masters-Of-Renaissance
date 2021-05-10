package network.beans;

import model.CardTable;
import model.Game;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import network.GameController;
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
        int i = 0;
        for (ActionToken token : ((Lorenzo) game.getLorenzo()).getActiveDeck()){
            assertEquals(token.getType(), lorenzoBean.getActiveTokens()[i++]);
        }
        i = 0;
        for (ActionToken token : ((Lorenzo) game.getLorenzo()).getUsedDeck()){
            assertEquals(token.getType(), lorenzoBean.getDiscardedTokens()[i++]);
        }
    }
}