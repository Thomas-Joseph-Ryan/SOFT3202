package au.edu.sydney.brawndo.erp.todo;

import org.junit.jupiter.api.Test;

public class ToDoFactoryTest {
    @Test
    void testToDoListCreation() {
        ToDoFactory toDoFactory = new ToDoFactory();
        toDoFactory.makeToDoList();
    }

}