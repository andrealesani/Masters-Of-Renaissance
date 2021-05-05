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
    void setWaitingRoomTypeFromPB() {
        List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        game.getCurrentPlayer().addResourceToWaitingRoom(COIN, 1);
        game.getCurrentPlayer().addResourceToWaitingRoom(SERVANT, 1);

        List<ResourceType> WR = game.getCurrentPlayer().getWaitingRoom().getStoredResources();
        ResourceType[] WRBean = pbBean.getWaitingRoomType();

        assertTrue(WR.contains(WRBean[0]));
        assertTrue(WR.contains(WRBean[1]));

    }

    @Test
    void setWaitingRoomQuantitiesFromPB() {
        List<String> nicknames = new ArrayList<String>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);

        game.getCurrentPlayer().addResourceToWaitingRoom(COIN, 1);
        game.getCurrentPlayer().addResourceToWaitingRoom(SERVANT, 3);

        List<ResourceType> WR = game.getCurrentPlayer().getWaitingRoom().getStoredResources();
        ResourceType[] WRBean = pbBean.getWaitingRoomType();

        int[] quantitiesBean = pbBean.getWaitingRoomQuantities();
        int numCoins = game.getCurrentPlayer().getWaitingRoom().getNumOfResource(COIN);
        int numServants = game.getCurrentPlayer().getWaitingRoom().getNumOfResource(SERVANT);

        int i;
        int j = (int) game.getCurrentPlayer().getWaitingRoom().getStoredResources().stream().distinct().count();
        for(i = 0; i < j; i++){
            if(WRBean[i] == COIN)
                assertEquals(numCoins, quantitiesBean[i]);
            else if(WRBean[i] == SERVANT)
                assertEquals(numServants, quantitiesBean[i]);
        }
    }

    @Test
    void setStrongboxTypeFromPB() {
    }

    @Test
    void setStrongboxQuantitiesFromPB() {
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