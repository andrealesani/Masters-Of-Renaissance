package model;

import java.util.List;

public interface Observable {
    void notifyObservers();
    void addObserver(Observer observer);
    List<Observer> getObservers();
}
