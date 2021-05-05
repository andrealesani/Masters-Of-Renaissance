package network.beans;

import model.Game;
import model.PlayerBoard;
import model.ResourceType;
import model.resource.ResourceCoin;
import model.storage.UnlimitedStorage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.ResourceType.COIN;
import static model.ResourceType.SERVANT;
import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardBeanTest {

    @Test
    void setUsernameFromPB() {
        List<String> nicknames = new ArrayList<String>();
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
        List<String> nicknames = new ArrayList<String>();
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
        List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        assertEquals(game.getCurrentPlayer().getWhiteMarbles(), pbBean.getWhiteMarbles());
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