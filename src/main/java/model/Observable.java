package model;

public interface Observable {
    void notifyObservers();
    void addObserver(Observer observer);
}
