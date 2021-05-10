package network.beans;

import model.*;
import model.card.leadercard.LeaderCard;
import network.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
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
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        List<LeaderCard> leaderCards = game.getCurrentPlayer().getLeaderCards();
        int[] leaderCardsBean = pbBean.getLeaderCards();

        int i;
        for (i = 0; i < leaderCards.size(); i++)
            assertEquals(leaderCardsBean[i], leaderCards.get(i).getId());

    }

    @Test
    void setActiveLeaderCardsFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        List<LeaderCard> activeLeaderCards = game.getCurrentPlayer().getLeaderCards();
        boolean[] activeLeaderCardsBean = pbBean.getActiveLeaderCards();

        int i;
        for(i = 0; i < activeLeaderCards.size(); i++) {
            if (activeLeaderCards.get(i).isActive())
                assertTrue(activeLeaderCardsBean[i]);
            else
                assertFalse(activeLeaderCardsBean[i]);
        }


    }

    @Test
    void setVpFaithTilesFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        int[] vpFaithTiles = game.getCurrentPlayer().getVpFaithTiles();
        int[] vpFaithTilesBean = pbBean.getVpFaithTiles();
        int i;

        for(i = 0; i < vpFaithTilesBean.length; i++)
            assertEquals(vpFaithTilesBean[i], vpFaithTiles[i]);
    }

    @Test
    void setVpFaithValuesFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        int[] vpFaithValues = game.getCurrentPlayer().getVpFaithValues();
        int[] vpFaithValuesBean = pbBean.getVpFaithValues();
        int i;

        for(i = 0; i < vpFaithValuesBean.length; i++)
            assertEquals(vpFaithValuesBean[i], vpFaithValues[i]);
    }

    @Test
    void setPopeTileStatesFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        List<PopeFavorTile> popeFavorTilesStates = game.getCurrentPlayer().getPopeFavorTiles();
        PopeTileState[] popeFavorTilesStatesBean = pbBean.getPopeTileStates();

        int i;
        for (i = 0; i < popeFavorTilesStatesBean.length; i++)
            assertEquals(popeFavorTilesStatesBean[i], popeFavorTilesStates.get(i).getState());
    }

    @Test
    void setPopeTilePointsFromPB() {
        Set<String> nicknames = new HashSet<>();
        nicknames.add("Gigi");
        nicknames.add("Tom");
        nicknames.add("Andre");
        Game game = new Game(nicknames);

        PrintWriter printWriter = new PrintWriter(new StringWriter(), true);
        PlayerBoardBean pbBean = new PlayerBoardBean(new GameController("Gigi", printWriter));
        game.getCurrentPlayer().addObserver(pbBean);

        List<PopeFavorTile> popeFavorTilesPoints = game.getCurrentPlayer().getPopeFavorTiles();
        int[] popeFavorTilesPointsBean = pbBean.getPopeTilePoints();

        int i;
        for (i = 0; i < popeFavorTilesPointsBean.length; i++)
            assertEquals(popeFavorTilesPointsBean[i], popeFavorTilesPoints.get(i).getVictoryPoints());
    }
}