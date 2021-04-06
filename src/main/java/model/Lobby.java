package model;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<String> playerLobby;
    private int nextGamePlayers;
    private List<Game> activeGames;

    private void createNewGame(){
        List<String> playersEnteringGame = new ArrayList<>();
        for(int i=0; i<nextGamePlayers; i++){
            playersEnteringGame.add(playerLobby.get(i));
        }
        activeGames.add(new Game(playersEnteringGame));
    }

    public void addPlayer(String nickname){
        playerLobby.add(nickname);
    }

    public void numberOfPlayers(int num){
        nextGamePlayers = num;
    }
}
