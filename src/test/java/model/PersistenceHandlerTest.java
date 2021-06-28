package model;

import Exceptions.BlockedResourceException;
import Exceptions.GameDataNotFoundException;
import Exceptions.NotEnoughSpaceException;
import Exceptions.WrongResourceInsertionException;
import com.google.gson.Gson;
import model.resource.ResourceType;
import org.apache.maven.settings.Server;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

class PersistenceHandlerTest {
    @Test
    void saveGame() throws NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, GameDataNotFoundException {
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
    void restoreGame() throws NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, GameDataNotFoundException, IOException {
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

        File jarFile = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarPath = jarFile.getParentFile().getAbsolutePath();

        FileInputStream file = new FileInputStream(jarPath + "/savedGames/game" + 1 + ".json");
        Reader reader = new InputStreamReader(file, StandardCharsets.UTF_8);
        String og_game = gson.fromJson(reader, PersistenceHandler.class).toString();
        game = persistenceHandler.restoreGame();

        StaticMethods.deleteGameData(persistenceHandler.getId());

        reader.close();
        file.close();
    }
}
