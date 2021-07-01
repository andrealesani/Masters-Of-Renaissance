package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.GameDataNotFoundException;
import it.polimi.ingsw.StaticMethods;
import it.polimi.ingsw.server.PersistenceHandler;
import org.apache.maven.settings.Server;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaticMethodsTest {

    /**
     * Tests the restoration of PersistenceHandlers saved on disk
     */
    @Test
    void restoreGames() throws GameDataNotFoundException {
        List<PersistenceHandler> restoredGames = StaticMethods.restoreGames();

        File jarFile = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarPath = jarFile.getParentFile().getAbsolutePath();

        File folder = new File(jarPath + "/savedGames");

        assertEquals(folder.listFiles().length, restoredGames.size());
    }
}