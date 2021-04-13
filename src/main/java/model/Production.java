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

    /**
     * Constructor
     *
     * @param input  specifies the Resources used to produce the output
     * @param output specifies the Resources obtained by executing this Production
     */
    public Production(List<Resource> input, List<Resource> output) {
        this.input = input;
        this.output = output;
        selectedByHandler = false;
    }

    /**
     * Equals
     *
     * @param obj object to compare
     * @return true only if the 2 Productions input and output lists are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Production)
            if (input.equals(((Production) obj).getInput()) && output.equals(((Production) obj).getOutput()))
                return true;
        return false;
    }

    /**
     * Getter
     *
     * @return returns the Production input list
     */
    public List<Resource> getInput() {
        return input;
    }

    /**
     * Getter
     *
     * @return returns the Production output list
     */
    public List<Resource> getOutput() {
        return output;
    }

    /**
     * Getter
     *
     * @return returns true if the Production has been selected by the ProductionHandler to be executed at the end of the turn
     */
    public boolean isSelectedByHandler() {
        return this.selectedByHandler;
    }

    /**
     * Setter for selectedByHandler
     */
    public void select() {
        this.selectedByHandler = true;
    }

    /**
     * Setter for selectedByHandler
     */
    public void unselect() {
        this.selectedByHandler = false;
    }
}
