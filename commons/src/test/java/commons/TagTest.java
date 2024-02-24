package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @BeforeEach
    public void beforeEach() {

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

    }

    @Test
    public void notEqualsTest() {
        
    }

    @Test
    public void equalsHashCodeTest() {

    }

    @Test
    public void toStringTest() {

    }

}