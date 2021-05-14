package model;

public interface Observer {
    void update(Object observable);
    void updateSinglePlayer(String username);
}
