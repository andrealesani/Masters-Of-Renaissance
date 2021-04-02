package model;

import model.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a container that holds information about one single production that a player decides to activate during
 * his turn. It has an input, an output and a boolean flag that is set to 'true' when the Production gets selected by
 * the ProductionHandler
 */
public class Production {
    private final List<Resource> input;
    private final List<Resource> output;
    private boolean selectedByHandler;

    public Production(List<Resource> input, List<Resource> output) {
        this.input = input;
        this.output = output;
        selectedByHandler = false;
    }

    public List<Resource> getInput() {
        return input;
    }

    public List<Resource> getOutput() {
        return output;
    }

    public boolean isSelectedByHandler() {
        return this.selectedByHandler;
    }

    public void select() {
        this.selectedByHandler = true;
    }

    public void unselect() {
        this.selectedByHandler = false;
    }
}
