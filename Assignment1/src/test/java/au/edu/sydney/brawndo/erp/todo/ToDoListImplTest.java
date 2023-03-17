package au.edu.sydney.brawndo.erp.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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
//        to my knowledge, since these tests also need to run on your implementations
//        of ToDoListImpl, so I cannot guarantee any new methods you would have.
        assertEquals(0, sut.findAll().size());
        sut.add(null, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        sut.add(null, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
    }

    @Test
    void testAddValidIdExternal() {
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
    }

    @Test
    void testAddIdExternalNotUnique() {
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(0, LocalDateTime.now(), "kitchen", "Dishwasher");
        });
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void testAddInternalThenExternal() {
//        Behaviour not specified yet
    }

    @Test
    void testAddExternalThenInternalId() {
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        assertThrows(IllegalStateException.class, () -> {
            sut.add(null, LocalDateTime.now(), "kitchen", "Dishwasher");
        });
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void testAddDateTimeNull() {
        assertEquals(0, sut.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(null, null, "kitchen", "Dishwasher");
        });
        assertEquals(0, sut.findAll().size());
    }

    @Test
    void testAddLocationNull() {
        assertEquals(0, sut.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> {
            sut.add(null, LocalDateTime.now(), null, "Dishwasher");
        });
        assertEquals(0, sut.findAll().size());
    }

    @Test
    void testAddLocationChar256() {
        assertEquals(0, sut.findAll().size());
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
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void testAddLocationCharOverflow() {
        assertEquals(0, sut.findAll().size());
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
        assertEquals(0, sut.findAll().size());
    }

    @Test
    void testRemoveTrue() {
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertTrue(sut.remove(1));
        assertEquals(1, sut.findAll().size());
    }

    @Test
    void testRemoveFalse() {
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertFalse(sut.remove(2));
        assertEquals(2, sut.findAll().size());
    }

    @Test
    void testFindOne() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertEquals(task, sut.findOne(0));
        assertNull(sut.findOne(2));
    }

    @Test
    void testFindAll() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertEquals(task, sut.findAll().get(0));
        assertEquals(task2, sut.findAll().get(1));
    }

    @Test
    void testFindAllNotCompleted() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertEquals(1, sut.findAll(false).size());
        assertEquals(task2, sut.findAll(false).get(0));
    }

    @Test
    void testFindAllCompleted() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.now(), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.now(), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        assertEquals(1, sut.findAll(true).size());
        assertEquals(task, sut.findAll(true).get(0));
    }

    @Test
    void testFindAll3AllValid() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        LocalDateTime from = LocalDateTime.of(2023, 12, 4, 1, 30);
        LocalDateTime to = LocalDateTime.of(2023, 12, 13, 1, 30);

        assertEquals(1, sut.findAll(from, to, null).size());

        assertEquals(0, sut.findAll(from, to, true).size());

        assertEquals(1, sut.findAll(from, to, false).size());

        assertEquals(1, sut.findAll(null, to, true).size());

        assertEquals(2, sut.findAll(null, to, null).size());

        assertEquals(2, sut.findAll(from, null, null).size());

        assertEquals(1, sut.findAll(null, null, true).size());

        assertEquals(2, sut.findAll(null, null, false).size());

        assertEquals(3, sut.findAll(null, null, null).size());
    }

    @Test
    void testFindAll3DateInvalid() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        LocalDateTime from = LocalDateTime.of(2023, 12, 13, 1, 30);
        LocalDateTime to = LocalDateTime.of(2023, 12, 4, 1, 30);

        assertThrows(IllegalArgumentException.class, () -> {
            sut.findAll(from, to, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sut.findAll(from, to, true);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sut.findAll(from, to, false);
        });
    }

    @Test
    void findALl5validAndSearch() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        LocalDateTime from = LocalDateTime.of(2023, 12, 4, 1, 30);
        LocalDateTime to = LocalDateTime.of(2023, 12, 13, 1, 30);
        Map<Task.Field, String> params1 = new HashMap<>();
        params1.put(Task.Field.DESCRIPTION, "Dishwasher");
        params1.put(Task.Field.LOCATION, "kitchen");
        Map<Task.Field, String> params2 = new HashMap<>();
        params2.put(Task.Field.LOCATION, "house");
        Map<Task.Field, String> params3 = new HashMap<>();
        params3.put(Task.Field.DESCRIPTION, "Dishwasher");
        Map<Task.Field, String> params4 = new HashMap<>();
        params4.put(Task.Field.DESCRIPTION, "Carpet");
        assertEquals(3, sut.findAll(null, null, null, null, true).size());
        assertEquals(2, sut.findAll(null, null, null, false, true).size());
        assertEquals(1, sut.findAll(null, null, null, true, true).size());
        assertEquals(2, sut.findAll(null, from, null, null, true).size());
        assertEquals(2, sut.findAll(null, from, null, false, true).size());
        assertEquals(0, sut.findAll(null, from, null, true, true).size());
        assertEquals(2, sut.findAll(null, null, to, null, true).size());
        assertEquals(1, sut.findAll(null, null, to, false, true).size());
        assertEquals(1, sut.findAll(null, null, to, true, true).size());
        assertEquals(1, sut.findAll(null, from, to, null, true).size());
        assertEquals(0, sut.findAll(null, from, to, true, true).size());
        assertEquals(1, sut.findAll(null, from, to, false, true).size());

        assertEquals(1, sut.findAll(params1, null, null, null, true).size());
        assertEquals(1, sut.findAll(params1, null, null, false, true).size());
        assertEquals(0, sut.findAll(params1, null, null, true, true).size());
        assertEquals(1, sut.findAll(params1, from, null, null, true).size());
        assertEquals(1, sut.findAll(params1, from, null, false, true).size());
        assertEquals(0, sut.findAll(params1, from, null, true, true).size());
        assertEquals(1, sut.findAll(params1, null, to, null, true).size());
        assertEquals(1, sut.findAll(params1, null, to, false, true).size());
        assertEquals(0, sut.findAll(params1, null, to, true, true).size());
        assertEquals(1, sut.findAll(params1, from, to, null, true).size());
        assertEquals(0, sut.findAll(params1, from, to, true, true).size());
        assertEquals(1, sut.findAll(params1, from, to, false, true).size());

        assertEquals(1, sut.findAll(params2, null, null, null, true).size());
        assertEquals(0, sut.findAll(params2, null, null, false, true).size());
        assertEquals(1, sut.findAll(params2, null, null, true, true).size());
        assertEquals(0, sut.findAll(params2, from, null, null, true).size());
        assertEquals(0, sut.findAll(params2, from, null, false, true).size());
        assertEquals(0, sut.findAll(params2, from, null, true, true).size());
        assertEquals(1, sut.findAll(params2, null, to, null, true).size());
        assertEquals(0, sut.findAll(params2, null, to, false, true).size());
        assertEquals(1, sut.findAll(params2, null, to, true, true).size());
        assertEquals(0, sut.findAll(params2, from, to, null, true).size());
        assertEquals(0, sut.findAll(params2, from, to, true, true).size());
        assertEquals(0, sut.findAll(params2, from, to, false, true).size());

        assertEquals(2, sut.findAll(params3, null, null, null, true).size());
        assertEquals(2, sut.findAll(params3, null, null, false, true).size());
        assertEquals(0, sut.findAll(params3, null, null, true, true).size());
        assertEquals(2, sut.findAll(params3, from, null, null, true).size());
        assertEquals(2, sut.findAll(params3, from, null, false, true).size());
        assertEquals(0, sut.findAll(params3, from, null, true, true).size());
        assertEquals(1, sut.findAll(params3, null, to, null, true).size());
        assertEquals(1, sut.findAll(params3, null, to, false, true).size());
        assertEquals(0, sut.findAll(params3, null, to, true, true).size());
        assertEquals(1, sut.findAll(params3, from, to, null, true).size());
        assertEquals(0, sut.findAll(params3, from, to, true, true).size());
        assertEquals(1, sut.findAll(params3, from, to, false, true).size());

        Task task4 = sut.add(3, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", null);
        assertEquals(4, sut.findAll().size());
        assertEquals(0, sut.findAll(params4, null, null, null, true).size());
        assertEquals(0, sut.findAll(params4, null, null, false, true).size());
        assertEquals(0, sut.findAll(params4, null, null, true, true).size());
        assertEquals(0, sut.findAll(params4, from, null, null, true).size());
        assertEquals(0, sut.findAll(params4, from, null, false, true).size());
        assertEquals(0, sut.findAll(params4, from, null, true, true).size());
        assertEquals(0, sut.findAll(params4, null, to, null, true).size());
        assertEquals(0, sut.findAll(params4, null, to, false, true).size());
        assertEquals(0, sut.findAll(params4, null, to, true, true).size());
        assertEquals(0, sut.findAll(params4, from, to, null, true).size());
        assertEquals(0, sut.findAll(params4, from, to, true, true).size());
        assertEquals(0, sut.findAll(params4, from, to, false, true).size());
    }

    @Test
    void findALl5validOrSearch() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        LocalDateTime from = LocalDateTime.of(2023, 12, 4, 1, 30);
        LocalDateTime to = LocalDateTime.of(2023, 12, 13, 1, 30);
        Map<Task.Field, String> params1 = new HashMap<>();
        params1.put(Task.Field.DESCRIPTION, "Dishwasher");
        params1.put(Task.Field.LOCATION, "kitchen");
        Map<Task.Field, String> params2 = new HashMap<>();
        params2.put(Task.Field.LOCATION, "house");
        Map<Task.Field, String> params3 = new HashMap<>();
        params3.put(Task.Field.DESCRIPTION, "Dishwasher");
        Map<Task.Field, String> params4 = new HashMap<>();
        params4.put(Task.Field.DESCRIPTION, "Carpet");
        assertEquals(3, sut.findAll(null, null, null, null, false).size());
        assertEquals(3, sut.findAll(null, null, null, false, false).size());
        assertEquals(3, sut.findAll(null, null, null, true, false).size());
        assertEquals(2, sut.findAll(null, from, null, null, false).size());
        assertEquals(2, sut.findAll(null, from, null, false, false).size());
        assertEquals(3, sut.findAll(null, from, null, true, false).size());
        assertEquals(2, sut.findAll(null, null, to, null, false).size());
        assertEquals(3, sut.findAll(null, null, to, false, false).size());
        assertEquals(2, sut.findAll(null, null, to, true, false).size());
        assertEquals(1, sut.findAll(null, from, to, null, false).size());
        assertEquals(2, sut.findAll(null, from, to, true, false).size());
        assertEquals(2, sut.findAll(null, from, to, false, false).size());

        assertEquals(3, sut.findAll(params1, null, null, null, false).size());
        assertEquals(3, sut.findAll(params1, null, null, false, false).size());
        assertEquals(3, sut.findAll(params1, null, null, true, false).size());
        assertEquals(2, sut.findAll(params1, from, null, null, false).size());
        assertEquals(2, sut.findAll(params1, from, null, false, false).size());
        assertEquals(3, sut.findAll(params1, from, null, true, false).size());
        assertEquals(2, sut.findAll(params1, null, to, null, false).size());
        assertEquals(3, sut.findAll(params1, null, to, false, false).size());
        assertEquals(2, sut.findAll(params1, null, to, true, false).size());
        assertEquals(1, sut.findAll(params1, from, to, null, false).size());
        assertEquals(2, sut.findAll(params1, from, to, true, false).size());
        assertEquals(2, sut.findAll(params1, from, to, false, false).size());

        assertEquals(3, sut.findAll(params2, null, null, null, false).size());
        assertEquals(3, sut.findAll(params2, null, null, false, false).size());
        assertEquals(3, sut.findAll(params2, null, null, true, false).size());
        assertEquals(3, sut.findAll(params2, from, null, null, false).size());
        assertEquals(3, sut.findAll(params2, from, null, false, false).size());
        assertEquals(3, sut.findAll(params2, from, null, true, false).size());
        assertEquals(2, sut.findAll(params2, null, to, null, false).size());
        assertEquals(3, sut.findAll(params2, null, to, false, false).size());
        assertEquals(2, sut.findAll(params2, null, to, true, false).size());
        assertEquals(2, sut.findAll(params2, from, to, null, false).size());
        assertEquals(2, sut.findAll(params2, from, to, true, false).size());
        assertEquals(3, sut.findAll(params2, from, to, false, false).size());

        assertEquals(3, sut.findAll(params3, null, null, null, false).size());
        assertEquals(3, sut.findAll(params3, null, null, false, false).size());
        assertEquals(3, sut.findAll(params3, null, null, true, false).size());
        assertEquals(2, sut.findAll(params3, from, null, null, false).size());
        assertEquals(2, sut.findAll(params3, from, null, false, false).size());
        assertEquals(3, sut.findAll(params3, from, null, true, false).size());
        assertEquals(3, sut.findAll(params3, null, to, null, false).size());
        assertEquals(3, sut.findAll(params3, null, to, false, false).size());
        assertEquals(3, sut.findAll(params3, null, to, true, false).size());
        assertEquals(2, sut.findAll(params3, from, to, null, false).size());
        assertEquals(3, sut.findAll(params3, from, to, true, false).size());
        assertEquals(2, sut.findAll(params3, from, to, false, false).size());

        Task task4 = sut.add(3, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", null);
        assertEquals(4, sut.findAll().size());
        assertEquals(4, sut.findAll(params4, null, null, null, false).size());
        assertEquals(4, sut.findAll(params4, null, null, false, false).size());
        assertEquals(4, sut.findAll(params4, null, null, true, false).size());
        assertEquals(3, sut.findAll(params4, from, null, null, false).size());
        assertEquals(3, sut.findAll(params4, from, null, false, false).size());
        assertEquals(4, sut.findAll(params4, from, null, true, false).size());
        assertEquals(2, sut.findAll(params4, null, to, null, false).size());
        assertEquals(4, sut.findAll(params4, null, to, false, false).size());
        assertEquals(2, sut.findAll(params4, null, to, true, false).size());
        assertEquals(1, sut.findAll(params4, from, to, null, false).size());
        assertEquals(2, sut.findAll(params4, from, to, true, false).size());
        assertEquals(3, sut.findAll(params4, from, to, false, false).size());
    }

    @Test
    void findALl5InvalidOrSearch() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        LocalDateTime from = LocalDateTime.of(2024, 12, 4, 1, 30);
        LocalDateTime to = LocalDateTime.of(2023, 12, 13, 1, 30);
        Map<Task.Field, String> params1 = new HashMap<>();
        params1.put(Task.Field.DESCRIPTION, "Dishwasher");
        params1.put(Task.Field.LOCATION, "kitchen");
        Map<Task.Field, String> params2 = new HashMap<>();
        params2.put(Task.Field.LOCATION, "house");
        Map<Task.Field, String> params3 = new HashMap<>();
        params3.put(Task.Field.DESCRIPTION, "Dishwasher");
        Map<Task.Field, String> params4 = new HashMap<>();
        params4.put(Task.Field.DESCRIPTION, "Carpet");
        assertThrows(IllegalArgumentException.class, () -> {
           sut.findAll(null, from, to, null, false);
        });
    }

    @Test
    void testClear() {
        assertEquals(0, sut.findAll().size());
        Task task = sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        task.complete();
        assertEquals(1, sut.findAll().size());
        Task task2 = sut.add(1, LocalDateTime.of(2023, 12, 12, 1, 30), "kitchen", "Dishwasher");
        assertEquals(2, sut.findAll().size());
        Task task3 = sut.add(2, LocalDateTime.of(2024, 12, 12, 1, 30), "butlers kitchen", "Dishwasher");
        assertEquals(3, sut.findAll().size());
        sut.clear();
        assertEquals(0, sut.findAll().size());
        sut.add(0, LocalDateTime.of(2023, 12, 3, 1, 30), "house", "feed da cat");
        assertEquals(1, sut.findAll().size());
    }
}