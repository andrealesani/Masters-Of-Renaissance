package network.beans;

import model.Game;
import model.Production;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardBeanTest {

    @Test
    void setUsernameFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        assertEquals(game.getCurrentPlayer().getUsername(), pbBean.getUsername());

    }

    @Test
    void setFaithFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        assertEquals(game.getCurrentPlayer().getFaith(), pbBean.getFaith());
    }

    @Test
    void setWhiteMarblesFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        assertEquals(game.getCurrentPlayer().getWhiteMarbles(), pbBean.getWhiteMarbles());
    }

    @Test
    void setProductionsFromPB(){
        List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        List<Production> prod = game.getCurrentPlayer().getProductionHandler().getProductions();
        int[] prodBean = pbBean.getProductions();

        int i;
        int j = game.getCurrentPlayer().getProductionHandler().getProductions().size();
        for(i = 0; i < j; i++){
            assertEquals(prod.get(i).getId(), prodBean[i]);
        }
    }

    @Test
    void setMarbleConversionsFromPB(){
        List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        List<ResourceType> marbleConv = game.getCurrentPlayer().getMarbleConversions();
        ResourceType[] marbleConvBean = pbBean.getMarbleConversions();

        int i;
        int j = game.getCurrentPlayer().getMarbleConversions().size();

        for(i = 0; i < j; i++)
            assertEquals(marbleConv.get(i), marbleConvBean[i]);


    }

    @Test
    void setDiscountFromPB(){
       /* List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        List<ResourceType> marbleConv = game.getCurrentPlayer().getMarbleConversions();
        ResourceType[] marbleConvBean = pbBean.getMarbleConversions();
        */
        
    }

    @Test
    void setCardSlotsFromPB() {
    }

    @Test
    void setLeaderCardsFromPB() {
    }

    @Test
    void setActiveLeaderCardsFromPB() {
    }

    @Test
    void setVpFaithTilesFromPB() {
    }

    @Test
    void setVpFaithValuesFromPB() {
    }

    @Test
    void setPopeTileStatesFromPB() {
    }

    @Test
    void setPopeTilePointsFromPB() {
    }
}