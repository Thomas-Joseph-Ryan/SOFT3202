package au.edu.sydney.brawndo.erp.todo;

import java.time.LocalDateTime;

public class TaskImpl implements Task {

    private Integer id;
    private LocalDateTime dateTime;
    private String location;
    private String description;
    private boolean completed = false;

    public TaskImpl(int id, LocalDateTime dateTime, String location, String description) {
        this.id = id;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) throws IllegalArgumentException {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        this.dateTime = dateTime;
    }

    @Override
    public void setLocation(String location) throws IllegalArgumentException {
        if (location == null) {
            throw new IllegalArgumentException("location must not be null.");
        } else if (location.length() > 256) {
            throw new IllegalArgumentException("location must be 256 char or less.");
        }
        this.location = location;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void complete() throws IllegalStateException {
        if (this.completed) {
            throw new IllegalStateException("Cannot complete task that has already been completed");
        }
        this.completed = true;
    }

    @Override
    public String getField(Field field) throws IllegalArgumentException {
        if (field == null) {
            throw new IllegalArgumentException("field cannot be null.");
        }
        if (field == Field.LOCATION) {
            return this.location;
        }
        return this.description;
    }
}