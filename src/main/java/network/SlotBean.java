package network;

import java.util.Arrays;
import java.util.List;

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
