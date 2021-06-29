package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.GameDataNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.leadercard.*;
import org.apache.maven.settings.Server;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class used for keeping general purpose static methods
 */
public class StaticMethods {

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
    
    //PERSISTENCE
    
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

        String jarPath = getJarDirectory();

        File folder = new File(jarPath + "/savedGames");
        if (!folder.exists()){
            if (!folder.mkdirs())
                System.err.println("Unable to create /savedGames folder.");
            else
                System.out.println("Created a new /savedGames folder as there was none.");
        }

        for (final File gameFile : folder.listFiles()) {
            String fileName = gameFile.getName();
            try {
                FileInputStream file = new FileInputStream(jarPath + "/savedGames/" + fileName);
                reader = new InputStreamReader(file, StandardCharsets.UTF_8);
                games.add(gson.fromJson(reader, PersistenceHandler.class));
                reader.close();
                file.close();
            } catch (FileNotFoundException ex) {
                System.err.println("Cannot find file /savedGames/" + fileName);
            } catch (IOException ex) {
                System.err.println("Failure in closing reader or file.");
                ex.printStackTrace();
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
        String jarPath = getJarDirectory();

        try {
            File file = new File(jarPath + "/savedGames/game" + gameID + ".json");
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

        String jarPath = getJarDirectory();

        File folder = new File(jarPath + "/savedGames");
        if (!folder.exists()){
            if (!folder.mkdirs())
                System.err.println("Unable to create /savedGames folder.");
            else
                System.out.println("Created a new /savedGames folder as there was none.");
        }

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

    /**
     * Saves the game in a JSON file named 'game + id + .json' inside the savedGames folder
     *
     * @param persistenceHandler is the game's PersistenceHandler
     */
    public static void saveGameOnDisk(PersistenceHandler persistenceHandler) {
        Gson gson = new Gson();
        int id = persistenceHandler.getId();

        String jarPath = getJarDirectory();

        try {
            PrintWriter writer = new PrintWriter(jarPath + "/savedGames/game" + id + ".json", StandardCharsets.UTF_8);
            writer.print(gson.toJson(persistenceHandler));
            writer.close();
            System.out.println("Saved game with ID " + id);
        } catch (IOException e) {
            System.err.println("Warning: couldn't save game to file.");
            e.printStackTrace();
        }
    }

    // PRIVATE METHODS

    /**
     * Returns the path to the jar file
     *
     * @return the path to the jar file
     */
    private static String getJarDirectory() {
        File jarPath = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return jarPath.getParentFile().getAbsolutePath();
    }
}

