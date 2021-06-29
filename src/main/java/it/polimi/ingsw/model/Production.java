package it.polimi.ingsw.model;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceJolly;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a container that holds information about one single production that a player decides to activate during
 * his turn. It has an input, an output and a boolean flag that is set to 'true' when the Production gets selected by
 * the ProductionHandler
 */
public class Production {
    /**
     * The id of the card which contains this production
     */
    private final int id;
    /**
     * A List of the resources required as input for the production
     */
    private final List<Resource> input;
    /**
     * A List of the resources obtained as output for the production
     */
    private final List<Resource> output;
    /**
     * A flag indicated whether the production has been selected by the production handler
     */
    private boolean selectedByHandler;

    //CONSTRUCTORS

    /**
     * Default constructor builds default Production (2 ResourceJolly in input and 1 ResourceJolly in output) with id 0
     */
    public Production() {
        id = 0;
        Resource jolly = new ResourceJolly();
        input = new ArrayList<>();
        input.add(jolly);
        input.add(jolly);
        output = new ArrayList<>();
        output.add(jolly);
        selectedByHandler = false;
    }

    /**
     * Constructor
     *
     * @param id     the production's card's id
     * @param input  specifies the Resources used to produce the output
     * @param output specifies the Resources obtained by executing this Production
     */
    public Production(int id, List<Resource> input, List<Resource> output) {
        this.id = id;
        this.input = input;
        this.output = output;
        selectedByHandler = false;
    }

    //PUBLIC METHODS

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

    //GETTERS

    /**
     * Getter
     *
     * @return the id of the card that represents this production
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     *
     * @return returns the Production input list
     */
    public List<Resource> getInput() {
        return new ArrayList<>(input);
    }

    /**
     * Getter
     *
     * @return returns the Production output list
     */
    public List<Resource> getOutput() {
        return new ArrayList<>(output);
    }

    /**
     * Getter
     *
     * @return returns true if the Production has been selected by the ProductionHandler to be executed at the end of the turn
     */
    public boolean isSelectedByHandler() {
        return this.selectedByHandler;
    }

    //EQUALS

    /**
     * Equals controls only if the input and output lists are equals. This means that 2 Productions with different IDs
     * but the same input/output will be considered equal
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
}
