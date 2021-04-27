package network.beans;

import model.CardTable;
import model.Game;
import model.lorenzo.ArtificialIntelligence;
import model.lorenzo.Lorenzo;
import model.lorenzo.tokens.ActionToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoBeanTest {
    @Test
    void setActiveTokensFromGame() {
        List<String> REE = new ArrayList<>();
        REE.add("Jimmy");
        Game game = new Game(REE);
        for(ActionToken token : ((Lorenzo) game.getLorenzo()).getActiveDeck()) {
            System.out.print(" " + token.getType() + " ");
        }
        System.out.println("\n");
        LorenzoBean lolloBean = new LorenzoBean();
        lolloBean.setActiveTokensFromGame(game);
        for(TokenType token : lolloBean.getActiveTokens()) {
            System.out.print(" " + token + " ");
        }
        System.out.println("\n");

        // Useless, it's just bragging ( * crying dab * , I wrote it to use assertEquals() but eventually I desisted and couldn't delete diz lil artpiece)
        ((Lorenzo) game.getLorenzo()).getActiveDeck().stream().map(ActionToken::getType).forEach(s -> System.out.print(" " + s + " "));
    }

}