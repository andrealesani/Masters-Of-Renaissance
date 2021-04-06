package model;

import java.util.List;

public class Lobby {
    private List<String> playerLobby;
    private int nextGamePlayers;
    private List<Game> activeGames;

    private void createNewGame(){
        //TODO
    }

    public void addPlayer(String nickname){
        playerLobby.add(nickname);
    }

    public void numberOfPlayers(int num){
        nextGamePlayers = num;
    }
}
