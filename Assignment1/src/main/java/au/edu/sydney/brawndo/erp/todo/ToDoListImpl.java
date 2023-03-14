package au.edu.sydney.brawndo.erp.todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoListImpl implements ToDoList{

    private Integer idCounter = 0;
    private boolean externallyAssignedTaskIDs;

    private Map<Integer, Task> tasks;

    public ToDoListImpl() {
        externallyAssignedTaskIDs = false;
        tasks = new HashMap<>();
    }

    @Override
    public Task add(Integer id, LocalDateTime dateTime, String location, String description) throws IllegalArgumentException, IllegalStateException {

//        Checking ID
        if (id == null && externallyAssignedTaskIDs) {
//            A null id has been passed after a non-null id task has already been added
            throw new IllegalStateException("ToDoList with externally managed ID's has been passed a Task with no ID");
        } else if (id == null) {
//            A null id has been passed, however the ToDoList is managing ID numbers thus far
            id = idCounter;
            idCounter ++;
        } else if (!externallyAssignedTaskIDs){
//            A non-null id has been passed for the first time
            externallyAssignedTaskIDs = true;
        } else if (tasks.containsKey(id)) {
            throw new IllegalArgumentException("Cannot assign ID to task as this ID is already assigned to another task");
        }

//        Checking dateTime
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }

//        Checking location
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        } else if (location.length() > 256) {
            throw new IllegalArgumentException("location cannot be longer then 256 characters");
        }

//        Nothing to check for description
        Task newTask = new TaskImpl(id);
        newTask.setDateTime(dateTime);
        newTask.setLocation(location);
        newTask.setDescription(description);

        tasks.put(id, newTask);

        return newTask;
    }

    @Override
    public boolean remove(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Task findOne(int id) {
        return tasks.get(id);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findAll(boolean completed) {
        return null;
    }

    @Override
    public List<Task> findAll(LocalDateTime from, LocalDateTime to, Boolean completed) throws IllegalArgumentException {
        return null;
    }

    @Override
    public List<Task> findAll(Map<Task.Field, String> params, LocalDateTime from, LocalDateTime to, Boolean completed, boolean andSearch) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void clear() {

    }

    public Map<Integer, Task> getMap() {
        return tasks;
    }
}