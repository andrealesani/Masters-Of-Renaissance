package Model;

import java.util.HashMap;
import java.util.Map;

public class Production {
    private Map<Resource, Integer> input = new HashMap<>();
    private Map<Resource, Integer> output = new HashMap<>();
    private boolean wasChosen;

    public Map<Resource, Integer> getInput() {
        return input;
    }

    public void setInput(Map<Resource, Integer> input) {
        this.input = input;
    }

    public Map<Resource, Integer> getOutput() {
        return output;
    }

    public void setOutput(Map<Resource, Integer> output) {
        this.output = output;
    }

    public boolean isWasChosen() {
        return wasChosen;
    }

    public void setWasChosen(boolean wasChosen) {
        this.wasChosen = wasChosen;
    }
}
