package au.edu.sydney.brawndo.erp.spfea;

import au.edu.sydney.brawndo.erp.todo.Task;
import au.edu.sydney.brawndo.erp.todo.ToDoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

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
        Mockito.verify(mockToDoList).add(nullable(Integer.class), any(localDateTime.getClass()), any(String.class), any(String.class));

        String newLocation = "CUSTOMER SITE";
        sut.addNewTask(localDateTime, description, newLocation);
        Mockito.verify(mockToDoList).add(nullable(Integer.class), any(localDateTime.getClass()), any(String.class), any(String.class));

        newLocation = "MOBILE";
        sut.addNewTask(localDateTime, description, newLocation);
        Mockito.verify(mockToDoList).add(nullable(Integer.class), any(localDateTime.getClass()), any(String.class), any(String.class));
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

        assertThrows(IllegalArgumentException.class, () -> {
            sut.completeTask(1);
        });

        sut.addNewTask(LocalDateTime.now().plusDays(2), "Do Homework", "HOME OFFICE");
//        Adding two tasks so that an ID of 1 is guaranteed no matter if the id count starts at 0 or 1
        sut.addNewTask(LocalDateTime.now().plusDays(2), "Do laundry", "HOME OFFICE");

        sut.completeTask(1);

        assertThrows(IllegalStateException.class, () -> {
           sut.completeTask(1);
        });
    }

    @Test
    void testSetToDoProvider() {
        ToDoList mockToDoList = Mockito.mock(ToDoList.class);
        sut.setToDoProvider(null);

        assertThrows(IllegalStateException.class, () -> {
            sut.getAllTasks();
        });

        sut.setToDoProvider(mockToDoList);

        sut.getAllTasks();
        Mockito.verify(mockToDoList).findAll();

        ToDoList mockToDoList2 = Mockito.mock(ToDoList.class);

        sut.setToDoProvider(mockToDoList2);
        sut.getAllTasks();
        Mockito.verify(mockToDoList2).findAll();
    }

    @Test
    void getAllTasks() {
        assertThrows(IllegalStateException.class, () -> {
            sut.getAllTasks();
        });

        ToDoList mockToDoList = Mockito.mock(ToDoList.class);

        sut.setToDoProvider(mockToDoList);

        sut.getAllTasks();
        Mockito.verify(mockToDoList).findAll();
    }

    @Test
    void getCustomerById() {
        //        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());


//        Test empty fname
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getCustomerID("", "chu");
        });

//        Test null fname
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getCustomerID(null, "chu");
        });

//        Test empty lname
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getCustomerID("pika", "");
        });

//        Test null lname
        assertThrows(IllegalArgumentException.class, () -> {
            sut.getCustomerID("pika", null);
        });

        assertEquals(id1, sut.getCustomerID("pika", "chu"));
        assertEquals(id2, sut.getCustomerID("tom", "ryan"));
        assertEquals(id3, sut.getCustomerID("lord", "farquad"));
    }

    @Test
    void testGetAllCustomers() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());
    }

    @Test
    void testGetCustomerPhone() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

        assertEquals("+(61)412121212", sut.getCustomerPhone(id1));
        assertNull(sut.getCustomerPhone(id2));
        assertEquals("+(61)432678945", sut.getCustomerPhone(id3));
        assertThrows(IllegalArgumentException.class, ()-> {
            sut.getCustomerPhone(20);
        });
    }

    @Test
    void testSetCustomerPhone() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

        sut.setCustomerPhone(id1, null);
        assertNull(sut.getCustomerPhone(id1));

        sut.setCustomerPhone(id2, "+(21)438094012");
        assertEquals("+(21)438094012", sut.getCustomerPhone(id2));

        sut.setCustomerPhone(id3, "+(99)415708932");
        assertEquals("+(99)415708932", sut.getCustomerPhone(id3));

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerPhone(id3, null);
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerPhone(20, null);
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerPhone(id3, "0451-875-829");
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerPhone(id3, "");
        });
    }

    @Test
    void testGetCustomerEmail() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

        assertEquals("pika_chu@pokedex.net", sut.getCustomerEmail(id1));
        assertEquals("tom@gmail.com", sut.getCustomerEmail(id2));
        assertNull(sut.getCustomerEmail(id3));
        assertThrows(IllegalArgumentException.class, ()-> {
            sut.getCustomerEmail(20);
        });
    }

    @Test
    void testSetCustomerEmail() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

        sut.setCustomerEmail(id1, null);
        assertNull(sut.getCustomerEmail(id1));

        sut.setCustomerEmail(id2, "newtom@gmail.com");
        assertEquals("newtom@gmail.com", sut.getCustomerEmail(id2));

        sut.setCustomerEmail(id3, "ash@pokedex.net");
        assertEquals("ash@pokedex.net", sut.getCustomerEmail(id3));

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerEmail(id2, null);
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerEmail(20, null);
        });

//        THE BELOW FUNCTIONALITY IS NOT EXPLICITY STATED

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerEmail(id2, "");
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.setCustomerEmail(id2, "ThomasATgmail.com");
        });
    }

    @Test
    void testRemoveCustomer() {
        assertEquals(0, sut.getAllCustomers().size());
//        Test Valid input okay
        int id1 = sut.addCustomer("pika", "chu", "+(61)412121212", "pika_chu@pokedex.net");
        assertEquals(1, sut.getAllCustomers().size());

        assertEquals("pika, chu", sut.getAllCustomers().get(0));

//        Test null phone only
        int id2 = sut.addCustomer("tom", "ryan", null, "tom@gmail.com");
        assertEquals(2, sut.getAllCustomers().size());

//        Test null email only
        int id3 = sut.addCustomer("lord", "farquad", "+(61)432678945",  null);
        assertEquals(3, sut.getAllCustomers().size());

        sut.removeCustomer(id1);
//        Probably could change this check condition
        assertEquals(2, sut.getAllCustomers().size());
        assertNull(sut.getCustomerID("pika", "chu"));

        assertThrows(IllegalArgumentException.class, ()-> {
            sut.removeCustomer(20);
        });
    }

}