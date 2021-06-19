package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Color;
import model.card.DevelopmentCard;
import server.ServerMain;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StaticMethods {
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

        Reader reader = new InputStreamReader(ServerMain.class.getResourceAsStream("/json/cards/developmentcards/DevelopmentCards.json"), StandardCharsets.UTF_8);
        List<DevelopmentCard> cards = gson.fromJson(reader, DevCardArray);
        int i = 1;
        for (DevelopmentCard card : cards)
            card.setId(i++);
        return cards;
    }
}
