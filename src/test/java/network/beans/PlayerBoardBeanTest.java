package network.beans;

import model.Game;
import model.Production;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void setProductionsFromPB() {
        Set<String> nicknames = new HashSet<>();
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
        for (i = 0; i < j; i++) {
            assertEquals(prod.get(i).getId(), prodBean[i]);
        }
    }

    @Test
    void setMarbleConversionsFromPB() {
        Set<String> nicknames = new HashSet<>();
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

        for (i = 0; i < j; i++)
            assertEquals(marbleConv.get(i), marbleConvBean[i]);

    }

    @Test
    void setDiscountFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PlayerBoardBean pbBean = new PlayerBoardBean();
        game.getCurrentPlayer().addObserver(pbBean);
        game.getCurrentPlayer().addDiscount(ResourceType.SHIELD, 1);

        Map<ResourceType, Integer> disc = game.getCurrentPlayer().getDiscounts();

        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType != ResourceType.FAITH && resourceType != ResourceType.UNKNOWN && resourceType != ResourceType.WHITEORB)
                if (Arrays.asList(pbBean.getDiscountType()).contains(resourceType)) {
                    assertEquals(game.getCurrentPlayer().getDiscounts().get(resourceType), pbBean.getDiscountQuantity()[java.util.Arrays.asList(pbBean.getDiscountType()).indexOf(resourceType)]);
                    System.out.println(resourceType);
                }
            }

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