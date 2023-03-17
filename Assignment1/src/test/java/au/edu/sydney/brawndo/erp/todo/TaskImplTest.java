package au.edu.sydney.brawndo.erp.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Used ChatGPT to assist me with this test class

public class TaskImplTest {

    private Task sut;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2003, 4, 4, 10, 30);
        sut = new TaskImpl(1, dateTime, "Kitchen", "Weetbix");
    }

    @Test
    void testGetID() {
        assertEquals(1, sut.getID());
    }

    @Test
    void testGetDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2003, 4, 4, 10, 30);
        assertEquals(dateTime, sut.getDateTime());
    }

    @Test
    void testGetLocation() {
        assertEquals("Kitchen", sut.getLocation());
    }

    @Test
    void testGetDescription() {
        assertEquals("Weetbix", sut.getDescription());
    }

    @Test
    void testIsCompleted() {
        assertFalse(sut.isCompleted());
    }

    @Test
    void testSetDateTime() {
        LocalDateTime newDateTime = LocalDateTime.of(2022, 4, 2, 11, 30);
        sut.setDateTime(newDateTime);
        assertEquals(newDateTime, sut.getDateTime());
    }

    @Test
    void testSetDateTimeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            sut.setDateTime(null);
        });
    }

    @Test
    void testSetLocation() {
        sut.setLocation("Bathroom");
        assertEquals("Bathroom", sut.getLocation());
    }

    @Test
    void testSetDescription() {
        sut.setDescription("Toast");
        assertEquals("Toast", sut.getDescription());
    }

    @Test
    void testComplete() {
        assertFalse(sut.isCompleted());
        sut.complete();
        assertTrue(sut.isCompleted());
    }

    @Test
    void testCompleteTwice() {
        sut.complete();
        assertThrows(IllegalStateException.class, () -> {
            sut.complete();
        });
    }

    @Test
    void testGetField() {
        assertEquals("Kitchen", sut.getField(Task.Field.LOCATION));
        assertEquals("Weetbix", sut.getField(Task.Field.DESCRIPTION));
    }

    @Test
    void testGetFieldNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getField(null);
        });
    }

    @Test
    void testGetFieldInvalidField() {
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getField(null);
        });
    }

    @Test
    void testSetLocationNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            sut.setLocation(null);
        });
    }

    @Test
    void testSetLocationTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            sut.setLocation("Lorem ipsum dolor sit amet, consectetur adipiscing" +
                            " elit. Sed fermentum ullamcorper nulla, nec dapibus" +
                            " lectus bibendum eget. Nunc id consectetur velit. " +
                            "Proin vel nibh ut dolor vehicula pulvinar. Fusce " +
                            "blandit leo eget ex malesuada, vitae ullamcorper mauris. " +
                            "Dishwasher"
            );
        });
    }
}