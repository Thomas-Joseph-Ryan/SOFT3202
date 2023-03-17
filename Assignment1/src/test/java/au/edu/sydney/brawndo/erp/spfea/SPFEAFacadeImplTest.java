package au.edu.sydney.brawndo.erp.spfea;

import au.edu.sydney.brawndo.erp.todo.Task;
import au.edu.sydney.brawndo.erp.todo.ToDoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SPFEAFacadeImplTest {

//    Used ChatGPT to help with test case ideas

    private SPFEAFacade sut;

    @BeforeEach
    void createFacade() {
        sut = new SPFEAFacadeImpl();
    }

    @Test
    void testAddCustomer() {

//        Test Valid input okay
        sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

//        Test null phone only
        sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

//        Test identical name invalid
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("pika", "chu", "0414121212", "pika_chu2@pokedex.net");
        });

//        Test customer null phone null email fails
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "ketchum", null, null);
        });

//        Test empty name fails
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("", "ketchum", "+(61)414121212", "pika_chu2@pokedex.net");
        });

//        Test empty last name fails
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "", "+(61)414121212", "pika_chu2@pokedex.net");
        });

//        Test null name fails
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer(null, "ketchum", "+(61)414121212", "pika_chu2@pokedex.net");
        });

//        Test null last name fails
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", null, "+(61)414121212", "pika_chu2@pokedex.net");
        });

//        Invalid phone number
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "ketchum", "+(61)414-121-212", "pika_chu2@pokedex.net");
        });

//        Invalid phone number
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "ketchum", "", "pika_chu2@pokedex.net");
        });

//        Invalid email
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "ketchum", "+(61)414-121-212", "pika_chu2ATpokedex.net");
        });


//        Invalid email
        assertThrows(IllegalArgumentException.class, () -> {
            sut.addCustomer("ash", "ketchum", "+(61)414-121-212", "");
        });
    }

    @Test
    void testAddNewTask() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);
        String description = "this is a description";
        String location = "HOME OFFICE";

        sut.setToDoProvider(null);

//        Null ToDoModule set
        assertThrows(IllegalStateException.class, () -> {
           sut.addNewTask(localDateTime, description, location);
        });


        ToDoList mockToDoList = Mockito.mock(ToDoList.class);
        sut.setToDoProvider(mockToDoList);

//        null date
        assertThrows(IllegalArgumentException.class, ()->{
           sut.addNewTask(null, description, location);
        });

//        date before now
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(LocalDateTime.of(2003, 12, 3, 5, 30), description, location);
        });

//        description null
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(localDateTime, null, location);
        });

//        description empty
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(localDateTime, "", location);
        });

//        location empty
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(localDateTime, description, "");
        });

//        location null
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(localDateTime, description, null);
        });

//        Location not one of the three
        assertThrows(IllegalArgumentException.class, ()->{
            sut.addNewTask(localDateTime, description, "BATHROOM");
        });

//        Valid call
        sut.addNewTask(localDateTime, description, location);
        Mockito.verify(mockToDoList).add(null, localDateTime, description, location);

        String newLocation = "CUSTOMER SITE";
        sut.addNewTask(localDateTime, description, newLocation);
        Mockito.verify(mockToDoList).add(null, localDateTime, description, newLocation);

        newLocation = "MOBILE";
        sut.addNewTask(localDateTime, description, newLocation);
        Mockito.verify(mockToDoList).add(null, localDateTime, description, newLocation);
    }

    @Test
    void testCompleteTask() {
        Task mockTask = Mockito.mock(Task.class);
        ToDoList mockToDoList = Mockito.mock(ToDoList.class);

        sut.setToDoProvider(null);

//       Null to do provider set
        assertThrows(IllegalStateException.class, () -> {
           sut.completeTask(1);
        });

        sut.setToDoProvider(mockToDoList);

        sut.addNewTask(LocalDateTime.now().plusDays(2), "Do Homework", "HOME OFFICE");
//        Adding two tasks so that an ID of 1 is guaranteed no matter if the id count starts at 0 or 1
        sut.addNewTask(LocalDateTime.now().plusDays(2), "Do laundry", "HOME OFFICE");
    }
}