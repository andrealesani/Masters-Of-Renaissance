package model;

import model.resource.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a container that holds information about one single production that a player decides to activate during
 * his turn. It has an input, an output and a boolean flag that is set to 'true' when the Production gets selected by
 * the ProductionHandler
 */
/* NOTE: in order to fully respect the purpose of this class, we could make input and output final attributes
* and only set them once in the constructor */
public class Production {
    private Map<Resource, Integer> input = new HashMap<>();
    private Map<Resource, Integer> output = new HashMap<>();
    private boolean selectedByHandler;

    public Production(Map<Resource, Integer> input, Map<Resource, Integer> output){
        this.input = input;
        this.output = output;
    }

    public Map<Resource, Integer> getInput() { return input; }

    public void setInput(Map<Resource, Integer> input) { this.input = input; }

    public Map<Resource, Integer> getOutput() { return output; }

    public void setOutput(Map<Resource, Integer> output) { this.output = output; }

    public boolean isSelectedByHandler() { return this.selectedByHandler; }

    public void select() { this.selectedByHandler = true; }

    public void unselect() { this.selectedByHandler = false; }
}
