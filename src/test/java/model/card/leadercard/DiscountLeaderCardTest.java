package model.card.leadercard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.CardColor;
import model.PlayerBoard;
import model.ResourceType;
import model.card.DevelopmentCard;
import model.resource.ResourceStone;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountLeaderCardTest {

    @Test
    void doAction() {
        CardColor[] requiredColors = {CardColor.GREEN, CardColor.BLUE};
        int[] requiredQuantities = {1,2};

        DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(5, ResourceType.COIN, 2, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertTrue(playerBoard.getDiscounts().isEmpty());
        discountLeaderCard.doAction(playerBoard);
        assertFalse(playerBoard.getDiscounts().isEmpty());
        assertEquals(2, playerBoard.getDiscounts().get(ResourceType.COIN));
    }

    @Test
    void areRequirementsMet() {
        CardColor[] requiredColors = {CardColor.GREEN, CardColor.BLUE};
        int[] requiredQuantities = {1,2};
        DevelopmentCard developmentCard = new DevelopmentCard(10, 1, CardColor.GREEN, null, null, null, null, null, null);

        DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(5, ResourceType.COIN, 2, requiredColors, requiredQuantities);
        PlayerBoard playerBoard = new PlayerBoard();

        assertFalse(discountLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCard(1, developmentCard);
        assertFalse(discountLeaderCard.areRequirementsMet(playerBoard));
        playerBoard.addDevelopmentCard(1, new DevelopmentCard(10, 2, CardColor.BLUE, null, null, null, null, null, null));
        //assertTrue(discountLeaderCard.areRequirementsMet(playerBoard));
    }

    @Test
    void JSONDiscountArray() {
        Gson gson = new Gson();
        JsonReader reader = null;

        try {
            reader = new JsonReader(new FileReader("./src/main/java/persistence/cards/leadercards/DiscountLeaderCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Type DiscountDecArray = new TypeToken<ArrayList<DiscountLeaderCard>>() {}.getType();
        ArrayList<DiscountLeaderCard> discountLeaderCards = gson.fromJson(reader, DiscountDecArray);
        assertTrue(discountLeaderCards.get(0).getVictoryPoints() == 2);
        assertTrue(discountLeaderCards.get(0).getDiscountType() == ResourceType.COIN);
        assertTrue(discountLeaderCards.get(0).getDiscount() == 1);
        assertTrue(discountLeaderCards.get(0).getRequiredColors()[0] == CardColor.YELLOW);
        assertTrue(discountLeaderCards.get(0).getRequiredColors()[1] == CardColor.PURPLE);
        assertTrue(discountLeaderCards.get(0).getRequiredQuantities()[0] == 1);
        assertTrue(discountLeaderCards.get(0).getRequiredQuantities()[1] == 1);
    }
}