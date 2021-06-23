package model;

import java.util.List;

/**
 * This interface indicates that the implementing class can be observed for changes by Observer objects
 */
public interface Observable {
    /**
     * This method calls the update() on every object observing this object
     */
    void notifyObservers();

    /**
     * Adds an observer to the list of this object's observers
     *
     * @param observer the Observer to be added
     */
    void addObserver(Observer observer);

    /**
     * Returns the list of this object's observers
     *
     * @return a List of the Observers
     */
    List<Observer> getObservers();
}
