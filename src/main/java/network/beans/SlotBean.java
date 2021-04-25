package network.beans;

import java.util.Arrays;

public class SlotBean {
    private int[] developmentCards;

    public int[] getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(int[] developmentCards) {
        this.developmentCards = developmentCards;
    }

    @Override
    public String toString() {
        return "SlotBean{" +
                "\ndevelopmentCards=" + Arrays.toString(developmentCards) +
                '\n' + '}';
    }
}
