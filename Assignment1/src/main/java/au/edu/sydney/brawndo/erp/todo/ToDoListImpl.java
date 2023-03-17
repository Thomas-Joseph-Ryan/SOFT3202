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
        Task newTask = new TaskImpl(id, dateTime, location, description);
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
        List<Task> tasksList = new ArrayList<>(tasks.values());
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasksList) {
            if (task.isCompleted() == completed) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    @Override
    public List<Task> findAll(LocalDateTime from, LocalDateTime to, Boolean completed) throws IllegalArgumentException {
        if (from != null && to != null) {
            if (from.isAfter(to)) {
                throw new IllegalArgumentException("from cannot be chronologically after to.");
            }
        }

        List<Task> filtered = new ArrayList<>();

        for (Task task : new ArrayList<>(tasks.values())) {
            if (completed != null && task.isCompleted() != completed) {
                continue;
            }

            if (from != null && task.getDateTime().isBefore(from)) {
                continue;
            }

            if (to != null && task.getDateTime().isAfter(to)) {
                continue;
            }

            filtered.add(task);
        }
        return filtered;
    }

    @Override
    public List<Task> findAll(Map<Task.Field, String> params, LocalDateTime from, LocalDateTime to, Boolean completed, boolean andSearch) throws IllegalArgumentException {
        List<Task> filtered = new ArrayList<>();
        if (andSearch) {
            List<Task> filteredDatesAndComplete = findAll(from, to, completed);
            for (Task task : filteredDatesAndComplete) {
                if (params == null) {
                    filtered.add(task);
                    continue;
                }
                filterParams(params, filtered, task);
            }
        } else {
            for (Task task : new ArrayList<>(tasks.values())) {
                if (completed != null && task.isCompleted() == completed) {
                    filtered.add(task);
                    continue;
                }

                if (from != null && to != null) {
                    if (from.isAfter(to)) {
                        throw new IllegalArgumentException("from cannot be chronologically after to.");
                    }
                }
                if ((from == null || task.getDateTime().isAfter(from)) && (to == null || task.getDateTime().isBefore(to))) {
                    filtered.add(task);
                    continue;
                }

                filterParams(params, filtered, task);
            }
        }
        return filtered;
    }

    private void filterParams(Map<Task.Field, String> params, List<Task> filtered, Task task) {
        if (params != null) {
            boolean twoParams = false;
            int i = 0;
            if (params.entrySet().size() == 2) {
                twoParams = true;
            }
            for (Map.Entry<Task.Field, String> entry : params.entrySet()) {
                String fieldValue = task.getField(entry.getKey());
                if (fieldValue == null || !fieldValue.contentEquals(entry.getValue())) {
                    break;
                }

                if (!twoParams) {
                    filtered.add(task);
                    break;
                }

                if (i == 0) {
                    i++;
                } else {
                    filtered.add(task);
                }
            }
        }
    }

    @Override
    public void clear() {
        tasks.clear();
        idCounter = 0;
    }

}