package model;

import model.resource.Resource;
import model.resource.ResourceStone;
import model.resource.ResourceType;
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