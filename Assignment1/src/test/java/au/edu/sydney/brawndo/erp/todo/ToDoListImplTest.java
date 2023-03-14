package au.edu.sydney.brawndo.erp.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoListImplTest {

    private ToDoListImpl sut;

    @BeforeEach
    void createToDoList() {
        sut = new ToDoListImpl();
    }

    @Test
    void testAddValidIdInternal() {
//        I would have preferred to test this using a mock task, however
//        the way the interface is created does not allow this to happen
//        to my knowledge
        assertEquals(0, sut.getMap().size());
        sut.add(null, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        sut.add(null, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
    }

    @Test
    void testAddValidIdExternal() {
        assertEquals(0, sut.getMap().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
    }

    @Test
    void testAddIdExternalNotUnique() {
        assertEquals(0, sut.getMap().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(0, LocalDateTime.now(), "kitchen", "Dishwasher");
        });
        assertEquals(1, sut.getMap().size());
    }

    @Test
    void testAddInternalThenExternal() {
//        Behaviour not specified yet
    }

    @Test
    void testAddExternalThenInternalId() {
        assertEquals(0, sut.getMap().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        assertThrows(IllegalStateException.class, () -> {
            sut.add(null, LocalDateTime.now(), "kitchen", "Dishwasher");
        });
        assertEquals(1, sut.getMap().size());
    }

    @Test
    void testAddDateTimeNull() {
        assertEquals(0, sut.getMap().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(null, null, "kitchen", "Dishwasher");
        });
        assertEquals(0, sut.getMap().size());
    }

    @Test
    void testAddLocationNull() {
        assertEquals(0, sut.getMap().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(null, LocalDateTime.now(), null, "Dishwasher");
        });
        assertEquals(0, sut.getMap().size());
    }

    @Test
    void testAddLocationChar256() {
        assertEquals(0, sut.getMap().size());
            sut.add(
                    null,
                    LocalDateTime.now(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing" +
                            " elit. Sed fermentum ullamcorper nulla, nec dapibus" +
                            " lectus bibendum eget. Nunc id consectetur velit. " +
                            "Proin vel nibh ut dolor vehicula pulvinar. Fusce " +
                            "blandit leo eget ex malesuada, vitae ullamcorper mauris.",
                    "Dishwasher"
            );
        assertEquals(1, sut.getMap().size());
    }

    @Test
    void testAddLocationCharOverflow() {
        assertEquals(0, sut.getMap().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(
                    null,
                    LocalDateTime.now(),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing" +
                            " elit. Sed fermentum ullamcorper nulla, nec dapibus" +
                            " lectus bibendum eget. Nunc id consectetur velit. " +
                            "Proin vel nibh ut dolor vehicula pulvinar. Fusce " +
                            "blandit leo eget ex malesuada, vitae ullamcorper mauris. ",
                    "Dishwasher"
            );
        });
        assertEquals(0, sut.getMap().size());
    }

    @Test
    void testRemoveTrue() {
        assertEquals(0, sut.getMap().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
        assertTrue(sut.remove(1));
        assertEquals(1, sut.getMap().size());
    }

    @Test
    void testRemoveFalse() {
        assertEquals(0, sut.getMap().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
        assertFalse(sut.remove(2));
        assertEquals(2, sut.getMap().size());
    }

    @Test
    void testFindOne() {
        assertEquals(0, sut.getMap().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
        assertEquals(task, sut.findOne(0));
        assertNull(sut.findOne(2));
    }

    @Test
    void testFindAll() {
        assertEquals(0, sut.getMap().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.getMap().size());
        Task task2 = sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.getMap().size());
        assertEquals(task, sut.findAll().get(0));
        assertEquals(task2, sut.findAll().get(1));
        sut.findAll().get(1).setLocation("laundry");
//        Figure out way to test that location has changed without relying on task method
//        assertEquals("laundry", sut.getMap().get(1).getDescription());
    }
}