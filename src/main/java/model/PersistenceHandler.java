package model;

import model.card.DevelopmentCard;
import model.card.leadercard.*;
import model.resource.Resource;
import model.resource.ResourceType;
import network.beans.SlotBean;
import server.GameController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static model.resource.ResourceType.*;
import static model.resource.ResourceType.STONE;

public class PersistenceHandler {

    private transient final GameController controller;

    private Map<CardColor, List<List<DevelopmentCard>>> cards;

    private int[][] cardTable;

    private List<PlayerBoard> playersTurnOrder;

    private String[] username;

    private int lastTriggeredTile;

    private TurnPhase turnPhase;

    private int finalFaith;

    private boolean weReInTheEndGameNow;

    private int initialLeaderCardNumber;

    private int finalLeaderCardNumber;

    private String winner;

    private int winnerVp;

    private ResourceType[][] marketBoard;

    private ResourceType slideMarble;

    private PopeTileState[][] popeTileStates;

    private int[] whiteMarbleNum;

    private int[] faith;

    private ResourceType[][] marbleConversions;

    private ResourceType[] discountType = {COIN, SERVANT, SHIELD, STONE};

    private int[][] discountQuantity;

    private SlotBean[][] cardSlots;

    private int[][] leaderCards;

    private Map<Integer, Integer>[] leaderDepotCards;

    private int[][] vpFaithTiles;

    private int[][] vpFaithValues;

    private int[] devCardMax;

    private int[][] popeTilePoints;

    private int[][] popeTriggerValues;

    private int[][] popeSectionSizes;

    private int[] productions;

    private boolean[] activeProductions;

    private int idProduction;

    private List<Resource> inputv;

    private List<Resource> outputv;

    private boolean selectedByHandler;

    private Map<ResourceType, Integer> input;

    private Map<ResourceType, Integer> output;

    private int sizeBasicDepot;

    private ResourceType storedResourceInBasicDepot;

    private int amountInBasicDepot;

    private int basicDepotNum;

    private ResourceType[] depotType;

    private int[] depotQuantity;

    private int[] depotSizes;

    // CONSTRUCTORS

    private PersistenceHandler(GameController controller) {
        this.controller = controller;
    }

    // GETTERS

    private Map<CardColor, List<List<DevelopmentCard>>> getCards() {
        return cards;
    }

    public int[][] getCardTable() { return cardTable; }

    private List<PlayerBoard> getPlayersTurnOrder() {
        return playersTurnOrder;
    }

    private String[] getUsername() {
        return username;
    }

    private int getLastTriggeredTile() {
        return lastTriggeredTile;
    }

    private int getFinalFaith() {
        return finalFaith;
    }

    private TurnPhase getTurnPhase() {
        return turnPhase;
    }

    private boolean isWeReInTheEndGameNow() {
        return weReInTheEndGameNow;
    }

    private int getInitialLeaderCardNumber() {
        return initialLeaderCardNumber;
    }

    private int getFinalLeaderCardNumber() {
        return finalLeaderCardNumber;
    }

    private String getWinner() {
        return winner;
    }

    private int getWinnerVp() {
        return winnerVp;
    }

    private ResourceType[][] getMarketBoard() {
        return marketBoard;
    }

    private ResourceType getSlideMarble() {
        return slideMarble;
    }

    private PopeTileState[][] getPopeTileStates() {
        return popeTileStates;
    }

    private int[] getWhiteMarbleNum() {
        return whiteMarbleNum;
    }

    private int[] getFaith() {
        return faith;
    }

    private ResourceType[][] getMarbleConversions() {
        return marbleConversions;
    }

    private int[][] getDiscountQuantity() {
        return discountQuantity;
    }

    private SlotBean[][] getCardSlots() {
        return cardSlots;
    }

    private int[][] getLeaderCards() {
        return leaderCards;
    }

    private Map<Integer, Integer>[] getLeaderDepotCards() {
        return leaderDepotCards;
    }

    private int[][] getVpFaithTiles() {
        return vpFaithTiles;
    }

    private int[][] getVpFaithValues() {
        return vpFaithValues;
    }

    public int[] getDevCardMax() {
        return devCardMax;
    }

    private int[][] getPopeTilePoints() {
        return popeTilePoints;
    }

    private int[][] getPopeTriggerValues() {
        return popeTriggerValues;
    }

    private int[][] getPopeSectionSizes() {
        return popeSectionSizes;
    }

