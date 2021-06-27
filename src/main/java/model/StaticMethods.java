package model;

import Exceptions.GameDataNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.card.CardColor;
import model.card.DevelopmentCard;
import model.card.leadercard.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
     * Takes in input the List of empty decks of a specific color and the color, reads the cards data from the DevelopmentCards
     * JSON file, then it splits the cards of the specified color into decks based on the cards level.
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
            colorCards.add(new ArrayList<>());
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

    /**
     * Restores the persistence handlers of all saved games
     *
     * @return a List of the restored PersistenceHandlers
     * @throws GameDataNotFoundException if one of the games fails to be restored
     */
    public static List<PersistenceHandler> restoreGames() throws GameDataNotFoundException {
        Gson gson = new Gson();
        List<PersistenceHandler> games = new ArrayList<>();
        Reader reader;

        Map gamesInfo = getSavedGamesInfo(gson);

        System.out.println("Found info file with " + ((Double) gamesInfo.get("maxId")).intValue() + " maxId");

        for (int i = 1; i <= ((Double) gamesInfo.get("maxId")).intValue(); i++) {
            try {
                reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/savedGames/game" + i + ".json"), StandardCharsets.UTF_8);
                games.add(gson.fromJson(reader, PersistenceHandler.class));
            } catch (Exception ignored) {
            }
        }

        return games;
    }

    /**
     * Deletes a specific game's save file
     *
     * @param gameID the game's PersistenceHandler's (and save file's) Id
     * @throws GameDataNotFoundException if the given Id does not correspond to any save file
     */
    public static void deleteGameData(int gameID) throws GameDataNotFoundException {
        try {
            File file = new File("src/main/resources/savedGames/game" + gameID + ".json");
            if (!file.delete())
                throw new Exception();
        } catch (Exception e) {
            throw new GameDataNotFoundException();
        }
    }

    /**
     * Returns the first available free persistence handler Id
     *
     * @return the first free Id
     */
    public static int findFirstFreePersistenceId() {
        Gson gson = new Gson();

        Map gamesInfo = getSavedGamesInfo(gson);

        return ((Double) gamesInfo.get("maxId")).intValue() + 1;

        /*File folder = new File("src/main/resources/savedGames");

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
        throw new RuntimeException("Server has more than 1000 saved games");*/
    }

    // PRIVATE METHODS

    /**
     * Creates a new savedGamesInfo file
     *
     * @param gson the json serializer
     */
    private static Map getSavedGamesInfo(Gson gson) {

        try {
            new InputStreamReader(StaticMethods.class.getResourceAsStream("/savedGames/savedGamesInfo.json"));
            System.out.println("Saved games info file correctly found.");
        } catch (Exception ex) {

            System.out.println("Couldn't read from savedGamesInfo.json, generating a new one.");

            try {
                PrintWriter writer = new PrintWriter("src/main/resources/savedGames/savedGamesInfo.json", StandardCharsets.UTF_8);
                Map file = new HashMap<>();
                file.put("maxId", 0);
                writer.print(gson.toJson(file));
                writer.close();
            } catch (IOException e) {
                System.err.println("Warning: couldn't create savedGamesInfo file.");
                e.printStackTrace();
            }
        }

        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(StaticMethods.class.getResourceAsStream("/savedGames/savedGamesInfo.json"), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            System.err.println("Something went wrong while accessing the savedGamesInfo file.");
            ex.printStackTrace();
        }

        return gson.fromJson(reader, Map.class);
    }
}
