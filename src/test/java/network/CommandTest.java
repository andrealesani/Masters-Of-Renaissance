package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.Game;
import model.card.DevelopmentCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    @Test
    void test() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./src/main/java/network/testCommand.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Command command = gson.fromJson(reader, Command.class);

        Game game = new Game();
        command.run(game);

    }
}