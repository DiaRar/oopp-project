package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag tag1, tag2, tag3;

    @BeforeEach
    public void beforeEach() {
        tag1 = new Tag("food", new Color(255, 0, 0));
        tag2 = new Tag("food1", new Color(255, 0, 255));
        tag3 = new Tag("food", new Color(255, 0, 0));
    }

    @Test
    public void checkConstructor() {
        Tag tag = new Tag("food", new Color(255, 0, 0));

        // Tag name should be food
        assertEquals("food", tag.getName());

        // Tag color should be red
        assertEquals(new Color(255, 0, 0), tag.getColor());
    }

    @Test
    public void equalsTest() {
        // Same tags should be equal
        assertEquals(tag1, tag3);
    }

    @Test
    public void notEqualsTest() {
        // Different tag name and color tags should not be equal
        assertNotEquals(tag1, tag2);
    }

    @Test
    public void equalsHashCodeTest() {
        // Equal tags should have the same hashCode
        assertEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void toStringTest() {
        // String should contain the tag name
        assertTrue(tag1.toString().contains("food"));
        // String should contain RGB values
        assertTrue(tag1.toString().contains("255"));
    }

}