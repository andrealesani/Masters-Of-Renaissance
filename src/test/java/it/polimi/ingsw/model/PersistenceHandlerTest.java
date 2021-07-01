package it.polimi.ingsw.model;

import it.polimi.ingsw.Exceptions.BlockedResourceException;
import it.polimi.ingsw.Exceptions.GameDataNotFoundException;
import it.polimi.ingsw.Exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.Exceptions.WrongResourceInsertionException;
import it.polimi.ingsw.StaticMethods;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.PersistenceHandler;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceHandlerTest {
    /**
     * Tests the saving of a game to disk
     */
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

    /**
     * Tests the restoration of a game from disk
     */
    @Test
    void restoreGame() throws NotEnoughSpaceException, WrongResourceInsertionException, BlockedResourceException, GameDataNotFoundException, IOException {
        Set<String> players = new HashSet<>();
        players.add("Gigi");
        players.add("Gugu");
        players.add("Gogo");
        players.add("Gaga");
        Game game = new Game(players);
        game.getCurrentPlayer().getWarehouse().getDepot(1).addResource(ResourceType.COIN, 1);
        PersistenceHandler persistenceHandler = new PersistenceHandler();

        persistenceHandler.saveGame(game);
        int id = persistenceHandler.getId();

        List<PersistenceHandler> list = StaticMethods.restoreGames();
        List<PersistenceHandler> result = list.stream().filter(e -> e.getId() == id).collect(Collectors.toList());

        assertEquals(1, result.size());

        StaticMethods.deleteGameData(persistenceHandler.getId());
    }
}
