package network;

import Exceptions.GameDataNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.CardColor;
import model.Color;
import model.Game;
import model.PersistenceHandler;
import model.card.DevelopmentCard;
import model.card.leadercard.*;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for keeping general purpose static methods
 */
public class StaticMethods {
    /**
     * Clears the console on windows and linux
     */
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.println("\033c");
            }
            System.out.println("\n" + Color.YELLOW_LIGHT_BG + Color.GREY_DARK_FG + "Hint:" + Color.RESET + " type '" + Color.RESOURCE_STD + "help" + Color.RESET + "' for a list of commands you can do ;)" + "\n");
        } catch (final Exception e) {
            System.out.println("Warning: failed to clear console");
        }
    }

    /**
     * This method creates an ordered list of all the games' DevelopmentCards from their JSON file
     *
     * @return a list of DevelopmentCards, ordered by their IDs
     */
    public static List<DevelopmentCard> getDevelopmentCardsFromJson() {
        Gson gson = new Gson();
        Type DevCardArray = new TypeToken<ArrayList<DevelopmentCard>>() {
        }.getType();

        Reader reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/json/cards/developmentcards/DevelopmentCards.json"), StandardCharsets.UTF_8);
        List<DevelopmentCard> cards = gson.fromJson(reader, DevCardArray);
        int i = 17; // 1 - 16 is used by LeaderCards
        for (DevelopmentCard card : cards)
            card.setId(i++);
        return cards;
    }

    /**
     * This method creates an ordered list of all the games' LeaderCards from their JSON file
     *
     * @return a list of LeaderCards, ordered by their IDs
     */
    public static List<LeaderCard> getLeaderCardsFromJson() {
        Gson gson = new Gson();
        List<LeaderCard> leaderCards = new ArrayList<>();
        Reader reader;

        // depot leader cards
        reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/json/cards/leadercards/DepotLeaderCards.json"), StandardCharsets.UTF_8);
        Type DepotDecArray = new TypeToken<ArrayList<DepotLeaderCard>>() {
        }.getType();
        ArrayList<DepotLeaderCard> depotLeaderCards = gson.fromJson(reader, DepotDecArray);
        leaderCards.addAll(depotLeaderCards);

        // discount leader cards
        reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/json/cards/leadercards/DiscountLeaderCards.json"), StandardCharsets.UTF_8);
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {
        }.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        leaderCards.addAll(discountLeaderCards);

        // marble leader cards
        reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/json/cards/leadercards/MarbleLeaderCards.json"), StandardCharsets.UTF_8);
        Type MarbleDecArray = new TypeToken<ArrayList<MarbleLeaderCard>>() {
        }.getType();
        ArrayList<MarbleLeaderCard> marbleLeaderCards = gson.fromJson(reader, MarbleDecArray);
        leaderCards.addAll(marbleLeaderCards);

        // production leader cards
        reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/json/cards/leadercards/ProductionLeaderCards.json"), StandardCharsets.UTF_8);
        Type ProductionDecArray = new TypeToken<ArrayList<ProductionLeaderCard>>() {
        }.getType();
        ArrayList<ProductionLeaderCard> productionLeaderCards = gson.fromJson(reader, ProductionDecArray);
        leaderCards.addAll(productionLeaderCards);

        int i = 1; // i++ prima passa i e poi lo incrementa => se voglio che id parta da 1 devo settare i a 1
        for (LeaderCard leaderCard : leaderCards) {
            leaderCard.setId(i++);
        }

        return leaderCards;
    }

    /**
     * Takes in input the path of the JSON file to read and the List of decks of a specific color,
     * then it reads the cards from the file and splits them into decks based on the cards level.
     * Level 1 cards will be in the first lists of every column.
     * Level 2 cards will be in the middle (second lists of every column).
     * Level 3 cards will be in the third lists of every column.
     * The method is hardcoded to receive cards with levels from 1 to 3.
     *
     * @param colorCards specifies which column of the deck is going to be instantiated
     * @param color      specifies the color of the cards of the decks to create
     */
    public static void createDecksFromJSON(List<List<DevelopmentCard>> colorCards, CardColor color) {
        List<DevelopmentCard> cards = StaticMethods.getDevelopmentCardsFromJson();
        for (int i = 0; i < 3; i++) {
            colorCards.add(new ArrayList<DevelopmentCard>());
        }
        for (DevelopmentCard developmentCard : cards) {
            if (developmentCard.getColor() == color) {
                if (developmentCard.getLevel() == 1)
                    colorCards.get(0).add(developmentCard);
                else if (developmentCard.getLevel() == 2)
                    colorCards.get(1).add(developmentCard);
                else if (developmentCard.getLevel() == 3)
                    colorCards.get(2).add(developmentCard);
            }
        }
    }

    public static List<Game> restoreGames() throws GameDataNotFoundException {
        Gson gson = new Gson();
        List<Game> games = new ArrayList<>();
        Reader reader;

        try {
            File folder = new File("src/main/resources/savedGames");

            for (final File gameFile : folder.listFiles()) {
                String fileName = gameFile.getName();
                reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/savedGames/" + fileName), StandardCharsets.UTF_8);
                PersistenceHandler persistenceHandler = gson.fromJson(reader, PersistenceHandler.class);
                games.add(persistenceHandler.restoreGame());
            }
        } catch (Exception e) {
            throw new GameDataNotFoundException();
        }

        return games;
    }

    public static void deleteGameData(int gameID) throws GameDataNotFoundException {
        try {
            File file = new File("src/main/resources/savedGames/game" + gameID + ".json");
            if (!file.delete())
                throw new Exception();
        } catch (Exception e) {
            throw new GameDataNotFoundException();
        }
    }

    public static int findFirstFreeId() {
        File folder = new File("src/main/resources/savedGames");

        for (int i = 1; i <= 1000; i++) {
            boolean isFree = true;
            for (final File gameFile : folder.listFiles()) {
                String fileName = gameFile.getName();
                if (fileName.contains(Integer.toString(i)))
                    isFree = false;
            }
            if (isFree)
                return i;
        }
        throw new RuntimeException("Server has more than 1000 saved games");
    }
}
