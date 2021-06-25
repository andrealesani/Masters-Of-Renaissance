package model;

import Exceptions.BlockedResourceException;
import Exceptions.GameDataNotFoundException;
import Exceptions.NotEnoughSpaceException;
import Exceptions.WrongResourceInsertionException;
import com.google.gson.Gson;
import model.resource.ResourceType;
import network.StaticMethods;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

class PersistenceHandlerTest {

    @Test
    void saveGame() throws NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, GameDataNotFoundException {
        Gson gson = new Gson();
        Set<String> players = new HashSet<>();
        players.add("Gigi");
        players.add("Gugu");
        players.add("Gogo");
        players.add("Gaga");
        Game game = new Game(players);
        game.getCurrentPlayer().getWarehouse().getDepot(1).addResource(ResourceType.COIN, 1);
        PersistenceHandler persistenceHandler = new PersistenceHandler();

        persistenceHandler.saveGame(game);

        StaticMethods.deleteGameData(persistenceHandler.getId());
    }

    @Test
    void restoreGame() throws NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, GameDataNotFoundException {
        Gson gson = new Gson();
        Set<String> players = new HashSet<>();
        players.add("Gigi");
        players.add("Gugu");
        players.add("Gogo");
        players.add("Gaga");
        Game game = new Game(players);
        game.getCurrentPlayer().getWarehouse().getDepot(1).addResource(ResourceType.COIN, 1);
        PersistenceHandler persistenceHandler = new PersistenceHandler();

        persistenceHandler.saveGame(game);

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/savedGames/game" + persistenceHandler.getId() + ".json"), StandardCharsets.UTF_8);
        String og_game = gson.fromJson(reader, PersistenceHandler.class).toString();
        game = persistenceHandler.restoreGame();

        StaticMethods.deleteGameData(persistenceHandler.getId());
    }
}
