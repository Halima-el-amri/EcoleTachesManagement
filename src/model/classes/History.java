package model.classes;

import java.time.LocalDate;
import java.util.List;

public class History {
    private int id ;
    private String nameOperation;
    private LocalDate dateOperation;
    private Task task;
    private User user;

    public History(String nameOperation, LocalDate dateOperation, Task task, User user) {
        this.nameOperation = nameOperation;
        this.dateOperation = dateOperation;
        this.task = task;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOperation() {
        return nameOperation;
    }

    public void setNameOperation(String nameOperation) {
        this.nameOperation = nameOperation;
    }

    public LocalDate getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDate dateOperation) {
        this.dateOperation = dateOperation;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", nameOperation='" + nameOperation + '\'' +
                ", dateOperation=" + dateOperation +
                ", task=" + task +
                ", user=" + user +
                '}';
    }
}
