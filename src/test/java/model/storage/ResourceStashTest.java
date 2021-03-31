package model.storage;

import Exceptions.NotEnoughResourceException;
import Exceptions.ResourceNotPresentException;
import model.resource.Resource;
import model.resource.ResourceShield;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceStashTest {

    @Test
    void emptyStash() {
        ResourceStash stash = new ResourceStash();
        assertTrue(stash.getStoredResources().isEmpty());
    }

    @Test
    void addResourceTest() {
        ResourceStash stash = new ResourceStash();
        Resource shield = new ResourceShield();
        stash.addResource(shield, 3);
        stash.addResource(shield, 3);
        try {
            assertEquals(6, stash.getNumOfResource(shield));
        } catch (ResourceNotPresentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void removeResourceTest() {
        ResourceStash stash = new ResourceStash();
        Resource shield = new ResourceShield();
        stash.addResource(shield, 3);
        try {
            stash.removeResource(shield, 2);
        } catch (ResourceNotPresentException ex) {
            System.out.println(ex.getMessage());
            fail();
        } catch (NotEnoughResourceException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
        try {
            assertEquals(1, stash.getNumOfResource(shield));
        } catch (ResourceNotPresentException ex) {
            System.out.println(ex.getMessage());
            fail();
        }
    }

}