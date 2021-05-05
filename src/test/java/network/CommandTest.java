package network;

import com.google.gson.Gson;
import model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void serialize() {
        Command command = new Command();
        Gson gson = new Gson();

        command.setCommandType(UserCommandsType.chooseBonusResourceType);
        Map parameters = new HashMap();
        parameters.put("Resource","COIN");
        parameters.put("Quantity", 2);
        command.setParameters(parameters);

        String jsonCommand = gson.toJson(command);
        System.out.println(jsonCommand);

        String expected = "{\"parameters\":{\"Resource\":\"COIN\",\"Quantity\":2},\"commandType\":\"chooseBonusResourceType\"}";
        assertTrue(jsonCommand.equals(expected));

        Command rebuiltCommand = (Command) gson.fromJson(jsonCommand, Command.class);

        assertTrue(rebuiltCommand.getCommandType()==UserCommandsType.chooseBonusResourceType);

        Map rebuiltParameters = rebuiltCommand.getParameters();

        ResourceType resource = ResourceType.valueOf((String) rebuiltParameters.get("Resource"));
        assertTrue(resource == ResourceType.COIN);

        Double quantity = (Double) rebuiltParameters.get("Quantity");
        assertTrue(quantity.intValue() == 2);
    }

    @Test
    void runCommand() {
    }

    @Test
    void setParameters() {
    }

    @Test
    void setCommandType() {
    }
}