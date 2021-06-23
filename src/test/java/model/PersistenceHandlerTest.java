package model;

import com.google.gson.Gson;
import network.StaticMethods;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceHandlerTest {

    @Test
    public void test() {
        Gson gson = new Gson();
        Set<String> players = new HashSet<>();
        players.add("Gigi");
        Game game = new Game(players);
        PersistenceHandler persistenceHandler = new PersistenceHandler(null);

        persistenceHandler.saveGame(game);
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/savedGame.json"), StandardCharsets.UTF_8);
        String og_game = gson.fromJson(reader, PersistenceHandler.class).toString();
        game = persistenceHandler.restoreGame();
    }

}