    private int getIdProduction() {
        return idProduction;
    }

    private List<Resource> getInputv() {
        return inputv;
    }

    private List<Resource> getOutputv() {
        return outputv;
    }

    private boolean isSelectedByHandler() {
        return selectedByHandler;
    }

    private int[] getProductions() {
        return productions;
    }

    private boolean[] getActiveProductions() {
        return activeProductions;
    }

    private Map<ResourceType, Integer> getInput() {
        return input;
    }

    private Map<ResourceType, Integer> getOutput() {
        return output;
    }

    private int getSize() {
        return sizeBasicDepot;
    }

    private ResourceType getStoredResource() {
        return storedResourceInBasicDepot;
    }

    private int getAmount() {
        return amountInBasicDepot;
    }

    private int getBasicDepotNum() {
        return basicDepotNum;
    }

    private ResourceType[] getDepotType() {
        return depotType;
    }

    private int[] getDepotQuantity() {
        return depotQuantity;
    }

    private int[] getDepotSizes() {
        return depotSizes;
    }

    // PUBLIC METHODS

    public void saveGame(Game game) {
        int i, j, k;

        /* CARD TABLE */
        // da salvare in una int[][][] gli id di tutte le carte
        // cardTable array
        cardTable = new int[3][game.getCardTable().getCards().entrySet().size()];
        cards = game.getCardTable().getCards();

        for (i = 0; i < cardTable.length; i++) {
            for(j = 0; j < 3; j++) {
                for (Map.Entry<CardColor, List<List<DevelopmentCard>>> color : cards.entrySet()) {
                    if (color.getValue().get(i).size() == 0)
                        cardTable[i][j] = -1;
                    else
                        cardTable[i][j] = color.getValue().get(i).get(j).getId();
                    j++;
                }
            }
        }

        /* GAME CLASS */

        lastTriggeredTile = game.getLastTriggeredTile();
        finalFaith = game.getFinalFaith();
        turnPhase = game.getTurnPhase();
        weReInTheEndGameNow = game.isEndGame();
        initialLeaderCardNumber = game.getInitialLeaderCardNumber();
        finalLeaderCardNumber = game.getFinalLeaderCardNumber();
        winner = game.getWinner();
        winnerVp = game.getWinnerVp();

        /* MARKET CLASS */
        Market market = game.getMarket();
        marketBoard = new ResourceType[market.getBoard().length][market.getBoard()[0].length];
        for (i = 0; i < market.getBoard().length; i++) {
            for (j = 0; j < market.getBoard()[0].length; j++) {
                marketBoard[i][j] = market.getBoard()[i][j].getType();
            }
        }
        slideMarble = market.getSlideOrb().getType();

        /* PLAYERBOARD CLASS  */

        for (j = 0; j < game.getPlayersTurnOrder().size(); j++) {
            username[j] = game.getPlayersTurnOrder().get(j).getUsername();

            List<PopeFavorTile> current = game.getPlayersTurnOrder().get(j).getPopeFavorTiles();
            popeTileStates[j] = new PopeTileState[current.size()];
            for (i = 0; i < popeTileStates[j].length; i++)
                popeTileStates[j][i] = current.get(j).getState();

            whiteMarbleNum[j] = game.getPlayersTurnOrder().get(j).getWhiteMarbles();
            faith[j] = game.getPlayersTurnOrder().get(j).getFaith();

            i = 0;
            marbleConversions[j] = new ResourceType[game.getPlayersTurnOrder().get(j).getMarbleConversions().size()];
            for (ResourceType resourceType : game.getPlayersTurnOrder().get(j).getMarbleConversions()) {
                marbleConversions[j][i++] = resourceType;
            }

            i = 0;
            discountType = new ResourceType[game.getPlayersTurnOrder().get(j).getDiscounts().size()];
            discountQuantity[j] = new int[discountType.length];
            for (Map.Entry<ResourceType, Integer> entry : game.getPlayersTurnOrder().get(j).getDiscounts().entrySet()) {
                discountType[i] = entry.getKey();
                discountQuantity[j][i++] = entry.getValue();
            }

            cardSlots[j] = new SlotBean[game.getPlayersTurnOrder().get(j).getCardSlots().size()];
            for (i = 0; i < 3; i++) {
                cardSlots[j][i] = new SlotBean();
                cardSlots[j][i].setDevelopmentCardsFromPB(game.getPlayersTurnOrder().get(j), i + 1);
            }

            i = 0;
            leaderCards[j] = new int[game.getPlayersTurnOrder().get(j).getLeaderCards().size()];
            for (LeaderCard leaderCard : game.getPlayersTurnOrder().get(j).getLeaderCards()) {
                leaderCards[j][i++] = leaderCard.getId();
            }
            leaderDepotCards[j] = game.getPlayersTurnOrder().get(j).getLeaderDepotCards();

            int[] present = game.getPlayersTurnOrder().get(j).getVpFaithTiles();
            vpFaithTiles[j] = new int[present.length];
            for (i = 0; i < vpFaithTiles[j].length; i++)
                vpFaithTiles[j][i] = present[i];

            int[] present2 = game.getPlayersTurnOrder().get(j).getVpFaithValues();
            vpFaithValues[j] = new int[present2.length];
            for (i = 0; i < vpFaithValues[j].length; i++)
                vpFaithValues[j][i] = present2[i];

            devCardMax[j] = game.getPlayersTurnOrder().get(j).getDevCardMax();

            /* POPE FAVOR TILE CLASS */

            List<PopeFavorTile> current2 = game.getPlayersTurnOrder().get(j).getPopeFavorTiles();
            popeTilePoints[j] = new int[current2.size()];
            for (i = 0; i < popeTilePoints[j].length; i++)
                popeTilePoints[j][i] = current2.get(i).getVictoryPoints();

            List<PopeFavorTile> current3 = game.getPlayersTurnOrder().get(j).getPopeFavorTiles();
            popeTriggerValues[j] = new int[current3.size()];
            for (i = 0; i < popeTriggerValues[j].length; i++)
                popeTriggerValues[j][i] = current3.get(i).getTriggerValue();

            List<PopeFavorTile> current4 = game.getPlayersTurnOrder().get(j).getPopeFavorTiles();
            popeSectionSizes[j] = new int[current4.size()];
            for (i = 0; i < popeSectionSizes[j].length; i++)
                popeSectionSizes[j][i] = current4.get(i).getActiveSectionSize();

        }

        /* PRODUCTION CLASS */

        ProductionHandler productionHandler = game.getCurrentPlayer().getProductionHandler();

        productions = new int[productionHandler.getProductions().size()];
        activeProductions = new boolean[productions.length];
        for (i = 0; i < productions.length; i++) {
            productions[i] = productionHandler.getProductions().get(i).getId();
            if (productionHandler.getProductions().get(i).isSelectedByHandler())
                activeProductions[i] = true;
            else
                activeProductions[i] = false;
        }

        input.put(COIN, 0);
        input.put(SERVANT, 0);
        input.put(SHIELD, 0);
        input.put(STONE, 0);
        input.put(UNKNOWN, 0);
        for (ResourceType resourceType : productionHandler.getCurrentInput().stream().map(Resource::getType).collect(Collectors.toList()))
            input.merge(resourceType, 1, Integer::sum);

        output.put(COIN, 0);
        output.put(SERVANT, 0);
        output.put(SHIELD, 0);
        output.put(STONE, 0);
        output.put(FAITH, 0);
        output.put(UNKNOWN, 0);
        for (ResourceType resourceType : productionHandler.getCurrentOutput().stream().map(Resource::getType).collect(Collectors.toList()))
            output.merge(resourceType, 1, Integer::sum);

        /* WAREHOUSE CLASS */

        for (j = 0; j < game.getPlayersTurnOrder().size(); j++) {
            basicDepotNum = game.getPlayersTurnOrder().get(j).getWarehouse().getNumOfDepots();
            depotType = new ResourceType[game.getPlayersTurnOrder().get(j).getWarehouse().getNumOfDepots()];
            depotQuantity = new int[depotType.length];
            depotSizes = new int[depotType.length];

            for (i = 0; i < game.getPlayersTurnOrder().get(j).getWarehouse().getNumOfDepots(); i++) {
                if (game.getPlayersTurnOrder().get(j).getWarehouse().getDepot(i + 1).getStoredResources().size() > 0)
                    depotType[i] = game.getPlayersTurnOrder().get(j).getWarehouse().getDepot(i + 1).getStoredResources().get(0);
                depotQuantity[i] = game.getPlayersTurnOrder().get(j).getWarehouse().getDepot(i + 1).getNumOfResource(depotType[i]);
                depotSizes[i] = game.getPlayersTurnOrder().get(j).getWarehouse().getDepot(i + 1).getSize();
            }


        }


    }
}
