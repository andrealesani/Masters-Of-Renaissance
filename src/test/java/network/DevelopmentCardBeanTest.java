package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import network.beans.PlayerBoardBean;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

class DevelopmentCardBeanTest {

    @Test
    void readJSON() {
        Gson gson = new Gson();
        JsonReader reader = null;
        
        try {
            reader = new JsonReader(new FileReader("./src/main/java/network/gsonIN.json"));
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        Type PBBeanArray = new TypeToken<ArrayList<PlayerBoardBean>>() {
        }.getType();
        ArrayList<PlayerBoardBean> playerBoards = gson.fromJson(reader, PBBeanArray);

        System.out.println(gson.toJson(playerBoards));

        /* for(PlayerBoardBean pbbean : playerBoards) {
            System.out.println(pbbean);
        } */
    }
}