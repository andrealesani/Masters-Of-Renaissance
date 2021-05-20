package model;

import model.resource.Resource;
import model.resource.ResourceType;
import org.junit.jupiter.api.Test;

class ResourceTypeTest {

    @Test
    void toResource() {
        ResourceType type = ResourceType.STONE;
        Resource resource = type.toResource();
        System.out.println(resource.getType());
    }
}