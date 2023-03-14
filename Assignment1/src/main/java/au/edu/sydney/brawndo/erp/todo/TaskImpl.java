package au.edu.sydney.brawndo.erp.todo;

import java.time.LocalDateTime;

public class TaskImpl implements Task {

    public TaskImpl(Integer id) {

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public LocalDateTime getDateTime() {
        return null;
    }

    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) throws IllegalArgumentException {

    }

    @Override
    public void setLocation(String location) throws IllegalArgumentException {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void complete() throws IllegalStateException {

    }

    @Override
    public String getField(Field field) throws IllegalArgumentException {
        return null;
    }
}