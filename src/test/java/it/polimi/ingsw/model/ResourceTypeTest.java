package it.polimi.ingsw.model;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceStone;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    @Test
    void toResource() {
        ResourceType type = ResourceType.STONE;
        Resource resource = type.toResource();
        assertEquals(new ResourceStone(), resource);
    }
}