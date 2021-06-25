package network;

import Exceptions.GameDataNotFoundException;
import model.PersistenceHandler;
import model.StaticMethods;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaticMethodsTest {

    @Test
    void restoreGames() throws GameDataNotFoundException {
        List<PersistenceHandler> restoredGames = StaticMethods.restoreGames();
        File folder = new File("src/main/resources/savedGames");

        assertEquals(folder.listFiles().length, restoredGames.size());
    }
}