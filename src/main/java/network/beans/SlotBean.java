package network.beans;

import java.util.Arrays;

public class SlotBean {
    private int[] developmentCards;

    // GETTERS

    public int[] getDevelopmentCards() {
        return developmentCards;
    }

    // SETTERS

    @Override
    public String toString() {
        return "SlotBean{" +
                "\ndevelopmentCards=" + Arrays.toString(developmentCards) +
                '\n' + '}';
    }
}
