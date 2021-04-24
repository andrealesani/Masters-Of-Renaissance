package network.beans;

import network.beans.DevelopmentCardBean;

import java.util.Arrays;

public class SlotBean {
    private DevelopmentCardBean[] developmentCards;

    public DevelopmentCardBean[] getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(DevelopmentCardBean[] developmentCards) {
        this.developmentCards = developmentCards;
    }

    @Override
    public String toString() {
        return "SlotBean{" +
                "\ndevelopmentCards=" + Arrays.toString(developmentCards) +
                '\n' + '}';
    }
}
