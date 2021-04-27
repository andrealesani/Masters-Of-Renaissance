package model;

import model.resource.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    @Test
    void toResource() {
        ResourceType type = ResourceType.STONE;
        Resource resource = type.toResource();
        System.out.println(resource.getType());
    }
}