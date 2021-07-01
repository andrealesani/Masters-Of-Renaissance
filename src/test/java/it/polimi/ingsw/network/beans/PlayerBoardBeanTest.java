package it.polimi.ingsw.network.beans;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PopeFavorTile;
import it.polimi.ingsw.model.PopeTileState;
import it.polimi.ingsw.model.card.leadercard.LeaderCard;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.network.beans.PlayerBoardBean;
import it.polimi.ingsw.server.GameController;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardBeanTest {
    /**
     * Tests the updating of the bean's username
     */
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

    /**
     * Tests the updating of the bean's faith
     */
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

    /**
     * Tests the updating of the bean's number of white marbles
     */
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

    /**
     * Tests the updating of the bean's marble conversions
     */
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

    /**
     * Tests the updating of the bean's development card discounts
     */
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
            if (resourceType != ResourceType.FAITH && resourceType != ResourceType.JOLLY && resourceType != ResourceType.WHITEORB)
                if (Arrays.asList(pbBean.getDiscountType()).contains(resourceType)) {
                    assertEquals(game.getCurrentPlayer().getDiscounts().get(resourceType), pbBean.getDiscountQuantity()[java.util.Arrays.asList(pbBean.getDiscountType()).indexOf(resourceType)]);
                }
            }
    }

    /**
     * Tests the updating of the bean's leader cards
     */
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

    /**
     * Tests the updating of the bean's active leader cards
     */
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

    /**
     * Tests the updating of the bean's victory points faith tiles number
     */
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

    /**
     * Tests the updating of the bean's victory points faith tile values
     */
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

    /**
     * Tests the updating of the bean's pope's favor tiles states
     */
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

    /**
     * Tests the updating of the bean's pope's favor tiles victory points
     */
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